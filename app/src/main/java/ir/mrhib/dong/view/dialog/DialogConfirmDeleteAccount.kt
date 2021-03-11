package ir.mrhib.dong.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ir.mrhib.dong.R
import ir.mrhib.dong.databinding.DialogConfirmDeleteAccountBinding
import ir.mrhib.dong.model.Account
import ir.mrhib.dong.repository.AccountingDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DialogConfirmDeleteAccount(var account: Account?) : DialogFragment() {

    private lateinit var binding: DialogConfirmDeleteAccountBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogConfirmDeleteAccountBinding
            .inflate(LayoutInflater.from(context));
        binding.account = account

        binding.btnCancelDialog.setOnClickListener {
            dialog!!.dismiss()
        }

        binding.btnDeleteAccount.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) { deleteAccount() }
        }
        return AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .create()
    }

    private suspend fun deleteAccount() {
        val accountDao = AccountingDatabase.getDatabase(requireContext()).accountDao()
        if (accountDao.deleteAccount(account!!.account_id) > 0) {
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