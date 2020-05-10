package com.example.pocketsongbook.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pocketsongbook.R
import com.example.pocketsongbook.domain.model.Chord
import kotlinx.android.synthetic.main.chord_item.view.*

class ChordsAdapter() :
    RecyclerView.Adapter<ChordsAdapter.ViewHolder>() {

    private var chords: List<Chord> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.chord_item, parent, false
        )
    )

    fun setChords(newChords: List<Chord>) {
        chords = newChords
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = chords.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(chords[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(chord: Chord) {
            itemView.apply {
                Glide.with(chordPicIv.context)
                    .load(chord.imgUrl)
                    .error(R.drawable.ic_chord_stub)
                    .into(chordPicIv)
            }
        }
    }
}