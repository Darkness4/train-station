package com.example.trainstationapp.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trainstationapp.databinding.StationsLoadStateFooterViewItemBinding

class StationsLoadStateAdapter(private val retry: OnClickListener) :
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
            retry
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
            binding.retryButton.setOnClickListener{ retry.onClick() }
            binding.executePendingBindings()
        }
    }
}