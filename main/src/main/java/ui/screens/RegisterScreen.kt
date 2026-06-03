@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    // Заголовок
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Регистрация", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // Скроллируемый список полей
        androidx.compose.foundation.lazy.LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                OutlinedTextField(value = state.lastName, onValueChange = { viewModel.setLastName(it) }, label = { Text("Фамилия") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            }
            item {
                OutlinedTextField(value = state.firstName, onValueChange = { viewModel.setFirstName(it) }, label = { Text("Имя") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            }
            item {
                OutlinedTextField(value = state.middleName, onValueChange = { viewModel.setMiddleName(it) }, label = { Text("Отчество (если есть)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            }
            item {
                OutlinedTextField(value = state.birthDate, onValueChange = { viewModel.setBirthDate(it) }, label = { Text("Дата рождения (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            }

            // Пол (Radio Buttons)
            item {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Пол:", style = MaterialTheme.typography.bodyLarge)
                    Row {
                        RadioButton(selected = state.gender == "MALE", onClick = { viewModel.setGender("MALE") })
                        Text("Мужской", modifier = Modifier.padding(end = 8.dp))
                        RadioButton(selected = state.gender == "FEMALE", onClick = { viewModel.setGender("FEMALE") })
                        Text("Женский")
                    }
                }
            }

            // Выбор группы (Dropdown)
            item {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = state.selectedGroupId?.let { id -> state.groups.find { g -> g.id == id }?.name } ?: "Выберите группу",
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        label = { Text("Группа") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        state.groups.forEach { group ->
                            DropdownMenuItem(
                                text = { Text(group.name) },
                                onClick = {
                                    viewModel.setSelectedGroupId(group.id)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            item {
                OutlinedTextField(value = state.email, onValueChange = { viewModel.setEmail(it) }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            }
            item {
                OutlinedTextField(value = state.login, onValueChange = { viewModel.setLogin(it) }, label = { Text("Логин для входа") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            }
            item {
                OutlinedTextField(value = state.password, onValueChange = { viewModel.setPassword(it) }, label = { Text("Пароль") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth(), singleLine = true)
            }
            item {
                OutlinedTextField(value = state.phoneNumber, onValueChange = { viewModel.setPhoneNumber(it) }, label = { Text("Телефон (необяз.)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        state.error?.let {
            Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 8.dp))
        }

        Button(
            onClick = {
                viewModel.register()
                if (viewModel.state.value.successMessage != null) {
                    navController.popBackStack() // Возврат к входу
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Зарегистрироваться")
            }
        }
    }
}