package com.vaidadry.demobankapp.transactions

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.vaidadry.demobankapp.R
import com.vaidadry.demobankapp.databinding.ActivityTransactionsBinding
import com.vaidadry.demobankapp.transactions.adapters.TransactionsAdapter
import com.vaidadry.demobankapp.util.LoadingStatus.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class TransactionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransactionsBinding
    private val viewModel: TransactionsViewModel by viewModels()
    private val transactionsAdapter = TransactionsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        binding = ActivityTransactionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
        viewModel.channel.offer(TransactionEvents.SyncTransactions)

        lifecycleScope.launch {
            setupObservers()
        }
    }

    private suspend fun setupObservers() {
        viewModel.stateFlow.collect { state ->
            when (state.loadingStatus) {
                is Loading -> {
                    showProgress(true)
                    binding.transactionsRecyclerView.isVisible = false
                }
                is Success -> {
                    showProgress(false)
                    binding.transactionsRecyclerView.isVisible = true
                    state.transactionList?.let {
                        transactionsAdapter.setListItems(it)
                    }
                    binding.balance.text =
                        getString(R.string.transaction_total_label).format(state.totalSum)
                }
                is Error -> {
                    showProgress(false)
                    Snackbar.make(
                        binding.root,
                        state.loadingStatus.message,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun initUI() {
        binding.transactionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = transactionsAdapter
        }
    }

    private fun showProgress(show: Boolean) {
        binding.progressBar.isVisible = show
    }
}