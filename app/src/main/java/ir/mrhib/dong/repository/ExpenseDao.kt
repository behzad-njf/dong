package ir.mrhib.dong.repository

import androidx.lifecycle.LiveData
import androidx.room.*
import ir.mrhib.dong.model.Expense
import ir.mrhib.dong.model.ExpenseWithPerson


@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(expense: Expense): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(expense: Expense): Int

    @Query("DELETE FROM Expenses WHERE expense_id=:expenseID")
    suspend fun deleteExpense(expenseID: Long): Int


    @Query("SELECT * FROM Expenses WHERE expense_id = :expenseID LIMIT 1")
    suspend fun getExpenseById(expenseID: Long): Expense?


    @Query("SELECT * FROM Expenses WHERE ex_person_id=:personID ORDER BY ex_title")
    fun getAllExpensesByPerson(personID: Long): LiveData<List<Expense>>

    @Query("SELECT * FROM Expenses WHERE ex_account_id=:accountID ORDER BY ex_title")
    fun getAllExpensesByAccount(accountID: Long): LiveData<List<Expense>>

    @Transaction
    @Query("SELECT * FROM expenses WHERE ex_account_id=:accountID")
    fun getExpensesWithPersonByAccountID(accountID: Long): LiveData<List<ExpenseWithPerson>>

}