package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import data.repository.AuthRepository
import ui.screens.LoginScreen
import ui.screens.MainScreen
import ui.screens.RegisterScreen
import ui.viewmodel.AuthViewModel
import ui.viewmodel.AuthViewModelFactory
import utils.TokenManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация TokenManager
        TokenManager.init(applicationContext)

        // Создание зависимостей
        val repository = AuthRepository()
        val factory = AuthViewModelFactory(repository)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(factory)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(factory: AuthViewModelFactory) {
    val navController = rememberNavController()
    val viewModel: AuthViewModel = viewModel(factory = factory)

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(viewModel = viewModel, navController = navController)
        }
        composable("register") {
            RegisterScreen(viewModel = viewModel, navController = navController)
        }
        composable("main") {
            MainScreen(viewModel = viewModel, navController = navController)
        }
    }
}