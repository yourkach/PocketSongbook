package com.example.pocketsongbook.feature.favourites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketsongbook.R
import com.ybond.core_entities.models.FavoriteSongModel
import com.example.pocketsongbook.databinding.ItemFavoriteSongBinding
import com.example.pocketsongbook.utils.EqualsDiffCallback
import com.example.pocketsongbook.utils.formatByDefault
import com.example.pocketsongbook.utils.utcMillisToLocalDate

class FavoriteSongsAdapter(
    private val onItemClickResponse: (song: FavoriteSongModel) -> Unit
) : ListAdapter<FavoriteSongModel, FavoriteSongsAdapter.ViewHolder>(
    EqualsDiffCallback<FavoriteSongModel> { a, b ->
        a.song.url == b.song.url
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_favorite_song,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemFavoriteSongBinding.bind(itemView)

        fun bind(model: FavoriteSongModel) {
            binding.apply {
                tvSongArtist.text = model.song.artist
                tvSongTitle.text = model.song.title
                tvSongWebsite.text = model.song.website.websiteName
                val dateText = model.timeAdded.utcMillisToLocalDate().formatByDefault()
                tvSavedDatetime.text = root.context.getString(R.string.saved_at, dateText)
                root.setOnClickListener {
                    onItemClickResponse(model)
                }
            }
        }
    }

}