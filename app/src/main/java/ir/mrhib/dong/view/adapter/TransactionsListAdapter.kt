package ir.mrhib.dong.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.mrhib.dong.databinding.ItemListTransactionBinding
import ir.mrhib.dong.model.Transaction


class TransactionsListAdapter(transactions: List<Transaction>) :
    RecyclerView.Adapter<TransactionsListAdapter.ViewHolder>() {

    private var items: List<Transaction> = transactions

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemListTransactionBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    inner class ViewHolder(private val binding: ItemListTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Transaction) {
            binding.transaction = item
            binding.executePendingBindings()
        }
    }
}