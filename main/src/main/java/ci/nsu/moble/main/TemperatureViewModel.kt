package ci.nsu.moble.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale

data class TemperatureUiState(
    val celsius: String = "",
    val fahrenheit: String = ""
) {
    val isCelsiusValid: Boolean
        get() = celsius.isBlank() || celsius.toDoubleOrNull() != null

    val isFahrenheitValid: Boolean
        get() = fahrenheit.isBlank() || fahrenheit.toDoubleOrNull() != null
}

class TemperatureViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TemperatureUiState())
    val uiState: StateFlow<TemperatureUiState> = _uiState.asStateFlow()

    fun onCelsiusChanged(value: String) {
        _uiState.update { state ->
            val fahrenheit = if (value.isNotBlank()) {
                value.toDoubleOrNull()?.let { c ->
                    String.format(Locale.US, "%.2f", c * 9.0 / 5.0 + 32.0)
                } ?: ""
            } else ""
            state.copy(celsius = value, fahrenheit = fahrenheit)
        }
    }

    fun onFahrenheitChanged(value: String) {
        _uiState.update { state ->546
            val celsius = if (value.isNotBlank()) {
                value.toDoubleOrNull()?.let { f ->
                    String.format(Locale.US, "%.2f", (f - 32.0) * 5.0 / 9.0)
                } ?: ""
            } else ""
            state.copy(celsius = celsius, fahrenheit = value)
        }
    }
}