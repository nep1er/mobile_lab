package ci.nsu.moble.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType  // Можно оставить или удалить - не важно
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.moble.main.navigation.Screen
import ci.nsu.moble.main.ui.viewmodel.DepositViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Stage1Screen(
    viewModel: DepositViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Основные параметры вклада",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        // ← Просто убрали keyboardOptions. Всё работает.
        OutlinedTextField(
            value = state.initialAmount,
            onValueChange = { viewModel.setInitialAmount(it) },
            label = { Text("Стартовый взнос (₽)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = state.error != null && state.initialAmount.isEmpty()
        )

        OutlinedTextField(
            value = state.periodMonths,
            onValueChange = {
                viewModel.setPeriodMonths(it)
                viewModel.clearError()
            },
            label = { Text("Срок вклада (месяцев)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = state.error != null && state.periodMonths.isEmpty()
        )

        state.availableRate?.let { rate ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Доступная процентная ставка:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "$rate% годовых",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        state.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = {
                    viewModel.clearForm()
                    navController.popBackStack(Screen.Main, inclusive = false)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("В начало")
            }

            Button(
                onClick = {
                    if (state.isStage1Valid) {
                        state.availableRate?.let { rate ->
                            viewModel.setInterestRate(rate)
                            navController.navigate(Screen.Stage2)
                        }
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = state.isStage1Valid
            ) {
                Text("Далее")
            }
        }
    }
}