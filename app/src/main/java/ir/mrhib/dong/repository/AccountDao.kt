package ir.mrhib.dong.repository

import androidx.lifecycle.LiveData
import androidx.room.*
import ir.mrhib.dong.model.Account

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(account: Account): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(account: Account): Int

    @Query("DELETE FROM Accounts WHERE account_id=:accountID")
    suspend fun deleteAccount(accountID: Long): Int


    @Query("SELECT * FROM Accounts WHERE account_id = :accountID LIMIT 1")
    suspend fun getAccountById(accountID: Long): Account?

    @get:Query("SELECT * FROM Accounts ORDER BY account_id DESC")
    val getAllAccounts: LiveData<List<Account>>
}