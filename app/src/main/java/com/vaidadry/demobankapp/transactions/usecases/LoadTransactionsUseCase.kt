package com.vaidadry.demobankapp.transactions.usecases

import com.vaidadry.demobankapp.data.entities.Transaction
import com.vaidadry.demobankapp.enums.TransactionType
import com.vaidadry.demobankapp.enums.TransactionType.*
import com.vaidadry.demobankapp.repository.MainRepository
import com.vaidadry.demobankapp.transactions.TransactionsState
import com.vaidadry.demobankapp.util.LoadingStatus
import com.vaidadry.demobankapp.util.TransactionsSynchronizationManager
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.joda.money.CurrencyUnit.EUR
import org.joda.money.Money
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
class LoadTransactionsUseCase @Inject constructor(
    private val transactionsSynchronizationManager: TransactionsSynchronizationManager,
    private val repository: MainRepository
) {
    fun invokeLoadTransactions(): Flow<TransactionsState> =
        callbackFlow {
            try {
                offer(
                    TransactionsState(
                        loadingStatus = LoadingStatus.Loading
                    )
                )
                delay(300)
                CoroutineScope(Dispatchers.IO).launch {
                    transactionsSynchronizationManager.syncTransactions()
                }
                val list = repository.getAllTransactions()
                offer(
                    TransactionsState(
                        loadingStatus = LoadingStatus.Success,
                        transactionList = list,
                        totalSum = calculateTotalSum(list)
                    )
                )
            } catch (error: Throwable) {
                offer(
                    TransactionsState(
                        loadingStatus = LoadingStatus.Error(error.localizedMessage),
                        transactionList = emptyList()
                    )
                )
            }
            close()
            awaitClose { cancel() }
        }

    private fun calculateTotalSum(list: List<Transaction>?): Money? {
        val totalCredit = getSum(list, CREDIT)
        val totalDebit = getSum(list, DEBIT)
        return Money.of(EUR, totalDebit.minus(totalCredit))
    }

    private fun getSum(
        list: List<Transaction>?,
        type: TransactionType
    ): BigDecimal {
        return list
            ?.filter { it.type == type.value }
            ?.map { it.getMoney().amount }
            ?.stream()
            ?.reduce(BigDecimal.ZERO, BigDecimal::add) ?: BigDecimal.ZERO
    }
}