package ir.mrhib.dong.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ir.mrhib.dong.model.Account

class ExpensesViewModelFactory(private val context: Context, var account: Account?) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ExpensesViewModel(context, account!!) as T
    }
}