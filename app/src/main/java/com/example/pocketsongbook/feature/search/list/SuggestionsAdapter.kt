package com.example.pocketsongbook.feature.search.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketsongbook.R
import com.example.pocketsongbook.feature.search.QuerySuggestion
import com.example.pocketsongbook.utils.EqualsDiffCallback
import com.example.pocketsongbook.utils.inflate
import kotlinx.android.synthetic.main.item_suggestion.view.*

class SuggestionsAdapter(
    private val onSuggestionClick: (String) -> Unit,
    private val onSuggestionDelete: (String) -> Unit
) : ListAdapter<QuerySuggestion, SuggestionsAdapter.ViewHolder>(
    EqualsDiffCallback<QuerySuggestion> { a, b -> a == b }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent.inflate(R.layout.item_suggestion))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(suggestion: QuerySuggestion) {
            with(itemView) {
                tvSuggestionText.text = suggestion.text
                setOnClickListener { onSuggestionClick(suggestion.text) }
                ivDeleteSuggestion.setOnClickListener { onSuggestionDelete(suggestion.text) }
            }
        }
    }
}