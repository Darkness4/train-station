package com.example.trainstationapp.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.trainstationapp.databinding.StationItemBinding
import com.example.trainstationapp.domain.entities.Station

class StationsAdapter(private val onClickListener: OnClickListener) :
    PagingDataAdapter<Station, StationsAdapter.ViewHolder>(Comparator) {

    object Comparator : DiffUtil.ItemCallback<Station>() {
        override fun areItemsTheSame(oldItem: Station, newItem: Station) =
            oldItem.recordid == newItem.recordid

        override fun areContentsTheSame(oldItem: Station, newItem: Station) = oldItem == newItem
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val station = getItem(position)
        station?.let { holder.bind(station) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        StationItemBinding.inflate( // station_item.xml
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        onClickListener,
    )

    fun interface OnClickListener {
        fun onClick(station: Station)
    }

    class ViewHolder(
        private val binding: StationItemBinding, // station_item.xml
        private val onClickListener: OnClickListener,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(station: Station) {
            binding.station = station
            binding.root.setOnClickListener { // TODO: May want to change root to a card...
                onClickListener.onClick(station)
            }
            binding.executePendingBindings()
        }
    }
}
