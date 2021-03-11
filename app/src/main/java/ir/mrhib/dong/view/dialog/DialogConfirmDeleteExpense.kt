package ir.mrhib.dong.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import ir.mrhib.dong.R
import ir.mrhib.dong.databinding.DialogConfirmDeleteAccountBinding
import ir.mrhib.dong.databinding.DialogConfirmDeleteExpenseBinding
import ir.mrhib.dong.databinding.DialogConfirmDeletePersonBinding
import ir.mrhib.dong.model.Account
import ir.mrhib.dong.model.Expense
import ir.mrhib.dong.model.ExpenseWithPerson
import ir.mrhib.dong.model.Person
import ir.mrhib.dong.repository.AccountingDatabase
import ir.mrhib.dong.repository.ExpenseDao
import ir.mrhib.dong.repository.PersonDao
import ir.mrhib.dong.viewmodel.AccountsViewModel
import ir.mrhib.dong.viewmodel.PersonsViewModel
import ir.mrhib.dong.viewmodel.PersonsViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DialogConfirmDeleteExpense(var expense: ExpenseWithPerson?) : DialogFragment() {

    private lateinit var binding: DialogConfirmDeleteExpenseBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogConfirmDeleteExpenseBinding
            .inflate(LayoutInflater.from(context));
        binding.expense = expense

        binding.btnCancelDialog.setOnClickListener {
            dialog!!.dismiss()
        }

        binding.btnDeleteExpense.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) { deleteExpense() }
        }
        return AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .create()
    }

    private suspend fun deleteExpense() {
        val expenseDao: ExpenseDao = AccountingDatabase.getDatabase(requireContext()).expenseDao();
        if (expenseDao.deleteExpense(expense!!.expense.expense_id) > 0) {
            dialog!!.dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        //val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.rounded_corner);
    }
}