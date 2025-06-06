package com.example.ecommerceapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.ecommerceapp.R

@Composable
fun ButtonComponent(
    onClick : () -> Unit,
    enable : Boolean,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    buttonText: Int = R.string.login
){
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        enabled = enable
    ) {
        Text(
            text = stringResource(id = buttonText),
            fontWeight = FontWeight.W500,
        )
    }

}

@Composable
fun OutlinedButtonComponent(
    onClick: () -> Unit,
    contentColor: Color = MaterialTheme.colorScheme.primary,
    buttonText: Int = R.string.register
){
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = contentColor
        )
    ) {
        Text(
            text = stringResource(id = buttonText),
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Composable
fun DividerButton(tryingToLogin: Boolean = true) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Start
        ) {
            HorizontalDivider()
        }

        Text(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = if (tryingToLogin) {
                stringResource(id = R.string.register_with)
            } else {
                stringResource(id = R.string.login_with)
            },
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            fontWeight = FontWeight.Normal
        )

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.End
        ) {
            HorizontalDivider()
        }
    }
}

@Composable
fun TextTermCondition(tryingToLogin: Boolean = true) {
    val initialText =
        if (tryingToLogin) {
            stringResource(id = R.string.by_entering_login) + " "
        } else stringResource(
            id = R.string.by_entering_register
        ) + " "
    val terms = stringResource(id = R.string.term_condition) + " "
    val and = stringResource(id = R.string.and) + " "
    val policy = stringResource(id = R.string.privacy_policy) + " "
    val store = stringResource(id = R.string.toko_phincon)

    val annotatedString = buildAnnotatedString {
        append(initialText)
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            pushStringAnnotation(tag = terms, annotation = terms)
            append(terms)
        }
        append(and)
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            pushStringAnnotation(tag = terms, annotation = terms)
            append(policy)
        }
        append(store)
    }

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        style = TextStyle(
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            fontWeight = FontWeight.Normal
        ),
        text = annotatedString
    )
}
