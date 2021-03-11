package ir.mrhib.dong.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import ir.mrhib.dong.R
import ir.mrhib.dong.databinding.FragmentExpensesBinding
import ir.mrhib.dong.model.Account
import ir.mrhib.dong.model.ExpenseWithPerson
import ir.mrhib.dong.view.MainActivity
import ir.mrhib.dong.view.adapter.ExpensesListAdapter
import ir.mrhib.dong.viewmodel.*
import java.text.DecimalFormat

class ExpensesFragment : Fragment() {

    private lateinit var expensesListAdapter: ExpensesListAdapter
    private var acc: Account? = null
    private var binding: FragmentExpensesBinding? = null
    private lateinit var expensesList: List<ExpenseWithPerson>
    private var totalExpense = MutableLiveData<Long>().apply {
        value = 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExpensesBinding.inflate(inflater, container, false)
        val view = binding!!.root
        acc = (activity as MainActivity).getSelectedAccount()
        expensesListAdapter =
            ExpensesListAdapter(parentFragmentManager, this, acc!!)
        binding!!.adapter = expensesListAdapter
        initData()


        totalExpense.observe(viewLifecycleOwner, {
            binding!!.txtTotalExpenses.text = String.format(
                "%s  %s  %s", getString(R.string.total_expenses1),
                DecimalFormat("###,###,###").format(it).toString(), getString(R.string.price_unit)
            )
        })


        return view
    }

    private fun initData() {
        val expensesViewModel: ExpensesViewModel by viewModels {
            ExpensesViewModelFactory(requireContext(), acc)
        }
        expensesViewModel.expenseList.observe(viewLifecycleOwner,
            {
                expensesList = it
                expensesListAdapter.setExpenses(it)
                totalExpense.apply {
                    value = 0
                    it.map { expenseWithPerson ->
                        value = value?.plus(expenseWithPerson.expense.ex_price)
                    }
                }
            }
        )
        val personsViewModel: PersonsViewModel by viewModels {
            PersonsViewModelFactory(requireContext(), acc)
        }
        personsViewModel.personsList.observe(viewLifecycleOwner, {
            acc?.persons = it
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}