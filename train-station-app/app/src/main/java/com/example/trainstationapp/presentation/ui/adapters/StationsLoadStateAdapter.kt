package com.example.trainstationapp.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trainstationapp.databinding.StationsLoadStateFooterViewItemBinding

/**
 * The `Adapter` for the footer load state view item in the `RecyclerView`.
 *
 * As displaying a header/footer based on the loading state and implementing a retry mechanism are
 * common tasks, the Paging 3.0 API helps us with both of these.
 *
 * For header/footer implementation we'll use a `LoadStateAdapter`. This implementation of
 * `RecyclerView.Adapter` is automatically notified of changes in load state. It makes sure that
 * only `Loading` and `Error` states lead to items being displayed and notifies the `RecyclerView`
 * when an item is removed, inserted, or changed, depending on the `LoadState`.
 *
 * For the retry mechanism we use `adapter.retry()`. Under the hood, this method ends up calling
 * our `PagingSource` implementation for the right page. The response will be automatically
 * propagated via `Flow<PagingData>`.
 */
class StationsLoadStateAdapter(private val onClick: OnClickListener) :
    LoadStateAdapter<StationsLoadStateAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        return ViewHolder(
            StationsLoadStateFooterViewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onClick
        )
    }

    fun interface OnClickListener {
        fun onClick()
    }

    class ViewHolder(
        private val binding: StationsLoadStateFooterViewItemBinding,
        private val retry: OnClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) {
            binding.loadState = loadState
            binding.retryButton.setOnClickListener { retry.onClick() }
            binding.executePendingBindings()
        }
    }
}
