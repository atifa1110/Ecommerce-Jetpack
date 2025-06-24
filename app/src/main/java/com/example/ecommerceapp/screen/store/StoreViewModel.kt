package com.example.ecommerceapp.screen.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.core.data.network.response.EcommerceResponse
import com.example.ecommerceapp.firebase.ProductAnalyticsManager
import com.example.core.domain.usecase.GetProductFilterUseCase
import com.example.core.domain.usecase.GetSearchProductUseCase
import com.example.core.domain.usecase.RefreshTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StoreUiState(
    val isLoading: Boolean = false,
    val isClickedGrid: Boolean = false,
    val isSearchOpen: Boolean = false,
    val isBottomSheetOpen: Boolean = false,
    val selectedSort: String? = null,
    val selectedBrand: String? = null,
    val lowestPrice: String = "",
    val highestPrice: String = "",
    val searchSuggestionState: SearchSuggestionState = SearchSuggestionState(),
    val productFilter: ProductFilter = ProductFilter(),
    val userMessage: String? = null
)

data class SearchSuggestionState(
    val suggestions: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isError : Boolean = false
)

data class ProductFilter(
    val search: String? = null,
    val brand: String? = null,
    val lowest: Int? = null,
    val highest: Int? = null,
    val sort: String? = null,
)

val ProductFilter.activeFilter: List<String>
    get() = buildList {
        search?.takeIf { it.isNotBlank() }?.let { add(it) }
        brand?.let { add(it) }
        sort?.let { add(it) }
        lowest?.let { add(it.toString()) }
        highest?.let { add(it.toString()) }
    }

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val getProductFilterUseCase: GetProductFilterUseCase,
    private val getSearchProductUseCase: GetSearchProductUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val productAnalyticsManager: ProductAnalyticsManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(StoreUiState())
    val uiState: StateFlow<StoreUiState> = _uiState

    fun refreshToken(onSuccess: () -> Unit) = viewModelScope.launch {
        refreshTokenUseCase.invoke().collect { result ->
            when (result) {
                is EcommerceResponse.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            userMessage = result.value,
                            productFilter = it.productFilter.copy()
                        )
                    }
                    onSuccess()
                }

                is EcommerceResponse.Failure -> {
                    _uiState.update {
                        it.copy(isLoading = false, userMessage = result.error)
                    }
                }

                is EcommerceResponse.Loading -> {
                    _uiState.update { it.copy(isLoading = true, userMessage = null) }
                }
            }
        }
    }

    // Called when user selects a suggestion
    fun onSuggestionSelected(suggestion: String) {
        _uiState.update {
            it.copy(
                productFilter = it.productFilter.copy(search = suggestion),
                isSearchOpen = false,
                searchSuggestionState = SearchSuggestionState() // reset suggestions
            )
        }
        productAnalyticsManager.trackSearchSuggestionSelected(suggestion)
    }

    fun setSearchScreenOpen(open: Boolean) {
        _uiState.update { it.copy(isSearchOpen = open) }
        if (open) {
            productAnalyticsManager.trackSearchScreenViewed()
        } else {
            productAnalyticsManager.trackSearchScreenClosed()
        }
    }

    fun setBottomSheetOpen(open: Boolean) {
        val current = _uiState.value.productFilter
        _uiState.update {
            if (open) {
                it.copy(
                    isBottomSheetOpen = true,
                    selectedBrand = current.brand,
                    selectedSort = current.sort,
                    lowestPrice = current.lowest?.toString() ?: "",
                    highestPrice = current.highest?.toString() ?: ""
                )
            } else {
                it.copy(
                    isBottomSheetOpen = false,
                    selectedBrand = null,
                    selectedSort = null,
                    lowestPrice = "",
                    highestPrice = ""
                )
            }
        }
    }

    fun updateSelectedBrand(brand: String) {
        _uiState.update { it.copy(selectedBrand = brand) }
    }

    fun updateSelectedSort(sort: String) {
        _uiState.update { it.copy(selectedSort = sort) }
    }

    fun updatePriceLowest(lowest: String) {
        _uiState.update { it.copy(lowestPrice = lowest) }
    }

    fun updatePriceHighest(highest: String) {
        _uiState.update { it.copy(highestPrice = highest) }
    }

    fun toggleClickedGrid() {
        _uiState.update { current ->
            current.copy(isClickedGrid = !current.isClickedGrid)
        }
    }

    fun searchQuery(search: String?) {
        _uiState.update { it.copy(
            productFilter = it.productFilter.copy(search = search))
        }
    }

    fun setQuery(brand: String?, lowest: String?, highest: String?, sort: String?) {
        productAnalyticsManager.trackFilterButtonClicked()
        _uiState.update {
            it.copy(
                productFilter = it.productFilter.copy(
                    brand = brand?.takeIf { it.isNotBlank() },
                    lowest = lowest?.toIntOrNull(),
                    highest = highest?.toIntOrNull(),
                    sort = sort?.takeIf { it.isNotBlank() }
                )
            )
        }

        val updatedFilter = uiState.value.productFilter
        val activeFilters = updatedFilter.activeFilter

        productAnalyticsManager.trackApplyFilter(
            filterType = activeFilters.joinToString(","),
            filterValue = activeFilters.joinToString(","),
            productListContext = "store_main_list"
        )
    }

    fun resetQuery() {
        productAnalyticsManager.trackFilterResetButtonClicked()
        _uiState.update {
            it.copy(productFilter = ProductFilter())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val getProductsFilter = uiState
        .map { it.productFilter }
        .distinctUntilChanged() // only trigger when filter actually changes
        .flatMapLatest { filter ->
            getProductFilterUseCase(
                filter.search,
                filter.brand,
                filter.lowest,
                filter.highest,
                filter.sort
            )
        }.cachedIn(viewModelScope)

    fun fetchSearchSuggestions() = viewModelScope.launch {
        productAnalyticsManager.trackSearchButtonClicked()
        val search = uiState.value.productFilter.search ?: ""
        getSearchProductUseCase.invoke(search)
            .collect { result ->
                when (result) {
                    EcommerceResponse.Loading -> {
                        _uiState.update {
                            it.copy(
                                searchSuggestionState = it.searchSuggestionState.copy(isLoading = true, isError = false, isSuccess = false)
                            )
                        }
                    }

                    is EcommerceResponse.Failure -> {
                        _uiState.update {
                            it.copy(
                                userMessage = result.error,
                                searchSuggestionState = it.searchSuggestionState.copy(
                                    isLoading = false,
                                    isSuccess = false,
                                    isError = true
                                )
                            )
                        }
                        productAnalyticsManager.trackSearchFailed(search,result.error)
                    }
                    is EcommerceResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                searchSuggestionState = it.searchSuggestionState.copy(
                                    suggestions = result.value,
                                    isLoading = false,
                                    isError = false,
                                    isSuccess = true,
                                )
                            )
                        }

                        productAnalyticsManager.trackSearchSuccess(search, result.value.size)
                        productAnalyticsManager.trackViewSearchResults(search, result.value)
                    }
                }
            }
        }

}


