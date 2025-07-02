package com.example.ecommerceapp.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.twotone.DeleteOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ecommerceapp.R
import com.example.core.ui.model.Cart
import com.example.core.ui.model.ItemTransaction
import com.example.core.ui.model.Notification
import com.example.core.ui.model.Payment
import com.example.core.ui.model.Product
import com.example.core.ui.model.Review
import com.example.core.ui.model.Transaction
import com.example.core.ui.model.Wishlist
import com.example.ecommerceapp.screen.status.RatingBar
import com.example.ecommerceapp.ui.theme.DarkPurple
import com.example.ecommerceapp.ui.theme.EcommerceAppTheme
import com.example.ecommerceapp.ui.theme.poppins
import com.example.ecommerceapp.utils.currency

@Composable
fun SearchCard(
    modifier: Modifier,
    productName : String
){
    ListItem(
        modifier = modifier,
        headlineContent = {
            Text(
                text = productName,
                style = MaterialTheme.typography.bodySmall
            )
        },
        leadingContent = {
            Icon(
                modifier = Modifier.size(18.dp),
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        trailingContent = {
            Icon(
                modifier = Modifier.size(18.dp),
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent,
            headlineColor = MaterialTheme.colorScheme.onBackground,
            leadingIconColor = MaterialTheme.colorScheme.onBackground,
            trailingIconColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

@Composable
fun ProductCardGrid(
    product: Product,
    onClickCard: () -> Unit
) {

    Card(modifier = Modifier
        .width(186.dp)
        .clickable {
            onClickCard()
        },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Column {
            Card(
                modifier = Modifier.size(186.dp),
                shape = RoundedCornerShape(
                    topEnd = 8.dp,
                    topStart = 8.dp,
                    bottomEnd = 0.dp,
                    bottomStart = 0.dp
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = if (product.image.isNotEmpty()) product.image else R.drawable.thumbnail,
                        contentDescription = "Products image",
                        contentScale = ContentScale.FillBounds,
                        // Optional: add placeholder and error images for smoother UX
                        placeholder = painterResource(id = R.drawable.thumbnail),
                        error = painterResource(id = R.drawable.thumbnail)
                    )
                }
            }

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = product.productName,
                    maxLines = 2,
                    style = MaterialTheme.typography.bodySmall,
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = currency(product.productPrice),
                    fontWeight = FontWeight.W600,
                    fontSize = 14.sp,
                    fontFamily = poppins
                )

                Row(
                    Modifier.padding(top = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(12.dp),
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Account"
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text  = product.store,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Row(
                    Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(12.dp),
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Star"
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "${product.productRating} | ${stringResource(R.string.sold)} ${product.sale}",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Composable
fun ProductCardList(
    product: Product,
    onClickCard: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClickCard()
            },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Box {
            Row(
                modifier = Modifier.padding(10.dp)
            ) {
                Card(
                    modifier = Modifier.size(80.dp),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            modifier = Modifier.fillMaxSize(),
                            model = if (product.image.isNotEmpty()) product.image else R.drawable.thumbnail,
                            contentDescription = "Products image",
                            contentScale = ContentScale.FillBounds,
                            // Optional: add placeholder and error images for smoother UX
                            placeholder = painterResource(id = R.drawable.thumbnail),
                            error = painterResource(id = R.drawable.thumbnail)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp)
                ) {
                    Text(
                        text = product.productName,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        modifier = Modifier.padding(vertical = 5.dp),
                        text = currency(product.productPrice),
                        fontWeight = FontWeight.W600,
                        fontSize = 14.sp,
                        fontFamily = poppins
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.size(12.dp),
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Account"
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = product.store,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.size(12.dp),
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Star"
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "${product.productRating} | ${stringResource(R.string.sold)} ${product.sale}",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WishlistCardList(
    wishlist : Wishlist,
    onAddToCart: () -> Unit,
    onDeleteFavorite: (id: String) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Box {
            Column(modifier = Modifier.padding(10.dp)) {
                Row {
                    Card(
                        modifier = Modifier.size(80.dp),
                        shape = RoundedCornerShape(5.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                modifier = Modifier.fillMaxSize(),
                                model = if (wishlist.productImage.isNotEmpty()) wishlist.productImage else R.drawable.thumbnail,
                                contentDescription = "Products image",
                                contentScale = ContentScale.FillBounds,
                                // Optional: add placeholder and error images for smoother UX
                                placeholder = painterResource(id = R.drawable.thumbnail),
                                error = painterResource(id = R.drawable.thumbnail)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))

                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp)
                    ) {
                        Text(
                            text = wishlist.productName,
                            lineHeight = 15.sp,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            text = currency(wishlist.unitPrice),
                            fontWeight = FontWeight.W600,
                            fontSize = 14.sp,
                            fontFamily = poppins
                        )
                        Row(
                            Modifier.padding(top = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.size(12.dp),
                                imageVector = Icons.Filled.AccountCircle,
                                contentDescription = "Account"
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = wishlist.store.toString(),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }

                        Row(
                            Modifier.padding(top = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.size(12.dp),
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Star"
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "${wishlist.productRating} | ${stringResource(R.string.sold)} ${wishlist.sale}",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row {
                    Card(
                        modifier = Modifier
                            .size(32.dp)
                            .clickable {},
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color.Gray),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onBackground
                        ),
                    ) {
                        Box(modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.clickable {
                                    onDeleteFavorite(wishlist.productId)
                                },
                                tint = MaterialTheme.colorScheme.primary,
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Delete Favorite"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    OutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp),
                        onClick = { onAddToCart() },
                        contentPadding = PaddingValues(0.dp) // Optional: tighten padding
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+ " + stringResource(id = R.string.cart),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun WishlistCardGrid(
    wishlist: Wishlist,
    onAddToCart: () -> Unit,
    onDeleteFavorite: (id: String) -> Unit
) {

    Card(modifier = Modifier.width(186.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Column {
            Card(modifier = Modifier.size(186.dp),
                shape = RoundedCornerShape(
                    topEnd = 8.dp,
                    topStart = 8.dp,
                    bottomEnd = 0.dp,
                    bottomStart = 0.dp
                )
            ) {
                Box(modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = if (wishlist.productImage.isNotEmpty()) wishlist.productImage else R.drawable.thumbnail,
                        contentDescription = "Products image",
                        contentScale = ContentScale.FillBounds,
                        // Optional: add placeholder and error images for smoother UX
                        placeholder = painterResource(id = R.drawable.thumbnail),
                        error = painterResource(id = R.drawable.thumbnail)
                    )
                }
            }

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = wishlist.productName,
                    maxLines = 2,
                    lineHeight = 15.sp,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = currency(wishlist.unitPrice),
                    fontWeight = FontWeight.W600,
                    fontSize = 14.sp,
                    fontFamily = poppins
                )

                Row(
                    Modifier.padding(top = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(12.dp),
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Account"
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = wishlist.store.toString(),
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Row(
                    Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(12.dp),
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Star"
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "${wishlist.productRating} | ${stringResource(R.string.sold)} ${wishlist.sale}",
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row {
                    Card(
                        modifier = Modifier
                            .size(32.dp)
                            .clickable {},
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color.Gray),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                        )
                    ) {
                        Box(modifier = Modifier
                            .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.clickable {
                                    onDeleteFavorite(wishlist.productId)
                                },
                                tint = MaterialTheme.colorScheme.primary,
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Delete Favorite"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    OutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp),
                        onClick = { onAddToCart() },
                        contentPadding = PaddingValues(0.dp) // Optional: tighten padding
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+ " + stringResource(id = R.string.cart),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun CartListCard(
    cart: Cart,
    onDeleteCart: () -> Unit,
    onChecked: (checked: Boolean) -> Unit,
    decreaseQuantity: () -> Unit,
    increaseQuantity: () -> Unit
) {
    Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        Row(modifier = Modifier
            .padding(top = 16.dp, start = 5.dp, end = 16.dp)
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = cart.isCheck,
                onCheckedChange = {
                    onChecked(it)
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.onBackground,
                    checkmarkColor =MaterialTheme.colorScheme.background,
                )
            )

            Card(
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = if (cart.productImage.isNotEmpty()) cart.productImage else R.drawable.thumbnail,
                        contentDescription = "Products image",
                        contentScale = ContentScale.FillBounds,
                        // Optional: add placeholder and error images for smoother UX
                        placeholder = painterResource(id = R.drawable.thumbnail),
                        error = painterResource(id = R.drawable.thumbnail)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = cart.productName,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    modifier = Modifier.padding(vertical = 4.dp),
                    text = cart.variantName,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    modifier = Modifier.padding(vertical = 4.dp),
                    text = if (cart.stock > 9) stringResource(id = R.string.stock, cart.stock) else stringResource(id = R.string.stock_few, cart.stock),
                    color = if (cart.stock > 9) MaterialTheme.colorScheme.onBackground else Color.Red,
                    style = MaterialTheme.typography.labelSmall,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = currency(cart.unitPrice),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { onDeleteCart() },
                            imageVector = Icons.TwoTone.DeleteOutline,
                            contentDescription = "Delete"
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Card(
                            modifier = Modifier
                                .height(25.dp)
                                .width(72.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.onBackground
                            ),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, Color.Gray)
                        ) {
                            Row(modifier = Modifier
                                .fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clickable {
                                            decreaseQuantity()
                                        },
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Remove"
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = cart.quantity.toString(),
                                    style = MaterialTheme.typography.labelMedium,
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Icon(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clickable { increaseQuantity() },
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CheckoutListCart(
    cart: Cart,
    decreaseQuantity: () -> Unit,
    increaseQuantity: () -> Unit,
) {
    Row(modifier = Modifier
        .padding(vertical = 8.dp)
        .background(MaterialTheme.colorScheme.background)) {
        Card(
            modifier = Modifier.size(80.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = if (cart.productImage.isNotEmpty()) cart.productImage else R.drawable.thumbnail,
                    contentDescription = "Products image",
                    contentScale = ContentScale.FillBounds,
                    // Optional: add placeholder and error images for smoother UX
                    placeholder = painterResource(id = R.drawable.thumbnail),
                    error = painterResource(id = R.drawable.thumbnail)
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = cart.productName,
                maxLines = 1,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                modifier = Modifier.padding(vertical = 4.dp),
                text = cart.variantName,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                modifier = Modifier.padding(vertical = 4.dp),
                text = if (cart.stock > 9) stringResource(id = R.string.stock, cart.stock) else stringResource(id = R.string.stock_few, cart.stock),
                color = if (cart.stock > 9) MaterialTheme.colorScheme.onBackground else Color.Red,
                style = MaterialTheme.typography.labelSmall,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = currency(cart.unitPrice),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                    horizontalArrangement = Arrangement.End
                ) {
                    Card(modifier = Modifier
                        .height(25.dp)
                        .width(72.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, Color.Gray),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor =  MaterialTheme.colorScheme.onBackground,
                        )
                    ) {
                        Row(modifier = Modifier
                            .fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clickable {
                                        decreaseQuantity()
                                    },
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Remove"
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = cart.quantity.toString(),
                                style = MaterialTheme.typography.labelMedium
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Icon(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clickable {
                                        increaseQuantity()
                                    },
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionListCard(
    transaction: Transaction,
    onNavigateToStatus: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        elevation = CardDefaults.cardElevation(3.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(
                    top = 10.dp,
                    bottom = 10.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            imageVector = Icons.Outlined.ShoppingBag,
                            contentDescription = "Shopping Bag"
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = stringResource(id = R.string.shopping),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.W600,
                                fontFamily = poppins
                            )
                            Text(
                                text = transaction.date,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.W400,
                                fontFamily = poppins
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {
                        Card(
                            modifier = Modifier.size(width = 46.dp, height = 24.dp),
                            shape = RoundedCornerShape(4.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkPurple),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(id = R.string.done),
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.W600,
                                    textAlign = TextAlign.Center,
                                    fontFamily = poppins
                                )
                            }
                        }
                    }
                }
            }
            HorizontalDivider()
            Column(
                modifier = Modifier.padding(
                    top = 10.dp,
                    bottom = 10.dp,
                    start = 16.dp,
                    end = 16.dp
                )
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Card(
                        modifier = Modifier.size(40.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Box(modifier = Modifier.size(40.dp)) {
                            AsyncImage(
                                modifier = Modifier.fillMaxSize(),
                                model = if (transaction.image.isNotEmpty()) transaction.image else R.drawable.thumbnail,
                                contentDescription = "Products image",
                                contentScale = ContentScale.FillBounds,
                                // Optional: add placeholder and error images for smoother UX
                                placeholder = painterResource(id = R.drawable.thumbnail),
                                error = painterResource(id = R.drawable.thumbnail)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = transaction.name,
                            maxLines = 1,
                            style = MaterialTheme.typography.labelLarge
                        )
                        val item = transaction.items.map { it.quantity }
                        Text(
                            modifier = Modifier.padding(vertical = 4.dp),
                            text = pluralStringResource(R.plurals.item_count, item.sum(), item.sum()),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = stringResource(id = R.string.total_spend),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.W400,
                            fontFamily = poppins
                        )
                        Text(
                            text = currency(transaction.total),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W600,
                            fontFamily = poppins
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {
                        Card(
                            modifier = Modifier
                                .alpha(
                                    if (transaction.rating == 0 && transaction.review == "") {
                                        1f
                                    } else {
                                        0f
                                    }
                                )
                                .clickable {
                                    onNavigateToStatus()
                                }
                                .height(24.dp)
                                .width(84.dp),
                            shape = RoundedCornerShape(100.dp),
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    color = Color.White,
                                    text = stringResource(id = R.string.review),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.W500,
                                    fontFamily = poppins
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun ReviewListCard(
    review: Review
) {
    Column(Modifier.fillMaxWidth()
        .padding(16.dp)
        .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(DarkPurple),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (review.userImage.isNotEmpty()) {
                    AsyncImage(
                        modifier = Modifier.size(36.dp),
                        model = review,
                        contentScale = ContentScale.FillBounds,
                        contentDescription = "User image"
                    )
                }else {
                    val initial = review.userName.firstOrNull()?.uppercase() ?: "?"
                    Text(
                        text = initial,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(text = review.userName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W600,
                    fontFamily = poppins,
                    color = MaterialTheme.colorScheme.onBackground
                )

                RatingBar(
                    modifier = Modifier.size(12.dp),
                    maxRating = 5,
                    rating = review.userRating,
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text(text = review.userReview,
            fontSize = 12.sp,
            fontWeight = FontWeight.W400,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(10.dp))
    }

    HorizontalDivider()
}

@Composable
fun PaymentListCart(
    item: Payment.PaymentItem,
    onItemClick: (payment: Payment.PaymentItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (item.status == false) Color.LightGray else MaterialTheme.colorScheme.background)
    ) {
        Card(
            modifier = if (item.status == true) {
                Modifier
                    .clickable {
                        onItemClick(item)
                    }
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            } else
                Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if(item.status == false) Color.LightGray else MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (item.image.isEmpty()) {
                            Icon(
                                imageVector = Icons.Default.AddCard,
                                contentDescription = "Card",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        } else {
                            AsyncImage(
                                modifier = Modifier.size(48.dp, 32.dp),
                                model = item.image,
                                contentDescription = "Image Item"
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            modifier = Modifier.weight(1f),
                            text = (if (item.label.isEmpty() == true) stringResource(id = R.string.choose_payment) else item.label).toString(),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.End
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = "Arrow"
                        )
                    }
                }
            }
        }
        HorizontalDivider(modifier = Modifier.padding(start = 16.dp))
    }
}

@Composable
fun NotificationListCard(
    notification: Notification,
    setNotificationRead: (id: Int, read: Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (notification.isRead) MaterialTheme.colorScheme.background
                else DarkPurple
            )
            .clickable {
                notification.id?.let { setNotificationRead(it, true) }
            }
            .padding(top = 16.dp, start = 16.dp)
    ) {
        Card(
            modifier = Modifier.size(36.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if(notification.image.isEmpty()){
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = R.drawable.thumbnail),
                        contentDescription = "Card")
                }else{
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = notification.image,
                        contentDescription = "Notification Image"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column {
            Column(modifier = Modifier.padding(end = 16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = notification.type,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (notification.isRead)
                            MaterialTheme.colorScheme.onBackground else Color.Black
                    )

                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "${notification.date}, ${notification.time}",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (notification.isRead)
                                MaterialTheme.colorScheme.onBackground else Color.Black
                        )
                    }
                }

                Text(
                    text = notification.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W600,
                    fontFamily = poppins,
                    color = if (notification.isRead)
                        MaterialTheme.colorScheme.onBackground else Color.Black
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = notification.body,
                    style = MaterialTheme.typography.bodySmall,
                    lineHeight = 20.sp,
                    color = if (notification.isRead)
                        MaterialTheme.colorScheme.onBackground else Color.Black
                )
            }
            HorizontalDivider(modifier = Modifier.padding(top = 10.dp))
        }
    }
}

@Preview("Light Mode", device = Devices.PIXEL_3)
@Composable
fun ProductCardPreview(){
    EcommerceAppTheme {
       ProductCardGrid(
           product = Product(
               "1","DELL ALIENWARE M15 R5 RYZEN 7 5800 16GB 512SSD RTX3050Ti 4GB W10 15.6F",15000000,"image","brand","Apple Store",28,5.0F
           )
       ) { }
    }
}

@Preview("Light Mode", device = Devices.PIXEL_3)
@Composable
fun TransactionCardPreview(){
    EcommerceAppTheme {
        TransactionListCard(
            transaction = Transaction(
                invoiceId = "2e77cfe0-6a5a-4a2d-9db9-9ee9ad6692b0",
                status = false,
                date= "04 Jun 2025",
                time = "12:03",
                payment = "Bank BCA",
                total = 31848000,
                items = listOf(
                    ItemTransaction(
                        productId ="685817b3-ae28-4e36-a409-e2b5448aeba1",
                        variantName= "RAM 16GB",
                        quantity= 1
                    )
                ),
                rating=  0,
                review = "",
                image = "https://images.tokopedia.net/img/cache/900/VqbcmM/2022/2/12/748b3fbf-d7c8-44f3-a8e6-35d00394974c.png",
                name = "DELL ALIENWARE M15 R5 RYZEN 7 5800 16GB 512SSD RTX3050Ti 4GB W10 15.6F - Hitam, UNIT"
            ),
            onNavigateToStatus = {}
        )
    }
}

@Preview("Light Mode", device = Devices.PIXEL_3)
@Composable
fun CardNotificationPreview() {
    val notification = Notification(
        1,"Telkomsel Award 2023",
        "Nikmati Kemeriahan ulang tahun Telkomsel pada har jumat 21 Juli 2023 pukul 19.00 - 21.00 WIB langsung dari Beach City International Stadium dengan berbagai kemudahan untuk mendapatkan aksesnya.",
        "",
        "Promo",
        "21 Jul 2023","12:34", true
    )
    EcommerceAppTheme {
        NotificationListCard(
            notification = notification,
            setNotificationRead = { id, read -> },
        )
    }
}
