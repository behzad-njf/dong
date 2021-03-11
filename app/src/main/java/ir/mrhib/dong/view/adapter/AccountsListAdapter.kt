package ir.mrhib.dong.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import ir.mrhib.dong.databinding.ItemListAccountBinding
import ir.mrhib.dong.model.Account
import ir.mrhib.dong.view.MainActivity
import ir.mrhib.dong.view.dialog.DialogAddAccount
import ir.mrhib.dong.view.dialog.DialogConfirmDeleteAccount
import ir.mrhib.dong.viewmodel.Communicator
import java.io.Serializable

class AccountsListAdapter(var supportFragmentManager: FragmentManager, var context: Context) :
    RecyclerView.Adapter<AccountsListAdapter.ViewHolder>() {

    private var items: List<Account> = emptyList()
    private var model: Communicator? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemListAccountBinding.inflate(inflater, parent, false)

        binding.imgDeleteAccount.setOnClickListener {
            DialogConfirmDeleteAccount(binding.account).show(
                supportFragmentManager,
                "DialogConfirmDeleteAccount"
            )
        }
        binding.imgEditAccount.setOnClickListener {
            DialogAddAccount(false, binding.account).show(
                supportFragmentManager,
                "DialogEditAccount"
            )
        }

        binding.cardView.setOnClickListener {
            val intent = Intent(parent.context, MainActivity::class.java)
            intent.putExtra("account", binding.account as Serializable);
            context.startActivity(intent);
        }
        return ViewHolder(binding)
    }

    fun setAccounts(accounts: List<Account>) {
        this.items = accounts
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    inner class ViewHolder(private val binding: ItemListAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Account) {
            binding.account = item
            binding.executePendingBindings()
        }
    }
}