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
import ci.nsu.moble.main.data.database.AppDatabase
import ci.nsu.moble.main.data.repository.DepositRepository
import ci.nsu.moble.main.domain.usecase.CalculateDepositUseCase
import ci.nsu.moble.main.navigation.Screen
import ci.nsu.moble.main.ui.screens.*
import ci.nsu.moble.main.ui.viewmodel.DepositViewModel
import ci.nsu.moble.main.ui.viewmodel.DepositViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация зависимостей
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = DepositRepository(database.depositDao())
        val calculateUseCase = CalculateDepositUseCase()
        val viewModelFactory = DepositViewModelFactory(repository, calculateUseCase)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DepositApp(viewModelFactory)
                }
            }
        }
    }
}

@Composable
fun DepositApp(viewModelFactory: DepositViewModelFactory) {
    val navController = rememberNavController()
    val viewModel: DepositViewModel = viewModel(factory = viewModelFactory)

    NavHost(
        navController = navController,
        startDestination = Screen.Main
    ) {
        composable<Screen.Main> {
            MainScreen(navController = navController)
        }

        composable<Screen.Stage1> {
            Stage1Screen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable<Screen.Stage2> {
            Stage2Screen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable<Screen.Result> {
            ResultScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable<Screen.History> {
            HistoryScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}