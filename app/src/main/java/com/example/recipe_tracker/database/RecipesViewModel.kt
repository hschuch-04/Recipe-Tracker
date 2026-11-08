package com.example.recipe_tracker.database

import android.app.Application
import androidx.compose.ui.input.key.Key.Companion.I
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RecipesViewModel (app: Application ) : AndroidViewModel(app) {
    private val db = AppDatabase.get(app)
    private val recipeDao = db.recipeDao()

    val recipes: StateFlow<List<RecipeWithIngredients>> =
        recipeDao.viewAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // Ingredients that the user can pick from
    val availableIngredients: StateFlow<List<IngredientEntity>> =
        db.ingredientDao().viewAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000),  emptyList())


    fun addRecipe(name: String, instructions: String, ingredientIds: List<Long>) {
        viewModelScope.launch {
            recipeDao.insertRecipeWithIngredients(
                RecipeEntity(name = name, instructions = instructions),
                ingredientIds
            )
        }
    }

    fun deleteRecipe(recipe: RecipeEntity) {
        viewModelScope.launch {  recipeDao.deleteRecipe(recipe) }
    }
}