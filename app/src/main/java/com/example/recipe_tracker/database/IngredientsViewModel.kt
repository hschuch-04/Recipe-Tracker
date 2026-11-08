package com.example.recipe_tracker.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class IngredientsViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = AppDatabase.get(app).ingredientDao()

    val ingredients: StateFlow<List<IngredientEntity>> =
        dao.viewAll().stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // Adds the Ingredients to the table
    fun addIngredient(
        name: String,
        grams: Int,
        calories: Int,
        protein: Double,
        fat: Double,
        carbs: Double
    ) {
        viewModelScope.launch {
            dao.insert(
                IngredientEntity(
                    name = name,
                    grams = grams,
                    calories = calories,
                    protein = protein,
                    fat = fat,
                    carbs = carbs
                )
            )
        }
    }

    // Deletes an Ingredient from the Table
    fun deleteIngredient(ingredient: IngredientEntity) {
        viewModelScope.launch { dao.delete(ingredient) }
    }
}



