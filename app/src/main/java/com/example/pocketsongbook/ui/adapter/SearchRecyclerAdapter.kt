package com.example.pocketsongbook.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketsongbook.R
import com.example.pocketsongbook.data.SongSearchItem
import com.example.pocketsongbook.ui.presenter.SearchPresenter
import kotlinx.android.synthetic.main.song_item_layout.view.*

class SearchRecyclerAdapter(private val presenter: SearchPresenter) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var viewItems: ArrayList<SongSearchItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TextViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.song_item_layout,
                parent,
                false
            ), presenter
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TextViewHolder -> {
                holder.bind(viewItems[position])
            }
        }
    }

    override fun getItemCount(): Int = viewItems.size

    fun submitList(listViewSongItem: ArrayList<SongSearchItem>) {
        viewItems = listViewSongItem
    }

    class TextViewHolder constructor(itemView: View, private val delegate: SearchPresenter) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val artistTextView: TextView = itemView.songArtistTv
        private val songTextView: TextView = itemView.songTitleTv
        private lateinit var link: String

        override fun onClick(v: View?) {
            if (v != null) {
                delegate.onSongClicked(adapterPosition)
            }
        }

        fun bind(viewSongItem: SongSearchItem) {
            artistTextView.text = viewSongItem.artist
            songTextView.text = viewSongItem.title
            link = viewSongItem.link
            itemView.setOnClickListener(this)
        }
    }

}

