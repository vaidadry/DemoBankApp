package com.vaidadry.demobankapp.repository

import com.vaidadry.demobankapp.data.entities.Transaction
import com.vaidadry.demobankapp.data.model.ApiTransaction

interface MainRepository {

    fun getAllTransactions(): List<Transaction>

    fun updateTransactionsList(apiTransactions: List<ApiTransaction>?)

    fun deleteNonExistentTransactions(apiTransactions: List<ApiTransaction>?)
}