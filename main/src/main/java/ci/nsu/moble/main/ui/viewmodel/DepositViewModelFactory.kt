package ci.nsu.moble.main.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ci.nsu.moble.main.data.repository.DepositRepository
import ci.nsu.moble.main.domain.usecase.CalculateDepositUseCase

class DepositViewModelFactory(
    private val repository: DepositRepository,
    private val calculate: CalculateDepositUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DepositViewModel::class.java)) {
            return DepositViewModel(repository, calculate) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}