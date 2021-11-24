package com.vaidadry.demobankapp.transactions.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vaidadry.demobankapp.R
import com.vaidadry.demobankapp.data.entities.Transaction
import com.vaidadry.demobankapp.databinding.ViewTransactionListItemBinding

class TransactionsAdapter(
    var items: List<Transaction> = emptyList()
) : RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ViewTransactionListItemBinding.inflate(layoutInflater, parent, false)
        return TransactionViewHolder(binding, parent.context)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class TransactionViewHolder(
        private val binding: ViewTransactionListItemBinding,
        val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Transaction) {
            with(binding) {
                title.text = item.counterPartyName
                subtitle.text = item.description
                if (item.isCredit()) {
                    amount.text = "- ${item.getMoney()}"
                    amount.setTextColor(getColor(context, R.color.red))
                } else {
                    amount.text = "+ ${item.getMoney()}"
                    amount.setTextColor(getColor(context, R.color.green))
                }
                divider.isVisible = items.last() != item
            }
        }
    }

    fun setListItems(newItems: List<Transaction>) {
        val callback = DiffUtil.calculateDiff(
            TransactionsCallback(
                newItems,
                this.items
            )
        )
        this.items = newItems
        callback.dispatchUpdatesTo(this)
    }
}