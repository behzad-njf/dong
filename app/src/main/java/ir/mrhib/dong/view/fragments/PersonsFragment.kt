package ir.mrhib.dong.view.fragments;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import ir.mrhib.dong.databinding.FragmentPersonsBinding
import ir.mrhib.dong.model.Account
import ir.mrhib.dong.model.Expense
import ir.mrhib.dong.model.Person
import ir.mrhib.dong.view.MainActivity
import ir.mrhib.dong.view.adapter.PersonsListAdapter
import ir.mrhib.dong.viewmodel.*

class PersonsFragment : Fragment() {

    private lateinit var personsList: List<Person>
    private lateinit var personsListAdapter: PersonsListAdapter
    private lateinit var acc: Account
    private var binding: FragmentPersonsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        acc = (activity as MainActivity).getSelectedAccount()
        binding = FragmentPersonsBinding.inflate(inflater, container, false)
        val view = binding!!.root
        personsListAdapter = PersonsListAdapter(parentFragmentManager)
        binding!!.adapter = personsListAdapter
        initData()
        return view
    }

    private fun initData() {
        val personsViewModel: PersonsViewModel by viewModels {
            PersonsViewModelFactory(requireContext(), acc)
        }
        personsViewModel.personsList.observe(viewLifecycleOwner,
            { persons: List<Person>? ->
                personsList = persons!!
                personsListAdapter.setPersons(persons)
                acc.persons = persons
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}