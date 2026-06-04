@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment  // ← ЭТОГО НЕ ХВАТАЛО
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import data.model.UserDto  // ← Полный путь
import ui.viewmodel.AuthViewModel

@Composable
fun MainScreen(
    viewModel: AuthViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Шапка
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Пользователи", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
            OutlinedButton(onClick = {
                viewModel.logout()
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            }) {
                Text("Выйти")
            }
        }

        Text("Добро пожаловать, ${state.currentUser?.login ?: "Пользователь"}!", modifier = Modifier.padding(vertical = 16.dp))

        // Список пользователей
        if (state.users.isEmpty()) {
            Text("Список пуст или загрузка...", modifier = Modifier.padding(top = 16.dp))
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.users) { user ->
                    UserItem(user)
                }
            }
        }
    }
}

@Composable
fun UserItem(user: UserDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = "👤 ${user.login}", fontWeight = FontWeight.Bold)
            Text(text = "📧 ${user.email ?: "-"}", style = MaterialTheme.typography.bodySmall)
            if (user.person?.firstName != null) {
                Text(text = "${user.person.firstName} ${user.person.lastName}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}