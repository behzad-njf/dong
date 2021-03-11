package ir.mrhib.dong.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "accounts")
class Account(
    @PrimaryKey(autoGenerate = true)
    var account_id: Long = 0,
    var account_name: String = "",
    var account_date: String = "",

    @Ignore
    var expenses: List<ExpenseWithPerson> = emptyList(),
    @Ignore
    var persons: List<Person> = emptyList()
) : Serializable {
    constructor() : this(0, "", "")
}