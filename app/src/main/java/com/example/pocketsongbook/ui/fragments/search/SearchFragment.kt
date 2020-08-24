package com.example.pocketsongbook.ui.fragments.search

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pocketsongbook.R
import com.example.pocketsongbook.data.models.Song
import com.example.pocketsongbook.data.models.SongSearchItem
import com.example.pocketsongbook.ui.fragments.favourites.FavouritesFragment
import com.example.pocketsongbook.ui.fragments.search.list.SearchAdapter
import com.example.pocketsongbook.ui.fragments.search.list.WebsitesAdapter
import com.example.pocketsongbook.ui.fragments.song.SongFragment
import com.example.pocketsongbook.ui.navigation.*
import dagger.android.support.AndroidSupportInjection
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_search.*
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import ru.terrakok.cicerone.Router
import java.util.concurrent.TimeUnit
import javax.inject.Inject

// TODO: 18.07.20 сделать пагинацию для результатов поиска
// TODO: 18.07.20 добавить поле для отображения количества найденных песен
// TODO: 18.07.20 сделать FavouriteSongsRepository, хранить Url'ы в префсах для быстрой проверки
class SearchFragment : MvpAppCompatFragment(R.layout.fragment_search),
    SearchSongView, AdapterView.OnItemSelectedListener {

    @Inject
    lateinit var searchPresenter: SearchPresenter

    @Inject
    lateinit var router: Router

    private val presenter by moxyPresenter { searchPresenter }

    private lateinit var searchItemsAdapter: SearchAdapter

    private val websitesAdapter = WebsitesAdapter { clickedPosition ->
        presenter.onWebsiteItemSelected(clickedPosition)
    }

    // TODO: 08.07.20 сделать реализацию поиска с debounce на Coroutines Flow вместо RxJava
    private val searchQuerySubject = PublishSubject.create<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initSearchView()
        initWebsiteSelector()

        searchOpenFavouritesIv.setOnClickListener {
            presenter.onFavouritesClicked()
        }
    }

    private fun initSearchView() {
        searchViewMain.apply {
            val id = context.resources.getIdentifier("android:id/search_src_text", null, null)
            findViewById<AutoCompleteTextView>(id).setTextColor(requireContext().getColor(R.color.colorPrimaryDark))
            setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (!query.isNullOrEmpty()) {
                            searchQuerySubject.onNext(query)
                            cleanSearchBarFocus()
                        }
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (!newText.isNullOrEmpty()) {
                            searchQuerySubject.onNext(newText)
                        }
                        return true
                    }
                }
            )
        }

        searchQuerySubject
            .distinctUntilChanged()
            .filter { !it.isNullOrEmpty() }
            .debounce(700, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .subscribe { query ->
                presenter.onQueryTextSubmit(query)
            }
    }

    override fun showToast(messageId: Int) {
        Toast.makeText(context, getString(messageId), Toast.LENGTH_SHORT).show()
    }

    override fun navigateToSongView(song: Song) {
        val fragment = SongFragment.SongArgs(song).toFragment()

        router.navigateToFragment(fragment)
    }

    override fun navigateToFavourites() {
        router.navigateToFragment(FavouritesFragment())
    }


    private fun initRecyclerView() {
        searchRv.apply {
            layoutManager = LinearLayoutManager(context)
            searchItemsAdapter =
                SearchAdapter { position ->
                    presenter.onSongClicked(position)
                }
            adapter = searchItemsAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun initWebsiteSelector() {
        websiteSelectorContainer.outlineProvider = null
        searchWebsiteSelector.apply {
            adapter = websitesAdapter
        }
    }

    override fun setWebsites(websiteNames: List<String>, selectedWebsitePosition: Int) {
        websitesAdapter.setWebsiteNames(websiteNames, selectedWebsitePosition)
    }

    override fun setWebsiteSelected(selectedWebsitePosition: Int) {
        websitesAdapter.setSelectedPosition(selectedWebsitePosition)
    }

    private fun cleanSearchBarFocus() {
        searchViewMain.clearFocus()
    }

    override fun showLoadingPanel(visible: Boolean) {
        if (visible) {
            searchLoadingPanel.visibility = View.VISIBLE
        } else {
            searchLoadingPanel.visibility = View.GONE
        }
    }

    override fun updateRecyclerItems(newItems: List<SongSearchItem>) {
        searchNothingFoundLabel.isVisible = newItems.isEmpty()
        searchItemsAdapter.apply {
            setList(newItems)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) = Unit

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        presenter.onWebsiteItemSelected(position)
    }

}