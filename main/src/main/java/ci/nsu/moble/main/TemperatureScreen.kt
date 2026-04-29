package ci.nsu.moble.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.text.KeyboardOptions
@Composable
fun TemperatureScreen(
    modifier: Modifier = Modifier,
    viewModel: TemperatureViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(100.dp),
            verticalArrangement = Arrangement.spacedBy(80.dp)
        ) {
            OutlinedTextField(
                value = uiState.celsius,
                onValueChange = { viewModel.onCelsiusChanged(it) },
                label = { Text("°C") },
                placeholder = { Text("Введите температуру") },
                isError = !uiState.isCelsiusValid,
                supportingText = {
                    if (!uiState.isCelsiusValid && uiState.celsius.isNotBlank()) {
                        Text("Некорректное число")
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()

            )

            OutlinedTextField(
                value = uiState.fahrenheit,
                onValueChange = { viewModel.onFahrenheitChanged(it) },
                label = { Text("°F") },
                placeholder = { Text("Введите температуру") },
                isError = !uiState.isFahrenheitValid,
                supportingText = {
                    if (!uiState.isFahrenheitValid && uiState.fahrenheit.isNotBlank()) {
                        Text("Некорректное число")
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}