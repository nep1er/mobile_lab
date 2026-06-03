@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package ui.screens

import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import data.model.UserDto
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

        Text("Добро пожаловать, ${state.currentUser?.login ?: "Пользователь"}!", modifier = Modifier.padding(bottom = 16.dp))

        // Список пользователей
        if (state.users.isEmpty()) {
            Text("Список пуст или не загружен...", modifier = Modifier.padding(top = 16.dp))
        } else {
            LazyColumn {
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
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = "👤 ${user.person?.firstName} ${user.person?.lastName}", fontWeight = FontWeight.Bold)
            Text(text = "📧 ${user.email ?: "-"}", style = MaterialTheme.typography.bodySmall)
            Text(text = " Логин: ${user.login}", style = MaterialTheme.typography.bodySmall)
            if (user.person?.birthDate != null) {
                Text(text = " Рождение: ${user.person.birthDate}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}