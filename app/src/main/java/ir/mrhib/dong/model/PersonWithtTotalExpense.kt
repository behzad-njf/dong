package ir.mrhib.dong.model

data class PersonWithTotalExpense(
    val person_id: Long,
    val person_name: String,
    val totalExpense: Long,
    val count: Int
)
