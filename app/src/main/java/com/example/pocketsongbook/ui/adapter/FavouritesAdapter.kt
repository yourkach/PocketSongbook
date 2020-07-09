package com.example.pocketsongbook.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketsongbook.R
import com.example.pocketsongbook.data.database.SongEntity
import kotlinx.android.synthetic.main.song_item_layout.view.*

class FavouritesAdapter(private val onItemClickResponse: (position: Int) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<SongEntity> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.song_item_layout,
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

    fun setList(newItems: List<SongEntity>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(song: SongEntity) {
            itemView.apply {
                songTitleTv.text = song.title
                songArtistTv.text = song.artist
                setOnClickListener {
                    onItemClickResponse(adapterPosition)
                }
            }
        }
    }
}