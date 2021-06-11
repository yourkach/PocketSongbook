package com.example.pocketsongbook.feature.song

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pocketsongbook.R
import com.example.pocketsongbook.databinding.ItemChordBinding
import com.ybond.core_entities.models.Chord

class ChordsAdapter() :
    RecyclerView.Adapter<ChordsAdapter.ViewHolder>() {

    private var chords: List<Chord> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_chord, parent, false
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

        private val binding = ItemChordBinding.bind(itemView)

        fun bind(chord: Chord) {
            itemView.apply {
                Glide.with(binding.chordPicIv.context)
                    .load(chord.imgUrl)
                    .error(R.drawable.ic_chord_stub)
                    .into(binding.chordPicIv)
            }
        }
    }
}