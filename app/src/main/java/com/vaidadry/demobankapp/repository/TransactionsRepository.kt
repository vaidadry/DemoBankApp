package com.vaidadry.demobankapp.repository

import io.realm.Realm
import com.vaidadry.demobankapp.data.TransactionsApi
import com.vaidadry.demobankapp.data.entities.Transaction
import com.vaidadry.demobankapp.data.model.ApiTransaction
import com.vicpin.krealmextensions.queryAll
import com.vicpin.krealmextensions.save
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class TransactionsRepository @Inject constructor(
    private val api: TransactionsApi,
    private val realm: Realm
) : MainRepository {

    override fun getAllTransactions(): List<Transaction> = queryAll()

    override fun updateTransactionsList(apiTransactions: List<ApiTransaction>?) {
        apiTransactions?.forEach {
            Transaction(
                id = it.id,
                counterPartyName = it.counterPartyName,
                counterPartyAccount = it.counterPartyAccount,
                type = it.type,
                amount = it.amount,
                description = it.description,
                date = SimpleDateFormat("yyyy-mm-dd", Locale.ROOT).parse(it.date)
            ).save()
        }
    }

    override fun deleteNonExistentTransactions(apiTransactions: List<ApiTransaction>?) {
        val transactionIds = apiTransactions?.map { it.id }
        getAllTransactions()
            .filter { transaction ->
                transactionIds?.firstOrNull { it == transaction.id } == null
            }
            .forEach { localPackage ->
                localPackage.deleteFromRealm()
            }
    }
}