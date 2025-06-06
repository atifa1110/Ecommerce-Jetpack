package com.example.ecommerceapp.components

import android.util.Patterns
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ecommerceapp.R

@Composable
fun EmailComponent(
    email: String,
    emailError: String,
    onEmailChanged: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(
                text = stringResource(id = R.string.email),
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        singleLine = true,
        maxLines = 1,
        value = email,
        onValueChange = {
            onEmailChanged(it)
        },
        isError = emailError.isNotEmpty(),
    )

    if (emailError.isNotEmpty()) {
        TextFieldError(
            textError = R.string.email_invalid,
            color = MaterialTheme.colorScheme.error
        )
    } else {
        TextFieldCorrect(
            textError = if (email.isEmpty()) "Contoh: test@gmail.com" else "Contoh: $email",
            color = Color.Gray
        )
    }
}

@Composable
fun TextFieldError(textError: Int, color: Color) {
    Text(
        modifier = Modifier
            .padding(top = 2.dp, start = 16.dp)
            .fillMaxWidth(),
        text = stringResource(id = textError),
        color = color,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
fun TextFieldCorrect(textError: String, color: Color) {
    Text(modifier = Modifier
        .padding(top = 2.dp, start = 16.dp)
        .fillMaxWidth(),
        text = textError,
        color = color,
        style = MaterialTheme.typography.bodySmall
    )
}
