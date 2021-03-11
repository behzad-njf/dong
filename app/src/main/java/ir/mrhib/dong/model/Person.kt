package ir.mrhib.dong.model

import androidx.room.*

@Entity(
    tableName = "Persons",
    foreignKeys = [ForeignKey(
        entity = Account::class,
        parentColumns = ["account_id"],
        childColumns = ["person_accountId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [
        Index(value = ["person_accountId"]),
        Index(value = ["person_id"]),
    ]
)
data class Person(
    @PrimaryKey(autoGenerate = true)
    var person_id: Long = 0,
    var person_name: String = "",
    var person_accountId: Long = 0
) {
    constructor() : this(0, "", 0)

    override fun toString(): String {
        return person_name
    }
}
