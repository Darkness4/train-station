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
import com.example.trainstationapp.core.state.doOnFailure
import com.example.trainstationapp.databinding.FragmentStationListBinding
import com.example.trainstationapp.presentation.ui.adapters.StationsAdapter
import com.example.trainstationapp.presentation.ui.adapters.StationsLoadStateAdapter
import com.example.trainstationapp.presentation.viewmodels.AuthViewModel
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
    private val authViewModel by activityViewModels<AuthViewModel>()

    private var _binding: FragmentStationListBinding? = null
    private val binding: FragmentStationListBinding
        get() = _binding!!

    private val adapter =
        StationsAdapter(
            onFavorite = {
                authViewModel.idToken.value?.let { token -> activityViewModel.update(it, token) }
            },
            onClick = { activityViewModel.showDetails(it) },
        )

    private var fetchJob: Job? = null
    private var idTokenJob: Job? = null
    private var scrollToTopJob: Job? = null
    private var networkStatusJob: Job? = null
    private var showDetailsJob: Job? = null
    private var refreshManuallyJob: Job? = null

    /**
     * Call `viewModel.watchPages()`, collect the `PagingData` result and pass to the
     * `StationAdapter`.
     *
     * This method is the bridge between the repository and the `RecyclerView`.
     */
    private fun fetch(search: String) {
        fetchJob?.cancel()
        authViewModel.idToken.value?.let { token ->
            fetchJob =
                lifecycleScope.launch {
                    activityViewModel.watchPages(search, token)
                        .collectLatest { adapter.submitData(it) }
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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStationListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.list.addItemDecoration(decoration)

        // Add the adapter for the PagingData, with footer and header.
        binding.list.adapter =
            adapter.withLoadStateHeaderAndFooter(
                header = StationsLoadStateAdapter(onRetry = { adapter.retry() }),
                footer = StationsLoadStateAdapter(onRetry = { adapter.retry() })
            )

        adapter.addLoadStateListener { loadState ->
            // Show loading spinner during initial load or refresh.
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error

            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState =
                loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(context, "\uD83D\uDE28 Whooops ${it.error}", Toast.LENGTH_LONG)
                    .show()
            }

            binding.retryButton.setOnClickListener { adapter.retry() }
        }

        // Fetch data
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        fetch(query)
        initSearchUi(query)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        // Watch for network errors
        networkStatusJob =
            lifecycleScope.launch {
                activityViewModel.networkStatus.collect {
                    it?.doOnFailure { e ->
                        Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
                        Timber.e(e)
                    }
                }
            }

        // Watch for refresh action
        refreshManuallyJob =
            lifecycleScope.launch {
                activityViewModel.refreshManually.collect {
                    it?.let {
                        when (it) {
                            MainViewModel.RefreshMode.Normal ->
                                fetch(binding.searchBar.text!!.trim().toString())
                            MainViewModel.RefreshMode.WithScrollToTop -> {
                                fetch(DEFAULT_QUERY)
                                initSearchUi(DEFAULT_QUERY)
                            }
                        }

                        activityViewModel.refreshManuallyDone()
                    }
                }
            }

        // Watch for login changes
        idTokenJob =
            lifecycleScope.launch {
                authViewModel.idToken.collect {
                    it?.let { fetch(binding.searchBar.text!!.trim().toString()) }
                }
            }

        // Watch for the "show details" action
        showDetailsJob =
            lifecycleScope.launch {
                activityViewModel.showDetails.collect {
                    it?.let {
                        authViewModel.idToken.value?.let { token ->
                            findNavController()
                                .navigate(
                                    StationListFragmentDirections.actionStationListFragmentToDetailsActivity(
                                        it, token
                                    )
                                )
                            activityViewModel.showDetailsDone()
                        }
                    }
                }
            }

        // Scroll to top when the list is refreshed from network.
        scrollToTopJob =
            lifecycleScope.launch {
                adapter
                    .loadStateFlow
                    // Only emit when REFRESH LoadState for RemoteMediator changes.
                    .distinctUntilChangedBy { it.refresh }
                    // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                    .filter { it.refresh is LoadState.NotLoading }
                    .collect { binding.list.scrollToPosition(0) }
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, binding.searchBar.text!!.trim().toString())
    }

    /** On key board presses, fetch data. */
    private fun updateListFromInput() {
        fetch(binding.searchBar.text!!.trim().toString())
    }

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = ""
    }

    override fun onStop() {
        networkStatusJob?.cancel()
        showDetailsJob?.cancel()
        refreshManuallyJob?.cancel()
        scrollToTopJob?.cancel()
        fetchJob?.cancel()
        idTokenJob?.cancel()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
