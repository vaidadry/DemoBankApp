package com.vaidadry.demobankapp.util

import com.vaidadry.demobankapp.data.TransactionsApi
import com.vaidadry.demobankapp.data.model.ApiTransaction
import com.vaidadry.demobankapp.repository.MainRepository
import javax.inject.Inject

class TransactionsSynchronizationManager @Inject constructor(
    private val repository: MainRepository,
    private val api: TransactionsApi
) {

    suspend fun syncTransactions(): Resource<List<ApiTransaction>> {
        return try {
            val response = api.getAllTransactions()
            val result = response.body()
            if (response.isSuccessful && result != null) {
                repository.updateTransactionsList(result)
                repository.deleteNonExistentTransactions(result)
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An Error Occurred")
        }
    }
}