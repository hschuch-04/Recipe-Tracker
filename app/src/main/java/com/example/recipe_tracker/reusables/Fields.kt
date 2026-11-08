package com.example.recipe_tracker.reusables

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType


// Reusable TextField which will be input to convert to Int
@Composable
fun NumberField(
    label: String,
    value: String,
    last: Boolean = false,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { new ->
            if (new.isEmpty() || new.matches(Regex("""\d*"""))) {
                onValueChange(new)
            }
        },
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = if (last) {
                ImeAction.Done
            } else {
                ImeAction.Next
            }
        )
    )
}


// Reusable TextField which will be input to convert to Double
@Composable
fun DecimalField(
    label: String,
    value: String,
    last: Boolean = false,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { new ->
            if (new.isEmpty() || new.matches(Regex("""\d*\.?\d*"""))) {
                onValueChange(new)
            }
        },
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = if (last) {
                ImeAction.Done
            } else {
                ImeAction.Next
            }
        )
    )
}
