package com.example.trainstationapp.presentation.ui.fragments

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.trainstationapp.core.result.doOnFailure
import com.example.trainstationapp.databinding.FragmentStationListBinding
import com.example.trainstationapp.presentation.ui.adapters.StationsAdapter
import com.example.trainstationapp.presentation.ui.adapters.StationsLoadStateAdapter
import com.example.trainstationapp.presentation.viewmodels.MainViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import timber.log.Timber

class StationListFragment : Fragment() {
    private val activityViewModel by activityViewModels<MainViewModel>()

    private lateinit var binding: FragmentStationListBinding

    private val adapter = StationsAdapter(
        onFavorite = { station ->
            activityViewModel.update(station)
        },
        onClick = { station ->
            activityViewModel.showDetails(station)
        }
    )

    private var fetchJob: Job? = null

    /**
     * Call `viewModel.watchPages()`, collect the `PagingData` result and pass to the `StationAdapter`.
     *
     * This method is the bridge between the repository and the `RecyclerView`.
     */
    private fun fetch(search: String) {
        fetchJob?.cancel()
        fetchJob = lifecycleScope.launch {
            activityViewModel.watchPages(search).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun initSearchUi(query: String) {
        binding.searchBar.setText(query)

        binding.searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateListFromInput()
                true
            } else {
                false
            }
        }
        binding.searchBar.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateListFromInput()
                true
            } else {
                false
            }
        }

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStationListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.list.addItemDecoration(decoration)

        // Add the adapter for the PagingData, with footer and header.
        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = StationsLoadStateAdapter(onRetry = { adapter.retry() }),
            footer = StationsLoadStateAdapter(onRetry = { adapter.retry() })
        )

        adapter.addLoadStateListener { loadState ->
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
                    context,
                    "\uD83D\uDE28 Wooops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }

            binding.retryButton.setOnClickListener { adapter.retry() }
        }

        // Watch for network errors
        activityViewModel.networkStatus.observe(viewLifecycleOwner) {
            it?.doOnFailure { e ->
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
                Timber.e(e)
            }
        }
        // Watch for refresh action
        activityViewModel.refreshManually.observe(viewLifecycleOwner) {
            it?.let {
                when (it) {
                    MainViewModel.RefreshMode.Normal -> fetch(
                        binding.searchBar.text!!.trim().toString()
                    )
                    MainViewModel.RefreshMode.WithScrollToTop -> {
                        fetch(DEFAULT_QUERY)
                        initSearchUi(DEFAULT_QUERY)
                    }
                }

                activityViewModel.refreshManuallyDone()
            }
        }
        // Watch for the "show details" action
        activityViewModel.showDetails.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(
                    StationListFragmentDirections.actionStationListFragmentToDetailsActivity(it)
                )
                activityViewModel.showDetailsDone()
            }
        }

        // Fetch data
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        fetch(query)
        initSearchUi(query)

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, binding.searchBar.text!!.trim().toString())
    }

    /**
     * On key board presses, fetch data.
     */
    private fun updateListFromInput() {
        fetch(binding.searchBar.text!!.trim().toString())
    }

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = ""

        @JvmStatic
        fun newInstance() = StationListFragment()
    }
}
