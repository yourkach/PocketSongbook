package com.example.pocketsongbook.feature.search.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketsongbook.R
import com.example.pocketsongbook.data.models.SongSearchItem
import com.example.pocketsongbook.utils.setOnSafeClickListener
import kotlinx.android.synthetic.main.item_search_song.view.*

class SearchAdapter(private val onItemClickResponse: (searchItem: SongSearchItem) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<SongSearchItem> = listOf()

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

    fun setList(newItems: List<SongSearchItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(searchItem: SongSearchItem) {
            itemView.apply {
                tvSongArtist.text = searchItem.title
                tvSongTitle.text = searchItem.artist
                songFavoriteIv.isVisible = searchItem.isFavourite
                setOnSafeClickListener {
                    onItemClickResponse(searchItem)
                }
            }
        }
    }

}

