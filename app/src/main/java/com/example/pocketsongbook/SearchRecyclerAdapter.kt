package com.example.pocketsongbook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketsongbook.data_classes.SongViewItem
import com.example.pocketsongbook.interfaces.AsyncResponse
import com.example.pocketsongbook.interfaces.SongClickResponse
import kotlinx.android.synthetic.main.song_item_layout.view.*

class SearchRecyclerAdapter(private val delegate: SongClickResponse) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var viewItems: ArrayList<SongViewItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TextViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.song_item_layout,
                parent,
                false
            ), delegate
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

    fun submitList(listViewItem: ArrayList<SongViewItem>) {
        viewItems = listViewItem
    }

    class TextViewHolder constructor(itemView: View, val delegate: SongClickResponse) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val artistTextView: TextView = itemView.artistTextView
        private val songTextView: TextView = itemView.titleTextView
        private lateinit var link: String

        override fun onClick(v: View?) {
            if (v != null) {
                WebPageDownloadTask(object :
                    AsyncResponse {
                    override fun processFinish(result: String?) {
                        if (result != null) {
                            delegate.onSelectedSongDownloaded(adapterPosition, result)
                        }
                    }
                }).execute(link)
            }
        }

        fun bind(viewItem: SongViewItem) {
            artistTextView.text = viewItem.artist
            songTextView.text = viewItem.title
            link = viewItem.link
            itemView.setOnClickListener(this)
        }
    }

}

