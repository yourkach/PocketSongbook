package com.example.pocketsongbook.feature.search.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketsongbook.R
import kotlinx.android.synthetic.main.item_website.view.*

// TODO: 24.08.20 создать базовые классы адаптеров и listItem'ов

class SelectableItemsAdapter(
    val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<SelectableItemsAdapter.TextItemViewHolder>() {

    private val currentList = mutableListOf<String>()

    private var selectedPosition: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder =
        TextItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_website, parent, false
            )
        )

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        holder.bind(currentList[position], selectedPosition == position)
    }

    fun setWebsiteNames(items: List<String>) {
        selectedPosition = -1
        currentList.clear()
        currentList.addAll(items)
        notifyDataSetChanged()
    }

    private fun setSelectedPosition(position: Int) {
        if (position >= 0 && position != selectedPosition) {
            selectedPosition?.let(::notifyItemChanged)
            selectedPosition = position
            notifyItemChanged(selectedPosition!!)
        }
    }

    fun setSelectedByName(websiteName: String) {
        setSelectedPosition(currentList.indexOf(websiteName))
    }

    inner class TextItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(itemText: String, isSelected: Boolean) {
            with(view.websiteName) {
                text = itemText

                // TODO: 31.10.20 Сделать кастомную view для selectable item'a
                val colorId =
                    if (isSelected) R.color.colorWhite else R.color.colorPrimaryDark
                val backgroundId =
                    if (isSelected) R.drawable.ripple_dark_background else R.drawable.ripple_light_background

                setTextColor(ContextCompat.getColor(context, colorId))
                background = ContextCompat.getDrawable(context, backgroundId)

                setOnClickListener { onItemClick(itemText) }

            }
        }

    }
}