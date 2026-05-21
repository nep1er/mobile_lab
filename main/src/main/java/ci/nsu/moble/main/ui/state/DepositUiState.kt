package ci.nsu.moble.main.ui.state

import ci.nsu.moble.main.data.model.Deposit

data class DepositUiState(
    val initialAmount: String = "",
    val periodMonths: String = "",
    val monthlyTopUp: String = "",
    val interestRate: Double? = null,
    val result: Deposit? = null,
    val history: List<Deposit> = emptyList(),
    val error: String? = null
) {
    val isStage1Valid: Boolean
        get() = initialAmount.toDoubleOrNull()?.let { it > 0 } == true &&
                periodMonths.toIntOrNull()?.let { it > 0 } == true

    val availableRate: Double?
        get() = periodMonths.toIntOrNull()?.let { months ->
            when {
                months < 6 -> 15.0
                months < 12 -> 10.0
                else -> 5.0
            }
        }
}