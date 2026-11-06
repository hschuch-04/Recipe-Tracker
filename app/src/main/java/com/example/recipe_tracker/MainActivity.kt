package com.example.recipe_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.recipe_tracker.ingredients.IngredientsPage
import com.example.recipe_tracker.ui.theme.RecipeTrackerTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipeTrackerTheme {
                MainPage()
            }
        }
    }
}

@Composable
fun MainPage(modifier: Modifier = Modifier) {
    IngredientsPage()
}
