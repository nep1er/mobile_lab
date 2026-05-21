package ci.nsu.moble.main.domain.usecase

import ci.nsu.moble.main.data.model.Deposit

class CalculateDepositUseCase {

    operator fun invoke(
        initialAmount: Double,
        periodMonths: Int,
        interestRate: Double,
        monthlyTopUp: Double? = null
    ): Deposit {
        val monthlyRate = interestRate / 100 / 12
        var currentAmount = initialAmount
        var totalInterest = 0.0

        repeat(periodMonths) {
            val monthlyInterest = currentAmount * monthlyRate
            totalInterest += monthlyInterest
            currentAmount += monthlyInterest
            monthlyTopUp?.let { currentAmount += it }
        }

        return Deposit(
            initialAmount = initialAmount,
            periodMonths = periodMonths,
            interestRate = interestRate,
            monthlyTopUp = monthlyTopUp,
            finalAmount = currentAmount,
            interestEarned = totalInterest
        )
    }

    fun getRateForPeriod(months: Int): Double = when {
        months < 6 -> 15.0
        months < 12 -> 10.0
        else -> 5.0
    }
}