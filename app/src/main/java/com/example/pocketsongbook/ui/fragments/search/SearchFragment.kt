package com.example.pocketsongbook.ui.fragments.search

import android.os.Bundle
import android.view.View
import android.widget.*
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
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.flow.Flow
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class SearchFragment : MvpAppCompatFragment(R.layout.fragment_search),
    SearchSongView,
    AdapterView.OnItemSelectedListener {

    @Inject
    lateinit var searchPresenter: SearchPresenter

    private val presenter by moxyPresenter { searchPresenter }

    private lateinit var searchItemsAdapter: SearchAdapter

    // TODO: 08.07.20 сделать реализацию поиска с debounce на корутинах вместо RxJava
//    private lateinit var searchFlow: Flow<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        setUpSpinner()
        setUpSearchView()

        searchOpenFavouritesIv.setOnClickListener {
            presenter.onFavouritesClicked()
        }
    }

    private fun setUpSearchView() {
        //TODO сделать не черный текст в searchView
        searchViewMain.apply {
            val id = context.resources.getIdentifier("android:id/search_src_text", null, null)
            findViewById<AutoCompleteTextView>(id).setTextColor(requireContext().getColor(R.color.colorPrimaryDark))
        }
        Observable.create<String> { emitter ->
            searchViewMain.setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (!query.isNullOrEmpty()) {
                            emitter.onNext(query)
                            cleanSearchBarFocus()
                        }
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (!newText.isNullOrEmpty()) {
                            emitter.onNext(newText)
                        }
                        return true
                    }
                }
            )
        }
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

    private fun setUpSpinner() {
        searchWebsiteSelector.apply {
            adapter = ArrayAdapter(
                context,
                R.layout.spinner_item, presenter.getSpinnerItems()
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
        searchItemsAdapter.apply {
            setList(newItems)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) = Unit

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        presenter.onSpinnerItemSelected(position)
    }


}