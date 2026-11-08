package com.example.recipe_tracker.ingredients

import android.widget.Toast
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipe_tracker.R
import com.example.recipe_tracker.database.IngredientsViewModel
import com.example.recipe_tracker.reusables.DecimalField
import com.example.recipe_tracker.reusables.NumberField


// Main Ingredients Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientsPage(
    viewModel: IngredientsViewModel = viewModel(),
    bottomBar: @Composable () -> Unit
) {
    val ingredients by viewModel.ingredients.collectAsStateWithLifecycle()
    val toast = Toast.makeText(LocalContext.current, "Ingredient has been removed.", Toast.LENGTH_SHORT)
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Ingredients") }
            )
        },
        bottomBar = bottomBar,
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
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        )
        {
            items(ingredients, key = { it.id }) { item ->
                ListItem(
                    modifier = Modifier.combinedClickable(
                        onClick = {},
                        onLongClick = {
                            viewModel.deleteIngredient(item)
                            toast.show()
                        }
                    ),
                    headlineContent = { Text(item.name) },
                    supportingContent = {
                        Text("${item.grams} g  |  CAL ${item.calories}  P ${item.protein}  F ${item.fat}  C ${item.carbs}")
                    }
                )
            }
        }
    }

    // Creates Add Ingredient Dialog window
    if (showDialog) {
        AddIngredientDialog(
            onDismiss = {showDialog = false },
            onConfirm = { name, grams, calories, protein, fat, carbs ->
                viewModel.addIngredient(name, grams, calories, protein, fat, carbs)
                showDialog = false
            }
        )
    }

}


// Create an Alert Dialog Box with all the inputs required for an Ingredient Class
@Composable
fun AddIngredientDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, grams: Int, calories: Int, protein: Double, fat: Double, carbs: Double) -> Unit
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
                        name.trim(),
                        grams.toInt(),
                        calories.toInt(),
                        grams.toDouble(),
                        fat.toDouble(),
                        carbs.toDouble()
                        )
                }
            ) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }

    )
}