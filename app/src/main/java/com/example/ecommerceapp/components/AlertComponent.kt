package com.example.ecommerceapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerceapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicAlert(
    openDialog: Boolean,
    onDismiss: () -> Unit,
    onLaunchCamera: () -> Unit,
    onLaunchGallery: () -> Unit
) {
    if (openDialog) {
        BasicAlertDialog(onDismissRequest = { onDismiss() }) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(8.dp)
            ) {
                Text(
                    modifier = Modifier.padding(
                        top = 16.dp,
                        bottom = 16.dp,
                        start = 12.dp
                    ),
                    fontSize = 24.sp,
                    text = stringResource(id = R.string.choose_picture),
                    fontWeight = FontWeight.W400,
                )

                TextButton(
                    onClick = {
                        onLaunchCamera()
                    }
                ) {
                    Text(
                        stringResource(id = R.string.camera),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W400,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                TextButton(
                    onClick = {
                        onLaunchGallery()
                    }
                ) {
                    Text(
                        stringResource(id = R.string.gallery),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W400,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}
