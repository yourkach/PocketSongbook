package com.example.pocketsongbook

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.text_line_layout.view.*

class SongTextRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: ArrayList<Pair<String, Boolean>> = ArrayList() //<line, is_chord_line>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TextViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.text_line_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TextViewHolder -> {
                holder.bind(items[position].first, items[position].second)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun submitList(itemList: ArrayList<Pair<String, Boolean>>) {
        items = itemList
    }

    fun removeAt(pos: Int) {
        items.removeAt(pos)
        notifyDataSetChanged()
        notifyItemRangeChanged(pos, items.size)
    }

    class TextViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textView = itemView.line_text

        fun bind(text: String, isChordLine: Boolean) {
            textView.text = text
            if (isChordLine) textView.setTypeface(null, Typeface.BOLD)
        }
    }

}
