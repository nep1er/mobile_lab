package ci.nsu.moble.main.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ci.nsu.moble.main.navigation.Screen
import ci.nsu.moble.main.ui.viewmodel.DepositViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ResultScreen(
    viewModel: DepositViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val result = state.result
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("ru", "RU")).apply {
        maximumFractionDigits = 2
    }
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    if (result == null) {
        LaunchedEffect(Unit) {
            navController.popBackStack(Screen.Stage2, inclusive = false)
        }
        return
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Результат расчёта",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                ResultRow("Стартовый взнос:", currencyFormat.format(result.initialAmount))
                ResultRow("Срок вклада:", "${result.periodMonths} мес.")
                ResultRow("Процентная ставка:", "${result.interestRate}%")

                if (result.monthlyTopUp != null) {
                    ResultRow("Ежемесячное пополнение:", currencyFormat.format(result.monthlyTopUp))
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                ResultRow(
                    "Итоговая сумма:",
                    currencyFormat.format(result.finalAmount),
                    isHighlight = true
                )
                ResultRow(
                    "Начисленные проценты:",
                    currencyFormat.format(result.interestEarned),
                    isHighlight = true
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp))
                ResultRow("Дата расчёта:", dateFormat.format(Date(result.calculationDate)))
            }
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
                    viewModel.saveResult()
                    viewModel.clearForm()
                    navController.popBackStack(Screen.Main, inclusive = false)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Сохранить")
            }
        }
    }
}

@Composable
private fun ResultRow(label: String, value: String, isHighlight: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (isHighlight) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = if (isHighlight) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal,
            color = if (isHighlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}