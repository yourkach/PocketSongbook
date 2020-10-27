package com.example.pocketsongbook.feature.search.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketsongbook.R
import kotlinx.android.synthetic.main.item_website.view.*

// TODO: 24.08.20 создать базовые классы адаптеров и listItem'ов

class WebsitesAdapter(
    val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<WebsitesAdapter.WebsiteViewHolder>() {

    private val currentList = mutableListOf<String>()

    private var selectedPosition: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebsiteViewHolder =
        WebsiteViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_website, parent, false
            )
        )

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: WebsiteViewHolder, position: Int) {
        holder.bind(currentList[position], selectedPosition == position)
    }

    fun setWebsiteNames(items: List<String>, selectedWebsitePosition: Int) {
        selectedPosition = selectedWebsitePosition
        currentList.clear()
        currentList.addAll(items)
        notifyDataSetChanged()
    }

    fun setSelectedPosition(position: Int) {
        if (position != selectedPosition) {
            selectedPosition?.let(::notifyItemChanged)
            selectedPosition = position
            notifyItemChanged(selectedPosition!!)
        }
    }

    inner class WebsiteViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(websiteItemText: String, isSelected: Boolean) {
            with(view.websiteName) {
                text = websiteItemText

                val colorId =
                    if (isSelected) R.color.colorWhite else R.color.colorPrimaryDark
                val backgroundId =
                    if (isSelected) R.drawable.ripple_dark_background else R.drawable.ripple_light_background

                setTextColor(ContextCompat.getColor(context, colorId))
                background = ContextCompat.getDrawable(context, backgroundId)

                setOnClickListener { onItemClick(adapterPosition) }

            }
        }

    }
}