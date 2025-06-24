package com.example.core.data.mapper

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun <T, R> Flow<List<T>>.listMap(
    transform: suspend (T) -> R
) = map { list -> list.map { transform(it) } }