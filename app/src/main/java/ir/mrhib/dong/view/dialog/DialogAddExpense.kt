package ir.mrhib.dong.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import ir.mrhib.dong.R
import ir.mrhib.dong.databinding.DialogAddExpenseBinding
import ir.mrhib.dong.model.Account
import ir.mrhib.dong.model.Expense
import ir.mrhib.dong.model.Person
import ir.mrhib.dong.repository.AccountingDatabase
import ir.mrhib.dong.utils.ShamsiDate
import ir.mrhib.dong.utils.hideKeyboard
import ir.mrhib.dong.utils.showKeyboard
import ir.mrhib.dong.viewmodel.PersonsViewModel
import ir.mrhib.dong.viewmodel.PersonsViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.StringBuilder

class DialogAddExpense(var account: Account) :
    DialogFragment() {
    private lateinit var binding: DialogAddExpenseBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddExpenseBinding
            .inflate(LayoutInflater.from(context))

        binding.account = account

        var slashCount = 0;
        binding.tvExpenseDate.addTextChangedListener {
            val str: String = it.toString()
            val textLength: Int = it!!.length
            if (textLength == 3 || textLength == 6) {
                if (slashCount < 2) {
                    binding.tvExpenseDate.setText(
                        StringBuilder(
                            binding.tvExpenseDate.text.toString()
                        ).insert(str.length - 1, "/").toString()
                    )
                    binding.tvExpenseDate.setSelection(binding.tvExpenseDate.text.length)
                    slashCount++
                }
            }
        }
        binding.tvExpenseDate.setText(ShamsiDate().iranianDate)

        binding.btnAddExpense.setOnClickListener {
            if(binding.etExpenseTitle.text.toString().isNotEmpty()) {
                if (checkDate(binding.tvExpenseDate.text.toString())) {
                    if (binding.etExpensePrice.text.toString().isNotEmpty()) {
                        GlobalScope.launch(Dispatchers.IO) {
                            addExpense()
                        }
                        dialog!!.dismiss()
                        context?.let { hideKeyboard(it) }
                    } else {
                        Toast.makeText(
                            context,
                            getString(R.string.invalid_price),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else
                    Toast.makeText(context, getString(R.string.invalid_date), Toast.LENGTH_SHORT)
                        .show()
            }else{
                Toast.makeText(context, getString(R.string.expense_title_cant_be_empty), Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.etExpenseTitle.requestFocus()
        context?.let { showKeyboard(it) }


        return AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .create()
    }

    private fun checkDate(date: String): Boolean {
        if (date.length < 10)
            return false
        val items = date.split('/');
        if (items[2].toInt() < 1 || items[2].toInt() > 31 || (items[2].toInt() > 6 && items[2].toInt() > 30) || (items[2].toInt() == 12 && items[2].toInt() > 29))
            return false
        if (items[1].toInt() < 1 || items[1].toInt() > 12) {
            return false
        }
        if (items[0].toInt() < 1250 || items[0].toInt() > 1500)
            return false

        return true
    }

    private suspend fun addExpense() {
        val expenseDao = AccountingDatabase.getDatabase(requireContext()).expenseDao()
        val personID = (binding.spinnerPersons.selectedItem as Person).person_id
        val expense: Expense = Expense(
            0,
            account.account_id,
            personID,
            binding.etExpenseTitle.text.toString(),
            binding.tvExpenseDate.text.toString(),
            binding.etExpensePrice.text.toString().toLong(),
        )
        expenseDao.insert(expense)
        /*(binding.spinnerPersons.selectedItem as Person).addToTotalExpense(binding.etExpensePrice.text.toString().toLong())*/
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        //val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.rounded_corner);
    }
}