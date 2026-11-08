package com.example.recipe_tracker.recipes

import android.R.attr.contentDescription
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.recipe_tracker.R
import com.example.recipe_tracker.database.IngredientEntity
import com.example.recipe_tracker.database.RecipesViewModel


// The Main Recipe Screen page
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipesPage(
    viewModel: RecipesViewModel,
    onAddClick: () -> Unit,
    bottomBar: @Composable () -> Unit
) {

    val recipes by viewModel.recipes.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Recipes") }
            )
        },
        bottomBar = bottomBar,
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_add),
                    contentDescription = "Add Recipe"
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(recipes, key = { it.recipe.id }) { item ->
                Card(Modifier.fillMaxWidth()) {
                    Column(
                        Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            item.recipe.name,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            item.ingredients.joinToString { it.name },
                            style = MaterialTheme.typography.bodySmall
                        )
                        if (item.recipe.instructions.isNotBlank()) {
                            Text(
                                item.recipe.instructions,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
            }
        }
    }
}


// The add a recipe screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(
    availableIngredients: List<IngredientEntity>,
    onBack: () -> Unit,
    onSave: (name: String, instructions: String, ingredientIds: List<Long>) -> Unit
)
{
    var name by rememberSaveable() { mutableStateOf("") }
    var instructions by rememberSaveable { mutableStateOf("")}
    val selectedIds = remember { mutableStateListOf<Long>() }

    // Only Check Name and Ingredients are mandatory
    val isValid = name.isNotBlank() && selectedIds.isNotEmpty()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add Recipe") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_back),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Recipe Name") },
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Text(
                    "Ingredients",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (availableIngredients.isEmpty()) {
                item {
                    Text("No ingredients have been made available yet.")
                }
            }

            items(availableIngredients, key = { it.id }) { ingredient ->
                val checked = ingredient.id in selectedIds
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .toggleable(
                            value = checked,
                            role = Role.Checkbox,
                            onValueChange = {
                                if (it) selectedIds.add(ingredient.id) else selectedIds.remove(ingredient.id)
                            }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox( checked = checked, onCheckedChange = null )
                    Spacer(Modifier.width(12.dp))
                    Text(ingredient.name)
                }
            }

            item {
                OutlinedTextField(
                    value = instructions,
                    onValueChange = { instructions = it },
                    label = { Text("Instructions") },
                    minLines = 6,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
            }

            item {
                Button(
                    onClick = { onSave(name.trim(), instructions.trim(), selectedIds.toList())},
                    enabled = isValid,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Recipe")
                }
            }
        }
    }
}


