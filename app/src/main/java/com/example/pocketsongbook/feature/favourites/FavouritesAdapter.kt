package com.example.pocketsongbook.feature.favourites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketsongbook.R
import com.example.pocketsongbook.data.models.SongModel
import kotlinx.android.synthetic.main.item_search_song.view.*

class FavouritesAdapter(private val onItemClickResponse: (song: SongModel) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<SongModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_search_song,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun setList(newItems: List<SongModel>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(song: SongModel) {
            itemView.apply {
                tvSongArtist.text = song.title
                tvSongTitle.text = song.artist
                setOnClickListener {
                    onItemClickResponse(song)
                }
            }
        }
    }
}