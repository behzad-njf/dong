package ir.mrhib.dong.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ir.mrhib.dong.R
import ir.mrhib.dong.databinding.DialogAddAccountBinding
import ir.mrhib.dong.databinding.DialogAddPersonBinding
import ir.mrhib.dong.model.Account
import ir.mrhib.dong.model.Person
import ir.mrhib.dong.repository.AccountingDatabase
import ir.mrhib.dong.utils.hideKeyboard
import ir.mrhib.dong.utils.showKeyboard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DialogAddPerson(var createOrEdit: Boolean, var person: Person?, var accountID: Long) :
    DialogFragment() {
    private lateinit var binding: DialogAddPersonBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddPersonBinding
            .inflate(LayoutInflater.from(context));
        if (createOrEdit) {
            binding.btnAddPerson.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) { createAccount() }
            }
        } else {
            binding.btnAddPerson.text = resources.getString(R.string.save_person)
            binding.etPersonName.setText(person!!.person_name)
            binding.btnAddPerson.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) { savePerson() }
            }
        }
        binding.etPersonName.requestFocus()
        context?.let { showKeyboard(it) }
        return AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .create()
    }

    private suspend fun savePerson() {
        if (TextUtils.isEmpty(binding.etPersonName.text)) {
            return;
        }
        val personDao = AccountingDatabase.getDatabase(requireContext()).personDao()
        person!!.person_name = binding.etPersonName.text.toString()
        if (personDao.update(person!!) > 0) {
            dialog!!.dismiss()
            context?.let { hideKeyboard(it) }
        }
    }

    private suspend fun createAccount() {
        if (TextUtils.isEmpty(binding.etPersonName.text)) {
            return;
        }
        val personDao = AccountingDatabase.getDatabase(requireContext()).personDao()
        val names: String = binding.etPersonName.text.toString()
        if (names.contains(".")) {
            for (n in names.split(".")) {
                if (n.length > 2)
                    personDao.insert(
                        Person(
                            person_name = n,
                            person_accountId = accountID
                        )
                    )
            }
        } else {
            personDao.insert(
                Person(
                    person_name = binding.etPersonName.text.toString(),
                    person_accountId = accountID
                )
            )
        }

        dialog!!.dismiss()
        context?.let { hideKeyboard(it) }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        //val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.rounded_corner);
    }
}