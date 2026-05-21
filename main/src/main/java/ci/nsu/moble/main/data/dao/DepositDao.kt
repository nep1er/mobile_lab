package ci.nsu.moble.main.data.dao

import androidx.room.*
import ci.nsu.moble.main.data.model.Deposit
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(deposit: Deposit)

    @Query("SELECT * FROM deposits ORDER BY calculationDate DESC")
    fun getAll(): Flow<List<Deposit>>

    @Query("DELETE FROM deposits WHERE id = :id")
    suspend fun delete(id: Long)
}