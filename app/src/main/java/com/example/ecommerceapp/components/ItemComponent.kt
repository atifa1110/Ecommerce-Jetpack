package com.example.ecommerceapp.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.ecommerceapp.data.ui.Cart
import com.example.ecommerceapp.data.ui.ItemTransaction
import com.example.ecommerceapp.data.ui.Product
import com.example.ecommerceapp.data.ui.Review
import com.example.ecommerceapp.data.ui.Transaction
import com.example.ecommerceapp.data.ui.Wishlist
import com.example.ecommerceapp.screen.status.RatingBar
import com.example.ecommerceapp.ui.theme.DarkPurple
import com.example.ecommerceapp.ui.theme.EcommerceAppTheme
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
                fontSize = 12.sp,
                fontWeight = FontWeight.W400
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
    Column(Modifier.padding(top = 5.dp, bottom = 5.dp, end = 5.dp)) {
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
                        fontWeight = FontWeight.W400,
                        fontSize = 12.sp,
                        maxLines = 2,
                        lineHeight = 15.sp
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = currency(product.productPrice),
                        fontWeight = FontWeight.W600,
                        fontSize = 14.sp
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
                            product.store,
                            fontWeight = FontWeight.W400,
                            fontSize = 10.sp
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
                            "${product.productRating} | ${stringResource(R.string.sold)} ${product.sale}",
                            fontWeight = FontWeight.W400,
                            fontSize = 10.sp
                        )
                    }
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
    Column(modifier = Modifier.padding(vertical = 5.dp)) {
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
                            fontWeight = FontWeight.W400,
                            fontSize = 12.sp,
                            lineHeight = 15.sp
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = currency(product.productPrice),
                            fontWeight = FontWeight.W600,
                            fontSize = 14.sp
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
                                product.store,
                                fontWeight = FontWeight.W400,
                                fontSize = 10.sp
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
                                "${product.productRating} | ${stringResource(R.string.sold)} ${product.sale}",
                                fontWeight = FontWeight.W400,
                                fontSize = 10.sp
                            )
                        }
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
    Column(
        Modifier.padding(vertical = 5.dp)
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
                                fontWeight = FontWeight.W400,
                                fontSize = 12.sp,
                                lineHeight = 15.sp
                            )
                            Spacer(modifier = Modifier.height(5.dp))

                            Text(
                                text = currency(wishlist.unitPrice),
                                fontWeight = FontWeight.W600,
                                fontSize = 14.sp
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
                                    fontWeight = FontWeight.W400,
                                    fontSize = 10.sp
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
                                    "${wishlist.productRating} | ${stringResource(R.string.sold)} ${wishlist.sale}",
                                    fontWeight = FontWeight.W400,
                                    fontSize = 10.sp
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
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.W500,
                                    color = MaterialTheme.colorScheme.primary
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
fun WishlistCardGrid(
    wishlist: Wishlist,
    onAddToCart: () -> Unit,
    onDeleteFavorite: (id: String) -> Unit
) {
    Column(Modifier.padding(top = 5.dp, bottom = 5.dp, end = 5.dp)) {
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
                        fontWeight = FontWeight.W400,
                        fontSize = 12.sp,
                        maxLines = 2,
                        lineHeight = 15.sp
                    )
                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = currency(wishlist.unitPrice),
                        fontWeight = FontWeight.W600,
                        fontSize = 14.sp
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
                            fontWeight = FontWeight.W400,
                            fontSize = 10.sp
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
                            "${wishlist.productRating} | ${stringResource(R.string.sold)} ${wishlist.sale}",
                            fontWeight = FontWeight.W400,
                            fontSize = 10.sp
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
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.W500,
                                    color = MaterialTheme.colorScheme.primary
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
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = cart.variantName,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.W400,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = if (cart.stock > 9) stringResource(id = R.string.stock, cart.stock) else stringResource(id = R.string.stock_few, cart.stock),
                    color = if (cart.stock > 9) MaterialTheme.colorScheme.onBackground else Color.Red,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.W400,
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
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
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
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.W500
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
                fontSize = 14.sp,
                fontWeight = FontWeight.W500,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = cart.variantName,
                fontSize = 10.sp,
                fontWeight = FontWeight.W400,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = if (cart.stock > 9) stringResource(id = R.string.stock, cart.stock) else stringResource(id = R.string.stock_few, cart.stock),
                color = if (cart.stock > 9) MaterialTheme.colorScheme.onBackground else Color.Red,
                fontSize = 10.sp,
                fontWeight = FontWeight.W400
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
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
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
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W500
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
                                fontWeight = FontWeight.W600
                            )
                            Text(
                                text = transaction.date,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.W400
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
                                    textAlign = TextAlign.Center
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
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500
                        )
                        val item: List<Int> = transaction.items.map { it.quantity }
                        Text(
                            text = pluralStringResource(R.plurals.item_count, item.sum(), item.sum()),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.W400
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
                            fontWeight = FontWeight.W400
                        )
                        Text(
                            text = currency(transaction.total),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W600
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
                                    fontWeight = FontWeight.W500
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
                }else{
                    val initial = review.userName.firstOrNull()?.uppercase() ?: "?"
                    Text(
                        text = initial,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(text = review.userName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W600,
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

@Preview("Light Mode", device = Devices.PIXEL_3)
@Composable
fun TransactionCardPreview(){
    EcommerceAppTheme {
        TransactionListCard(
            transaction = Transaction(
                invoiceId = "2e77cfe0-6a5a-4a2d-9db9-9ee9ad6692b0",
                status = true,
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