package com.example.trainstationapp.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.trainstationapp.databinding.StationItemBinding
import com.example.trainstationapp.domain.entities.Station

/**
 * The `Adapter` for the paged `RecyclerView`.
 *
 * To bind `PagingData` to a `RecyclerView` (instead of a `List`), we use a `PagingDataAdapter`.
 * The `PagingDataAdapter` gets notified whenever the `PagingData` content is loaded and then it
 * signals the `RecyclerView` to update.
 */
class StationsAdapter(
    private val onFavorite: OnClickListener,
    private val onClick: OnClickListener,
) :
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
        onFavorite,
        onClick,
    )

    fun interface OnClickListener {
        fun onClick(station: Station)
    }

    class ViewHolder(
        private val binding: StationItemBinding, // station_item.xml
        private val onFavorite: OnClickListener,
        private val onClick: OnClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(station: Station) {
            binding.station = station
            binding.favoriteButton.setOnClickListener { // TODO: May want to change root to a favorite button...
                onFavorite.onClick(station)
            }
            binding.root.setOnClickListener {
                onClick.onClick(station)
            }
            binding.executePendingBindings()
        }
    }
}
