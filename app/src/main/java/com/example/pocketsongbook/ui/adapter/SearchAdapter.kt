package com.example.pocketsongbook.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketsongbook.R
import com.example.pocketsongbook.domain.model.SongSearchItem
import kotlinx.android.synthetic.main.song_item_layout.view.*

class SearchAdapter(private val onItemClickResponse: (position: Int) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<SongSearchItem> = listOf()

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

    fun setList(newItems: List<SongSearchItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(searchItem: SongSearchItem) {
            itemView.apply {
                songTitleTv.text = searchItem.title
                songArtistTv.text = searchItem.artist
                if (searchItem.isFavourite) {
                    songFavoriteIv.visibility = View.VISIBLE
                } else {
                    songFavoriteIv.visibility = View.GONE
                }
                setOnClickListener {
                    onItemClickResponse(adapterPosition)
                }
            }
        }
    }

}
