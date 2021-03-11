package ir.mrhib.dong.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import ir.mrhib.dong.databinding.ItemListAccountBinding
import ir.mrhib.dong.databinding.ItemListPersonsBinding
import ir.mrhib.dong.model.Account
import ir.mrhib.dong.model.Person
import ir.mrhib.dong.view.MainActivity
import ir.mrhib.dong.view.dialog.DialogAddAccount
import ir.mrhib.dong.view.dialog.DialogAddPerson
import ir.mrhib.dong.view.dialog.DialogConfirmDeleteAccount
import ir.mrhib.dong.view.dialog.DialogConfirmDeletePerson
import java.io.Serializable

class PersonsListAdapter(var supportFragmentManager: FragmentManager) :
    RecyclerView.Adapter<PersonsListAdapter.ViewHolder>() {

    private var persons: List<Person> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemListPersonsBinding.inflate(inflater, parent, false)

        binding.imgDeletePerson.setOnClickListener {
            DialogConfirmDeletePerson(binding.person).show(
                supportFragmentManager,
                "DialogConfirmDeleteAccount"
            )
        }
        binding.imgEditPerson.setOnClickListener {
            DialogAddPerson(false, binding.person, 0).show(
                supportFragmentManager,
                "DialogEditAccount"
            )
        }
        return ViewHolder(binding)
    }

    public fun setPersons(persons: List<Person>) {
        this.persons = persons
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return persons.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(persons[position])

    inner class ViewHolder(private val binding: ItemListPersonsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Person) {
            binding.person = item
            binding.executePendingBindings()
        }
    }
}