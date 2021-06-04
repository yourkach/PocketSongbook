package com.example.pocketsongbook.feature.search

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ListPopupWindow
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.pocketsongbook.R
import com.example.pocketsongbook.common.BaseFragment
import com.example.pocketsongbook.common.navigation.toScreen
import com.example.pocketsongbook.databinding.FragmentSearchBinding
import com.example.pocketsongbook.domain.models.SongModel
import com.example.pocketsongbook.domain.search.SongsWebsite
import com.example.pocketsongbook.domain.search.toSongsWebsiteOrNull
import com.example.pocketsongbook.feature.favourites.FavouritesFragment
import com.example.pocketsongbook.feature.search.list.SongItemsAdapter
import com.example.pocketsongbook.feature.search.list.SuggestionsAdapter
import com.example.pocketsongbook.feature.song.SongFragment
import com.example.pocketsongbook.utils.SearchLayoutManager
import com.example.pocketsongbook.utils.hideKeyboard
import com.example.pocketsongbook.utils.isViewFocused
import com.example.pocketsongbook.utils.queryText
import moxy.ktx.moxyPresenter
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

// TODO: 18.07.20 сделать пагинацию для результатов поиска

class SearchFragment : BaseFragment(R.layout.fragment_search),
    SearchSongView {

    @Inject
    lateinit var searchPresenterProvider: Provider<SearchPresenter>

    private val presenter by moxyPresenter { searchPresenterProvider.get() }

    private val binding by viewBinding(FragmentSearchBinding::bind)

    private val searchItemsAdapter by lazy {
        SongItemsAdapter { item ->
            binding.songsSearchView.hideKeyboard()
            presenter.onSongClicked(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initSearchView()
        initWebsitesSelector()
    }

    override fun onBackPressed(): Boolean = with(binding.songsSearchView) {
        if (isViewFocused) {
            clearFocus()
            true
        } else false
    }

    private fun initRecyclerView() {
        binding.searchRv.apply {
            layoutManager = SearchLayoutManager(requireContext())
            adapter = searchItemsAdapter
        }
    }

    private val websitesListPopup by lazy { ListPopupWindow(requireContext()) }
    private fun initWebsitesSelector() {
        val websiteNames = SongsWebsite.values().map { it.websiteName }
        websitesListPopup.apply {
            anchorView = binding.tvSelectedWebsite
            ArrayAdapter(
                requireContext(),
                R.layout.item_website,
                websiteNames
            ).let(::setAdapter)
            setOnItemClickListener { _, _, position, _ ->
                websiteNames[position].toSongsWebsiteOrNull()?.let { website ->
                    presenter.onWebsiteSelected(website)
                }
                dismiss()
            }
        }
        binding.tvSelectedWebsite.setOnClickListener {
            websitesListPopup.show()
        }
    }

    override fun showSearchFailedError() {
        val error = getString(R.string.error_failed_to_get_search_results)
        showError(error)
    }

    override fun showInternetConnectionError() {
        val error = getString(R.string.error_no_connection)
        showError(error)
    }

    private val suggestionsAdapter by lazy {
        SuggestionsAdapter(
            onSuggestionClick = presenter::onSuggestionClick,
            onSuggestionDelete = presenter::onSuggestionDeleteClick
        )
    }

    private fun initSearchView() {
        binding.songsSearchView.apply {
            val id = context.resources.getIdentifier("android:id/search_src_text", null, null)
            findViewById<AutoCompleteTextView>(id).setTextColor(requireContext().getColor(R.color.colorPrimaryDark))
            setOnQueryTextFocusChangeListener { v, hasFocus ->
                presenter.onSearchFieldFocusChanged(hasFocus)
            }
            setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        val trimmedQuery = query.orEmpty().trim()
                        if (trimmedQuery.isNotEmpty()) {
                            presenter.onQueryTextSubmit(trimmedQuery)
                            binding.songsSearchView.hideKeyboard()
                        }
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        presenter.onQueryTextChange(
                            query = newText?.trim().orEmpty(),
                            isQueryFocused = binding.songsSearchView.focusedChild != null
                        )
                        return true
                    }
                }
            )
        }
        binding.rvSearchSuggestions.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = suggestionsAdapter
        }
    }

    override fun showLoading() {
        super.showLoading()
        binding.nothingFoundStub.isVisible = false
    }

    override fun toSongScreen(song: SongModel) {
        router.navigateTo(
            SongFragment.newInstance(song).toScreen()
        )
    }

    override fun toFavouritesScreen() {
        router.navigateTo(FavouritesFragment().toScreen())
    }

    override fun renderViewState(state: SearchViewState) {
        Timber.d("Render state: $state")
        renderQuerySuggestionsState(state.suggestionsState)
        renderSearchItemsState(state.searchItemsState)
        renderSelectedWebsite(state.selectedWebsite)
        renderQueryState(state.searchQueryState)
    }

    private fun renderQueryState(searchQueryState: SearchQueryState) {
        with(binding.songsSearchView) {
            isViewFocused = searchQueryState.isFocused
            queryText = searchQueryState.queryText
        }
    }

    private fun renderSelectedWebsite(website: SongsWebsite) {
        binding.tvSelectedWebsite.text = website.websiteName
    }

    private fun renderQuerySuggestionsState(suggestionsState: SuggestionsState) {
        suggestionsAdapter.submitList(suggestionsState.suggestionsList)
        binding.rvSearchSuggestions.isVisible = suggestionsState.isVisible
    }

    private fun renderSearchItemsState(state: SearchItemsState) {
        binding.searchRv.isNestedScrollingEnabled = false
        binding.nothingFoundStub.isVisible = false
        when (state) {
            is SearchItemsState.SearchResult.NothingFound -> {
                binding.nothingFoundStub.isVisible = true
                searchItemsAdapter.setLoadedSongs(listOf())
            }
            is SearchItemsState.SearchResult.Loading -> {
                setScrollingEnabled(false)
                searchItemsAdapter.setLoadingItemsList()
            }
            is SearchItemsState.Empty,
            is SearchItemsState.SearchResult.Failed -> {
                // TODO: 11.04.21 подумать об отображении ошибки
                searchItemsAdapter.setLoadedSongs(listOf())
            }
            is SearchItemsState.SearchResult.Loaded -> {
                binding.searchRv.isNestedScrollingEnabled = state.items.isNotEmpty()
                binding.nothingFoundStub.isVisible = false
                setScrollingEnabled(true)
                searchItemsAdapter.setLoadedSongs(state.items)
            }
        }
    }

    private fun setScrollingEnabled(isEnabled: Boolean) {
        (binding.searchRv.layoutManager as? SearchLayoutManager)?.isScrollingEnabled = isEnabled
    }

    override fun showFailedToLoadSongError() {
        val message = getString(R.string.error_failed_to_load_song)
        showError(message)
    }

}