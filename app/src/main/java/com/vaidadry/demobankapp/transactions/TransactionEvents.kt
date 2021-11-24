package com.vaidadry.demobankapp.transactions

sealed class TransactionEvents {
    object SyncTransactions : TransactionEvents()
}