package com.example.pocketsongbook.ui.fragments.search

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pocketsongbook.App
import com.example.pocketsongbook.R
import com.example.pocketsongbook.data.models.Song
import com.example.pocketsongbook.data.models.SongSearchItem
import com.example.pocketsongbook.ui.adapter.SearchAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.flow.Flow
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import java.util.concurrent.TimeUnit
import javax.inject.Inject

// TODO: 18.07.20 сделать пагинацию для результатов поиска
// TODO: 18.07.20 добавить поле для отображения количества найденных песен
// TODO: 18.07.20 сделать FavouriteSongsRepository, хранить Url'ы в префсах для быстрой проверки

class SearchFragment : MvpAppCompatFragment(R.layout.fragment_search),
    SearchSongView,
    AdapterView.OnItemSelectedListener {

    @Inject
    lateinit var searchPresenter: SearchPresenter

    private val presenter by moxyPresenter { searchPresenter }

    private lateinit var searchItemsAdapter: SearchAdapter

    // TODO: 08.07.20 сделать реализацию поиска с debounce на Coroutines Flow вместо RxJava
    private val searchQuerySubject = PublishSubject.create<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        setUpSearchView()

        searchOpenFavouritesIv.setOnClickListener {
            presenter.onFavouritesClicked()
        }
    }

    private fun setUpSearchView() {
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
            .debounce(700, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .subscribe { query ->
                presenter.onQueryTextSubmit(query)
            }
    }

    override fun showToast(messageId: Int) {
        Toast.makeText(context, getString(messageId), Toast.LENGTH_SHORT).show()
    }

    override fun navigateToSongView(song: Song) {
        findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToSongFragment(song)
        )
    }

    override fun navigateToFavourites() {
        findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToFavouritesFragment()
        )
    }


    private fun setUpRecyclerView() {
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

    override fun setSpinnerItems(spinnerItems: List<String>) {
        searchWebsiteSelector.apply {
            adapter = ArrayAdapter(
                context,
                R.layout.spinner_item, spinnerItems
            )
            onItemSelectedListener = this@SearchFragment
        }
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
        presenter.onSpinnerItemSelected(position)
    }


}