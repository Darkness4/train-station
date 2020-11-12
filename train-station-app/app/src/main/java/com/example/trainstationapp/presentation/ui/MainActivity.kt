package com.example.trainstationapp.presentation.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.trainstationapp.databinding.ActivityMainBinding
import com.example.trainstationapp.domain.repositories.StationRepository
import com.example.trainstationapp.presentation.ui.adapters.StationsAdapter
import com.example.trainstationapp.presentation.ui.adapters.StationsLoadStateAdapter
import com.example.trainstationapp.presentation.viewmodels.StationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var stationRepository: StationRepository
    private lateinit var binding: ActivityMainBinding

    private val adapter = StationsAdapter(onClickListener = { Timber.i(it.toString()) })

    private val viewModel by viewModels<StationViewModel> {
        StationViewModel.Factory(
            stationRepository
        )
    }

    private var fetchJob: Job? = null
    private fun fetch() {
        fetchJob?.cancel()
        fetchJob = lifecycleScope.launch {
            viewModel.fetch().collectLatest {
                adapter.submitData(it)
            }
        }
    }

    @InternalCoroutinesApi
    private fun initFetch() {
        // Scroll to top when the list is refreshed from network.
        lifecycleScope.launch {
            adapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.list.scrollToPosition(0) }
        }
    }

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.list.addItemDecoration(decoration)
        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = StationsLoadStateAdapter { adapter.retry() },
            footer = StationsLoadStateAdapter { adapter.retry() }
        )

        adapter.addLoadStateListener { loadState ->
            // Only show the list if refresh succeeds.
            binding.list.isVisible = loadState.source.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error

            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    this,
                    "\uD83D\uDE28 Wooops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }

            binding.retryButton.setOnClickListener { adapter.retry() }
        }

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        fetch()
        initFetch()
    }
}
