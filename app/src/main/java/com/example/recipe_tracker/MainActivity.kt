package com.example.recipe_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.recipe_tracker.database.IngredientsViewModel
import com.example.recipe_tracker.database.RecipesViewModel
import com.example.recipe_tracker.ingredients.IngredientsPage
import com.example.recipe_tracker.recipes.AddRecipeScreen
import com.example.recipe_tracker.recipes.RecipesPage
import com.example.recipe_tracker.ui.theme.RecipeTrackerTheme

class MainActivity : ComponentActivity() {
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
fun MainPage() {
    AppNav()
}

// The routes available for navigation
object Routes {
    const val RECIPES = "recipes"
    const val ADD_RECIPE = "add_recipe"
    const val INGREDIENTS = "ingredients"
    const val ADD_INGREDIENT = "add_ingredient"
}

@Composable
fun AppNav() {
    val navController = rememberNavController()
    val ingredientsVM: IngredientsViewModel = viewModel()
    val recipesVM: RecipesViewModel = viewModel()

    val bottomBar: @Composable () -> Unit = {
        val current = navController.currentBackStackEntryAsState().value?.destination?.route
        NavigationBar {
            NavigationBarItem(
                selected = current == Routes.RECIPES,
                onClick = { navController.navigateTopLevel(Routes.RECIPES) },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_list),
                        contentDescription = null
                    )
                },
                label = { Text("Recipes") }
            )
            NavigationBarItem(
                selected = current == Routes.INGREDIENTS,
                onClick = { navController.navigateTopLevel(Routes.INGREDIENTS) },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_list),
                        contentDescription = null
                    )
                },
                label = { Text("Ingredients") }
            )
        }
    }

    NavHost(navController, startDestination = Routes.RECIPES) {
        composable(Routes.INGREDIENTS) {
            IngredientsPage(
                viewModel = ingredientsVM,
                bottomBar = bottomBar
            )
        }
        composable(Routes.RECIPES) {
            RecipesPage(
                viewModel = recipesVM,
                onAddClick = { navController.navigate(Routes.ADD_RECIPE) },
                bottomBar = bottomBar
            )
        }
        composable(Routes.ADD_RECIPE) {
            val available by recipesVM.availableIngredients.collectAsStateWithLifecycle()
            AddRecipeScreen(
                availableIngredients = available,
                onBack = { navController.popBackStack() },
                onSave = { name, instructions, ingredientIds ->
                    recipesVM.addRecipe(name, instructions, ingredientIds)
                    navController.popBackStack()
                }
            )
        }
    }
}

// Function to make navigating back to top layers easy and keeps the state and doesn't reset
private fun NavController.navigateTopLevel(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
