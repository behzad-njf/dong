package ir.mrhib.dong.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ir.mrhib.dong.model.Account
import ir.mrhib.dong.repository.AccountDao
import ir.mrhib.dong.repository.AccountingDatabase

class AccountsViewModel(application: Application) : AndroidViewModel(application) {

    private val accountDao: AccountDao = AccountingDatabase.getDatabase(application).accountDao();
    val accountsList: LiveData<List<Account>> = accountDao.getAllAccounts

    suspend fun insert(account: Account) {
        accountDao.insert(account)
    }

    suspend fun update(account: Account) {
        accountDao.update(account)
    }

    suspend fun delete(account: Account) {
        accountDao.deleteAccount(account.account_id)
    }

}