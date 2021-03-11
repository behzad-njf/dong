package ir.mrhib.dong.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment
import ir.mrhib.dong.R
import ir.mrhib.dong.databinding.DialogAddAccountBinding
import ir.mrhib.dong.model.Account
import ir.mrhib.dong.repository.AccountingDatabase
import ir.mrhib.dong.utils.ShamsiDate
import ir.mrhib.dong.utils.hideKeyboard
import ir.mrhib.dong.utils.showKeyboard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class DialogAddAccount(var createOrEdit: Boolean, var account: Account?) : DialogFragment() {
    private lateinit var binding: DialogAddAccountBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddAccountBinding
            .inflate(LayoutInflater.from(context));
        if (createOrEdit) {
            binding.btnCreateAccount.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) { createAccount() }
            }
        } else {
            binding.btnCreateAccount.text = resources.getString(R.string.save_account)
            binding.etAccountTitle.setText(account!!.account_name)
            binding.btnCreateAccount.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) { saveAccount() }
            }
        }

        binding.etAccountTitle.requestFocus()
        context?.let { showKeyboard(it) }

        return AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .create()
    }

    private suspend fun saveAccount() {
        if (TextUtils.isEmpty(binding.etAccountTitle.text)) {
            return;
        }
        val accountDao = AccountingDatabase.getDatabase(requireContext()).accountDao()
        account!!.account_name = binding.etAccountTitle.text.toString()
        if (accountDao.update(account!!) > 0) {
            dialog!!.dismiss()
            context?.let { hideKeyboard(it) }
        }
    }

    private suspend fun createAccount() {
        if (TextUtils.isEmpty(binding.etAccountTitle.text)) {
            return;
        }
        val accountDao = AccountingDatabase.getDatabase(requireContext()).accountDao()
        if (accountDao.insert(
                Account(
                    account_name = binding.etAccountTitle.text.toString(),
                    account_date = ShamsiDate().iranianDate,
                )
            ) > 0
        ) {
            dialog!!.dismiss()
            context?.let { hideKeyboard(it) }
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