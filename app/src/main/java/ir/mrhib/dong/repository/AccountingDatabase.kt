package ir.mrhib.dong.repository

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ir.mrhib.dong.model.Account
import ir.mrhib.dong.model.Expense
import ir.mrhib.dong.model.Person

@Database(
    entities = [Account::class, Person::class, Expense::class],
    version = 6,
    exportSchema = true
)
abstract class AccountingDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao
    abstract fun personDao(): PersonDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        private var INSTANCE: AccountingDatabase? = null
        private const val DB_NAME = "myDatabase.db"

        fun getDatabase(context: Context): AccountingDatabase {
            if (INSTANCE == null) {
                synchronized(AccountingDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AccountingDatabase::class.java,
                            DB_NAME
                        )
                            .fallbackToDestructiveMigration()
                            .addCallback(callBack).build()
                    }
                }
            }

            return INSTANCE!!
        }

        private val callBack = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                populateDatabase(INSTANCE!!)
            }
        }

        private fun populateDatabase(db: AccountingDatabase) {
            /*val noteDao = db.accountDao()
            subscribeOnBackground {
                noteDao.insert(Account(1, "acc1", "2021-02-01", null, null))
                noteDao.insert(Account(2, "acc2", "2020-12-08", null, null))
                noteDao.insert(Account(3, "acc3", "2021-03-21", null, null))

            }*/
        }
    }

}