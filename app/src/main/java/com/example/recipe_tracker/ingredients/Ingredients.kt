package com.example.recipe_tracker.ingredients

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.recipe_tracker.R


// Data Class for Ingredients
data class Ingredient(
    val name: String,
    val grams: Int,
    val calories: Int,
    val protein: Double,
    val fat: Double,
    val carbs: Double
)

// Main Ingredients Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientsPage() {
    var showDialog by remember { mutableStateOf(false) }
    val ingredients = remember { mutableStateListOf<Ingredient>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ingredients") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_add),
                    contentDescription = "Add Ingredient"
                )
            }
        }
    )
    { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        )
        {
            ingredients.forEach {
                Text("${it.name} - ${it.grams}g (P ${it.protein} / F ${it.fat} / C ${it.carbs}")
            }
        }
    }

    // Creates Add Ingredient Dialog window
    if (showDialog) {
        AddIngredientDialog(
            onDismiss = {showDialog = false },
            onConfirm = { ingredient ->
                ingredients.add(ingredient)
                showDialog = false
            }
        )
    }
}


// Create an Alert Dialog Box with all the inputs required for an Ingredient Class
@Composable
fun AddIngredientDialog(
    onDismiss: () -> Unit,
    onConfirm: (Ingredient) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var grams by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }

    // Boolean to check inputs are valid
    val isValid = name.isNotBlank() &&
            grams.toIntOrNull() != null &&
            calories.toIntOrNull() != null &&
            protein.toDoubleOrNull() != null &&
            fat.toDoubleOrNull() != null &&
            carbs.toDoubleOrNull() != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Ingredient") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Ingredient Name") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
                NumberField("Grams", grams) { grams = it }
                NumberField("Calories", calories) { calories = it }
                DecimalField("Protein (g)", protein) { protein = it }
                DecimalField("Fat (g)", fat) { fat = it }
                DecimalField("Carbohydrates (g)", carbs, last = true) { carbs = it }
            }
        },
        confirmButton = {
            TextButton(
                enabled = isValid,
                onClick = {
                    onConfirm(
                        Ingredient(
                            name = name.trim(),
                            grams = grams.toInt(),
                            calories = calories.toInt(),
                            protein = grams.toDouble(),
                            fat = fat.toDouble(),
                            carbs = carbs.toDouble()
                        )
                    )
                }
            ) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }

    )
}


// Reusable TextField which will be input to convert to Int
@Composable
private fun NumberField(
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
private fun DecimalField(
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