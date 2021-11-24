package com.vaidadry.demobankapp.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaidadry.demobankapp.transactions.usecases.LoadTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val loadTransactionsUseCase: LoadTransactionsUseCase
) : ViewModel() {

    val channel = Channel<TransactionEvents>()

    private val _stateFlow = MutableStateFlow(TransactionsState())
    val stateFlow: StateFlow<TransactionsState> = _stateFlow

    init {
        viewModelScope.launch {
            channel.consumeAsFlow().collect { event ->
                when (event) {
                    is TransactionEvents.SyncTransactions -> loadTransactionList()
                }
            }
        }
    }

    private suspend fun loadTransactionList() {
        loadTransactionsUseCase.invokeLoadTransactions().collect { state ->
            _stateFlow.value = state
        }
    }
}