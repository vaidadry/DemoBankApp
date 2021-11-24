package com.vaidadry.demobankapp.transactions.adapters

import androidx.recyclerview.widget.DiffUtil
import com.vaidadry.demobankapp.data.entities.Transaction
import com.vaidadry.demobankapp.util.allTrue

class TransactionsCallback(
    private val newList: List<Transaction>,
    private val oldList: List<Transaction>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return allTrue(
            oldItem.id == newItem.id
                && oldItem.counterPartyName == newItem.counterPartyName
                && oldItem.counterPartyAccount == newItem.counterPartyAccount
                && oldItem.type == newItem.type
                && oldItem.amount == newItem.amount
                && oldItem.description == newItem.description
                && oldItem.date == newItem.date
        )
    }
}