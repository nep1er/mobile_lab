package ci.nsu.moble.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import ci.nsu.moble.main.ui.theme.PracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == Screen.Main.route,
                    onClick = { navController.navigate(Screen.Main.route) },
                    icon = { Text("🏠") },
                    label = { Text("Главная") }
                )
                NavigationBarItem(
                    selected = currentRoute == Screen.SecondNav.route,
                    onClick = { navController.navigate(Screen.SecondNav.route) },
                    icon = { Text("\uD83C\uDFAC") },
                    label = { Text("Экран 2") }
                )
                NavigationBarItem(
                    selected = currentRoute == Screen.Profile.route,
                    onClick = { navController.navigate(Screen.Profile.route) },
                    icon = { Text("👤") },
                    label = { Text("Профиль") }
                )
                NavigationBarItem(
                    selected = currentRoute == Screen.Settings.route,
                    onClick = { navController.navigate(Screen.Settings.route) },
                    icon = { Text("⚙️") },
                    label = { Text("Настройки") }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Main.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Main.route) {
                MainScreen(
                    onOpenSecondViaIntent = { text ->
                        val intent = Intent(context, MainActivity2::class.java).apply {
                            putExtra("KEY_TEXT", text)
                        }
                        context.startActivity(intent)
                    }
                )
            }
            composable(Screen.SecondNav.route) {
                SimpleContentScreen(title = "Экран через NavHost")
            }
            composable(Screen.Profile.route) {
                SimpleContentScreen(title = "Профиль")
            }
            composable(Screen.Settings.route) {
                SimpleContentScreen(title = "Настройки")
            }
        }
    }
}

@Composable
fun MainScreen(onOpenSecondViaIntent: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Главный экран", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(20.dp))

        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Текст для передачи") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { onOpenSecondViaIntent(text) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Открыть второй экран (Intent)")
        }
    }
}

@Composable
fun SimpleContentScreen(title: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(title, style = MaterialTheme.typography.headlineMedium)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMain() = PracticeTheme { MainScreen {} }