package ir.mrhib.dong.model

import androidx.room.Embedded
import androidx.room.Relation

data class ExpenseWithPerson(
    @Embedded val expense: Expense,
    @Relation(
        parentColumn = "ex_person_id",
        entityColumn = "person_id"
    )
    val person: Person
)
