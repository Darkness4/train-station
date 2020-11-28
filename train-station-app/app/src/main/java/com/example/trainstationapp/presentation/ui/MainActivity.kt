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

    private val adapter = StationsAdapter(
        onFavorite = { station ->
            lifecycleScope.launch {
                stationRepository.updateOne(station.toggleFavorite())
            }
        },
        onClick = { station ->
            Timber.i("TODO: Lancer une activity et faire passer station")
        }
    )

    private val viewModel by viewModels<StationViewModel> {
        StationViewModel.Factory(stationRepository)
    }

    private var fetchJob: Job? = null

    /**
     * Call `viewModel.watch()`, collect the `PagingData` result and pass to the `StationAdapter`.
     *
     * This method is the bridge between the repository and the `RecyclerView`.
     */
    private fun fetch(search: String) {
        fetchJob?.cancel()
        fetchJob = lifecycleScope.launch {
            viewModel.watch(search).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun initSearchUi() {
        /*binding.searchRepo.setText(query)

        binding.searchRepo.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }
        binding.searchRepo.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }*/

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.list.addItemDecoration(decoration)
        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = StationsLoadStateAdapter(onClick = { adapter.retry() }),
            footer = StationsLoadStateAdapter(onClick = { adapter.retry() })
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

        // Fetch data
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        fetch(query)
        initSearchUi()
    }

    private fun updateRepoListFromInput() {
        /*binding.searchRepo.text.trim().let {
            if (it.isNotEmpty()) {
                search(it.toString())
            }
        }*/
    }

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = ""
    }
}
