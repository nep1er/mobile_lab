package ci.nsu.moble.main.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.moble.main.data.repository.DepositRepository
import ci.nsu.moble.main.domain.usecase.CalculateDepositUseCase
import ci.nsu.moble.main.ui.state.DepositUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DepositViewModel(
    private val repository: DepositRepository,
    private val calculate: CalculateDepositUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DepositUiState())
    val state: StateFlow<DepositUiState> = _state.asStateFlow()

    init { loadHistory() }

    fun setInitialAmount(value: String) = _state.update { it.copy(initialAmount = value) }
    fun setPeriodMonths(value: String) = _state.update { it.copy(periodMonths = value) }
    fun setMonthlyTopUp(value: String) = _state.update { it.copy(monthlyTopUp = value) }
    fun setInterestRate(value: Double) = _state.update { it.copy(interestRate = value) }

    fun calculate() {
        viewModelScope.launch {
            val init = _state.value.initialAmount.toDoubleOrNull()
            val months = _state.value.periodMonths.toIntOrNull()
            val rate = _state.value.interestRate
            val topUp = _state.value.monthlyTopUp.toDoubleOrNull()
                .takeIf { _state.value.monthlyTopUp.isNotBlank() }

            if (init != null && months != null && rate != null && init > 0 && months > 0) {
                val result = calculate(init, months, rate, topUp)
                _state.update { it.copy(result = result, error = null) }
            } else {
                _state.update { it.copy(error = "Заполните все поля корректно") }
            }
        }
    }

    fun saveResult() {
        viewModelScope.launch {
            _state.value.result?.let { repository.insert(it) }
        }
    }

    private fun loadHistory() {
        viewModelScope.launch {
            repository.allDeposits.collect { deposits ->
                _state.update { it.copy(history = deposits) }
            }
        }
    }

    fun deleteFromHistory(id: Long) {
        viewModelScope.launch { repository.delete(id) }
    }

    fun clearForm() = _state.update {
        it.copy(
            initialAmount = "", periodMonths = "", monthlyTopUp = "",
            interestRate = null, result = null, error = null
        )
    }

    fun clearError() = _state.update { it.copy(error = null) }
}