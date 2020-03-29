package com.example.pocketsongbook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.song_item_layout.view.*

class TextRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: ArrayList<ItemSong> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TextViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.song_item_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TextViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun submitList(itemList: ArrayList<ItemSong>) {
        items = itemList
    }

    class TextViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener{

        private val artistTextView: TextView = itemView.artistTextView
        private val songTextView: TextView = itemView.songTextView
        lateinit var link : String

        override fun onClick(v: View?) {
            if (v != null) {
                Toast.makeText(v.context, link, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        fun bind(item: ItemSong) {
            artistTextView.text = item.artist
            songTextView.text = item.songName
            link = item.link
            itemView.setOnClickListener(this)
        }
    }

}

