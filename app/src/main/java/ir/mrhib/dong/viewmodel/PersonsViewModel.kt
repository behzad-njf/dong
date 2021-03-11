package ir.mrhib.dong.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ir.mrhib.dong.model.Account
import ir.mrhib.dong.model.Person
import ir.mrhib.dong.model.PersonWithTotalExpense
import ir.mrhib.dong.repository.AccountDao
import ir.mrhib.dong.repository.AccountingDatabase
import ir.mrhib.dong.repository.PersonDao
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PersonsViewModel(context: Context, var acc: Account) : ViewModel() {
    private val personsDao: PersonDao = AccountingDatabase.getDatabase(context).personDao();
    val personsList: LiveData<List<Person>> = personsDao.getAllPersonByAccount(acc.account_id)
    //val personsTotalExpensesList:List<PersonWithTotalExpense> = personsDao.getAllTotalExpenses(acc.account_id)

    suspend fun getAllPersonsWithTotalExpenses(): List<PersonWithTotalExpense> {
        return personsDao.getAllTotalExpenses(acc.account_id)
    }

    fun delete(person: Person) {
        GlobalScope.launch(Dispatchers.IO) { personsDao.deletePerson(person.person_id) }
    }
}