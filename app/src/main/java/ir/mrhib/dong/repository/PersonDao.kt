package ir.mrhib.dong.repository

import androidx.lifecycle.LiveData
import androidx.room.*
import ir.mrhib.dong.model.Person
import ir.mrhib.dong.model.PersonWithTotalExpense

@Dao
interface PersonDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(person: Person): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(person: Person): Int

    @Query("DELETE FROM Persons WHERE person_id=:personID")
    suspend fun deletePerson(personID: Long): Int

    @Query("SELECT * FROM Persons WHERE person_id = :personID LIMIT 1")
    suspend fun getPersonById(personID: Long): Person?


    @Query("SELECT * FROM Persons WHERE person_accountId=:accountID ORDER BY person_name")
    fun getAllPersonByAccount(accountID: Long): LiveData<List<Person>>


    @Query("SELECT Persons.person_id,Persons.person_name,coalesce(SUM(ex_price),0) as totalExpense,coalesce(COUNT(*),0) as count FROM Persons LEFT JOIN Expenses ON Persons.person_id=Expenses.ex_person_id AND Persons.person_accountId=Expenses.ex_account_id WHERE Persons.person_accountId=:accountID GROUP BY Persons.person_id")
    suspend fun getAllTotalExpenses(accountID: Long): List<PersonWithTotalExpense>
}