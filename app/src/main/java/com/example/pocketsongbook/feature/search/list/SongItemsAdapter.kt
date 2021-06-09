package com.example.pocketsongbook.feature.search.list

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketsongbook.R
import com.example.pocketsongbook.databinding.ItemSearchSongBinding
import com.example.pocketsongbook.databinding.ItemSearchSongSkeletonBinding
import com.ybond.core.models.FoundSongModel
import com.example.pocketsongbook.utils.EqualsDiffCallback
import com.example.pocketsongbook.utils.inflate
import com.example.pocketsongbook.utils.setOnSafeClickListener

class SongItemsAdapter(
    private val onItemClick: (searchItem: FoundSongModel) -> Unit
) : ListAdapter<SearchItem, SongItemsAdapter.ViewHolder>(
    EqualsDiffCallback<SearchItem> { a, b ->
        a is SearchItem.LoadedItem && b is SearchItem.LoadedItem
                && a.song.url == b.song.url
    }
) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SearchItem.SkeletonItem -> SKELETON_ITEM_TYPE
            else -> LOADED_ITEM_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType == SKELETON_ITEM_TYPE) {
            true -> {
                SkeletonHolder(parent.inflate(R.layout.item_search_song_skeleton))
            }
            else -> {
                ItemHolder(parent.inflate(R.layout.item_search_song))
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is SkeletonHolder -> {
                holder.bind()
            }
            is ItemHolder -> {
                val song = (getItem(position) as SearchItem.LoadedItem).song
                holder.bind(song)
            }
        }
    }

    fun setLoadingItemsList() {
        submitList(loadingItems)
    }

    fun setLoadedSongs(items: List<FoundSongModel>) {
        submitList(items.map(SearchItem::LoadedItem))
    }

    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class SkeletonHolder(itemView: View) : ViewHolder(itemView) {
        private val binding = ItemSearchSongSkeletonBinding.bind(itemView)
        fun bind() = binding.itemShimmerLayout.startShimmerAnimation()
    }

    inner class ItemHolder(itemView: View) : ViewHolder(itemView) {
        private val binding = ItemSearchSongBinding.bind(itemView)

        fun bind(searchItem: FoundSongModel) {
            binding.apply {
                tvSongArtist.text = searchItem.artist
                tvSongTitle.text = searchItem.title
                songFavoriteIv.isVisible = searchItem.isFavourite
                clItemContainer.setOnSafeClickListener {
                    onItemClick(searchItem)
                }
            }
        }
    }

    companion object {
        private const val SKELETON_ITEM_TYPE = 0
        private const val LOADED_ITEM_TYPE = 1

        private val loadingItems: List<SearchItem.SkeletonItem> by lazy {
            mutableListOf<SearchItem.SkeletonItem>().apply {
                repeat(15) { add(SearchItem.SkeletonItem) }
            }
        }
    }

}

sealed class SearchItem {
    data class LoadedItem(val song: FoundSongModel) : SearchItem()
    object SkeletonItem : SearchItem()
}

