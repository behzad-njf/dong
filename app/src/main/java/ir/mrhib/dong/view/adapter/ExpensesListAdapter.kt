package ir.mrhib.dong.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.map
import androidx.recyclerview.widget.RecyclerView
import ir.mrhib.dong.databinding.ItemListExpensesBinding
import ir.mrhib.dong.databinding.ItemListPersonsBinding
import ir.mrhib.dong.model.Account

import ir.mrhib.dong.model.Expense
import ir.mrhib.dong.model.ExpenseWithPerson
import ir.mrhib.dong.model.Person
import ir.mrhib.dong.repository.AccountingDatabase
import ir.mrhib.dong.view.MainActivity
import ir.mrhib.dong.view.dialog.*
import ir.mrhib.dong.viewmodel.ExpensesViewModel
import ir.mrhib.dong.viewmodel.ExpensesViewModelFactory
import ir.mrhib.dong.viewmodel.PersonsViewModel
import ir.mrhib.dong.viewmodel.PersonsViewModelFactory
import java.io.Serializable

class ExpensesListAdapter(
    var supportFragmentManager: FragmentManager,
    var fragment: Fragment,
    var account: Account
) :
    RecyclerView.Adapter<ExpensesListAdapter.ViewHolder>() {

    private var items: List<ExpenseWithPerson> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemListExpensesBinding.inflate(inflater, parent, false)

        binding.imgDeleteExpense.setOnClickListener {
            DialogConfirmDeleteExpense(binding.expense).show(
                supportFragmentManager,
                "DialogConfirmDeleteAccount"
            )
        }
        return ViewHolder(binding)
    }

    fun setExpenses(expenses: List<ExpenseWithPerson>) {
        this.items = expenses
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    inner class ViewHolder(private val binding: ItemListExpensesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ExpenseWithPerson) {
            binding.expense = item
            binding.executePendingBindings()
        }
    }
}