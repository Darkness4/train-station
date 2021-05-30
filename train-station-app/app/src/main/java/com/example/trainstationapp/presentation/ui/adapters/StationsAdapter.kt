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
    private val onFavorite: (Station) -> Unit,
    private val onClick: (Station) -> Unit,
) : PagingDataAdapter<Station, StationsAdapter.ViewHolder>(Comparator) {
    object Comparator : DiffUtil.ItemCallback<Station>() {
        override fun areItemsTheSame(oldItem: Station, newItem: Station) =
            oldItem.recordid == newItem.recordid

        override fun areContentsTheSame(oldItem: Station, newItem: Station) = oldItem == newItem
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val station = getItem(position)
        station?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.create(
        parent,
        onFavorite,
        onClick,
    )

    class ViewHolder(
        private val binding: StationItemBinding, // station_item.xml
        private val onFavorite: (Station) -> Unit,
        private val onClick: (Station) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(station: Station) {
            binding.station = station
            binding.favoriteButton.setOnClickListener {
                onFavorite(station)
            }
            binding.root.setOnClickListener {
                onClick(station)
            }
            binding.executePendingBindings()
        }

        companion object {
            fun create(
                parent: ViewGroup,
                onFavorite: (Station) -> Unit,
                onClick: (Station) -> Unit
            ) =
                ViewHolder(
                    StationItemBinding.inflate( // station_item.xml
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    onFavorite,
                    onClick,
                )
        }
    }
}
