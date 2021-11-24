package com.vaidadry.demobankapp.transactions

import com.vaidadry.demobankapp.data.entities.Transaction
import com.vaidadry.demobankapp.util.LoadingStatus
import org.joda.money.CurrencyUnit.EUR
import org.joda.money.Money

class TransactionsState(
    val transactionList: List<Transaction>? = null,
    val totalSum: Money? = Money.zero(EUR),
    val loadingStatus: LoadingStatus? = null
)