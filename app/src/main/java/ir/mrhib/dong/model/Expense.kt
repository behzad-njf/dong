package ir.mrhib.dong.model

import androidx.room.*

@Entity(
    tableName = "Expenses",
    foreignKeys = [ForeignKey(
        entity = Person::class,
        parentColumns = ["person_id"],
        childColumns = ["ex_person_id"],
        onDelete = ForeignKey.CASCADE
    ),
        ForeignKey(
            entity = Account::class,
            parentColumns = ["account_id"],
            childColumns = ["ex_account_id"],
            onDelete = ForeignKey.CASCADE
        )],
    indices = [Index(value = ["ex_account_id"]),
        Index(value = ["ex_person_id"])]
)
data class Expense(
    @PrimaryKey(autoGenerate = true)
    var expense_id: Long = 0,
    var ex_account_id: Long = 0,
    var ex_person_id: Long = 0,
    var ex_title: String = "",
    var ex_date: String = "",
    var ex_price: Long = 0,
) {
    constructor() : this(0, 0, 0, "", "", 0)
}
