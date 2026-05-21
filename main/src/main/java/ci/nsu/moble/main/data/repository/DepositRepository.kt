package ci.nsu.moble.main.data.repository

import ci.nsu.moble.main.data.dao.DepositDao
import ci.nsu.moble.main.data.model.Deposit
import kotlinx.coroutines.flow.Flow

class DepositRepository(private val dao: DepositDao) {
    val allDeposits: Flow<List<Deposit>> = dao.getAll()

    suspend fun insert(deposit: Deposit) = dao.insert(deposit)
    suspend fun delete(id: Long) = dao.delete(id)
}