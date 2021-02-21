package com.example.pocketsongbook.feature.favourites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketsongbook.R
import com.example.pocketsongbook.data.favorites.FavoriteSongModel
import com.example.pocketsongbook.utils.EqualsDiffCallback
import com.example.pocketsongbook.utils.formatByDefault
import com.example.pocketsongbook.utils.millisToLocalDate
import kotlinx.android.synthetic.main.item_favorite_song.view.*
import kotlinx.android.synthetic.main.item_search_song.view.tvSongArtist
import kotlinx.android.synthetic.main.item_search_song.view.tvSongTitle

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

        val dateFormat by lazy {

        }

        fun bind(model: FavoriteSongModel) {
            itemView.apply {
                tvSongArtist.text = model.song.artist
                tvSongTitle.text = model.song.title
                tvSongWebsite.text = model.song.website.websiteName
                val dateText = model.timeAdded.millisToLocalDate().formatByDefault()
                tvSavedDatetime.text = context.getString(R.string.saved_at, dateText)
                setOnClickListener {
                    onItemClickResponse(model)
                }
            }
        }
    }

}