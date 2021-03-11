package ir.mrhib.dong.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ir.mrhib.dong.model.Account
import ir.mrhib.dong.model.Expense
import ir.mrhib.dong.model.ExpenseWithPerson
import ir.mrhib.dong.model.Person
import ir.mrhib.dong.repository.AccountDao
import ir.mrhib.dong.repository.AccountingDatabase
import ir.mrhib.dong.repository.ExpenseDao

class ExpensesViewModel(context: Context, var account: Account) : ViewModel() {

    private val expenseDao: ExpenseDao = AccountingDatabase.getDatabase(context).expenseDao();
    val expenseList: LiveData<List<ExpenseWithPerson>> =
        expenseDao.getExpensesWithPersonByAccountID(account.account_id)

    suspend fun insert(expense: Expense) {
        expenseDao.insert(expense)
    }

    suspend fun update(expense: Expense) {
        expenseDao.update(expense)
    }

    suspend fun delete(expense: Expense) {
        expenseDao.deleteExpense(expense.expense_id)
    }

}