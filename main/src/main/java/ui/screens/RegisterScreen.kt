@file:OptIn(
    ExperimentalMaterial3Api::class,
    kotlinx.serialization.InternalSerializationApi::class
)
package ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ui.viewmodel.AuthViewModel
import data.model.GroupDto


@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsState()

    // После успешной регистрации → возврат на экран входа
    LaunchedEffect(state.successMessage) {
        state.successMessage?.let {
            navController.popBackStack()
            viewModel.clearSuccess()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Регистрация", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
            item { OutlinedTextField(value = state.lastName, onValueChange = { viewModel.setLastName(it) }, label = { Text("Фамилия") }, modifier = Modifier.fillMaxWidth(), singleLine = true) }
            item { OutlinedTextField(value = state.firstName, onValueChange = { viewModel.setFirstName(it) }, label = { Text("Имя") }, modifier = Modifier.fillMaxWidth(), singleLine = true) }
            item { OutlinedTextField(value = state.middleName, onValueChange = { viewModel.setMiddleName(it) }, label = { Text("Отчество") }, modifier = Modifier.fillMaxWidth(), singleLine = true) }
            item { OutlinedTextField(value = state.birthDate, onValueChange = { viewModel.setBirthDate(it) }, label = { Text("Дата (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth(), singleLine = true) }

            item {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Пол:", style = MaterialTheme.typography.bodyLarge)
                    Row {
                        RadioButton(selected = state.gender == "MALE", onClick = { viewModel.setGender("MALE") })
                        Text("М", modifier = Modifier.padding(end = 8.dp))
                        RadioButton(selected = state.gender == "FEMALE", onClick = { viewModel.setGender("FEMALE") })
                        Text("Ж")
                    }
                }
            }

            // Простой список групп (не dropdown)
            item {
                if (state.groups.isEmpty()) {
                    OutlinedTextField(
                        value = "Загрузка групп...",
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Группа") },
                        enabled = false
                    )
                } else {
                    var expanded by remember { mutableStateOf(false) }

                    // Находим имя выбранной группы
                    val selectedGroupName = state.selectedGroupId?.let { id ->
                        state.groups.find { g -> g.groupId == id }?.groupName
                    }

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedGroupName ?: "Выберите группу",
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),  // ← Обязательно для dropdown
                            label = { Text("Группа") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            colors = ExposedDropdownMenuDefaults.textFieldColors()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            state.groups.forEach { group ->
                                DropdownMenuItem(
                                    text = { Text(group.groupName ?: "Группа #${group.groupId}") },
                                    onClick = {
                                        viewModel.setSelectedGroupId(group.groupId)
                                        expanded = false  // ← Закрываем меню после выбора
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                    }
                }
            }

            item { OutlinedTextField(value = state.email, onValueChange = { viewModel.setEmail(it) }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), singleLine = true) }
            item { OutlinedTextField(value = state.login, onValueChange = { viewModel.setLogin(it) }, label = { Text("Логин") }, modifier = Modifier.fillMaxWidth(), singleLine = true) }
            item { OutlinedTextField(value = state.password, onValueChange = { viewModel.setPassword(it) }, label = { Text("Пароль") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth(), singleLine = true) }
            item { OutlinedTextField(value = state.phoneNumber, onValueChange = { viewModel.setPhoneNumber(it) }, label = { Text("Телефон") }, modifier = Modifier.fillMaxWidth(), singleLine = true) }
        }

        Spacer(modifier = Modifier.height(16.dp))
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 8.dp)) }

        Button(
            onClick = { viewModel.register() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading && state.isRegisterValid
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Зарегистрироваться")
            }
        }
    }
}