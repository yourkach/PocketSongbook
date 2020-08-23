package com.example.pocketsongbook.ui.fragments.song

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.SeekBar
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pocketsongbook.R
import com.example.pocketsongbook.data.models.Chord
import com.example.pocketsongbook.data.models.Song
import com.example.pocketsongbook.ui.navigation.ArgsFragment
import com.example.pocketsongbook.ui.navigation.FragmentArgs
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_song.*
import kotlinx.coroutines.*
import moxy.ktx.moxyPresenter
import javax.inject.Inject

class SongFragment : ArgsFragment<SongFragment.SongArgs>(R.layout.fragment_song), SongView {


    @Inject
    lateinit var songPresenterFactory: SongPresenter.Factory

    private val presenter: SongPresenter by moxyPresenter {
        songPresenterFactory.create(args.song)
    }

    private val chordsAdapter by lazy { ChordsAdapter() }

    // TODO: 18.07.20 переделать на Handler и postDelayed
    private var scrollJob: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
//        song = SongFragmentArgs.fromBundle(requireArguments()).song
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        setUpChordsRecycler()
        setUpSeekBar()
    }

    /*
    override fun onCreate(savedInstanceState: Bundle?) {
        song = intent.getParcelableExtra(SONG_KEY)!!
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_view)
        setOnClickListeners()
        setUpChordsRecycler()
        setUpSeekBar()
    }
    */

    private fun setUpSeekBar() {
        songScrollSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                setAutoScrollSpeed(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                songScrollSb.alpha = 1.0f
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                songScrollSb.alpha = 0.3f
            }
        })
    }


    /**
     * [speed] must be in 0..30 range
     */


    fun setAutoScrollSpeed(speed: Int) {
        scrollJob?.cancel()
        if (speed != 0) {
            val newJob = CoroutineScope(Dispatchers.Main).launch {
                coroutineTimer(repeatMillis = (35 - speed).toLong(),
                    action = {
                        songScrollView.smoothScrollBy(0, 1)
                        songLinearLayout.measuredHeight > (songScrollView.scrollY + songScrollView.height)
                    }
                )
            }
            newJob.invokeOnCompletion {
                if (it !is CancellationException) songScrollSb.progress = 0
            }
            scrollJob = newJob
        }
    }

    private suspend inline fun coroutineTimer(
        repeatMillis: Long = 0,
        crossinline action: () -> Boolean
    ) {
        if (repeatMillis > 0) {
            while (action()) {
                delay(repeatMillis)
            }
        } else {
            action()
        }
    }

    private fun setUpChordsRecycler() {
        songChordsRv.apply {
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
            )
            adapter = chordsAdapter
        }
    }

    private fun setOnClickListeners() {
        songKeyUpBtn.setOnClickListener {
            presenter.onKeyUpClicked()
        }
        songKeyDownBtn.setOnClickListener {
            presenter.onKeyDownClicked()
        }
        songKeyAmountTv.setOnClickListener {
            presenter.onKeyLabelClicked()
        }
        songFontPlusBtn.setOnClickListener {
            presenter.onFontPlusClicked()
        }
        songFontMinusBtn.setOnClickListener {
            presenter.onFontMinusClicked()
        }
        songFontSizeTv.setOnClickListener {
            presenter.onFontLabelClicked()
        }
        songAddToFavouriteIv.setOnClickListener {
            presenter.onFavouritesButtonClicked()
        }
        songOpenChordsFb.setOnClickListener {
            presenter.onFloatingButtonClicked()
        }
        songScrollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY - oldScrollY < 0) {
                songOpenChordsFb.show()
            } else {
                songOpenChordsFb.hide()
            }
        }
    }

    override fun setKeyLabelText(text: String) {
        songKeyAmountTv.text = text
    }

    override fun setKeyLabelText(resId: Int) {
        songKeyAmountTv.text = getString(resId)
    }

    override fun setFontSizeLabelText(text: String) {
        songFontSizeTv.text = text
    }

    override fun setFontSizeLabelText(resId: Int) {
        songFontSizeTv.text = getString(resId)
    }

    override fun setArtistLabelText(text: String) {
        songArtistTv.text = text
    }

    override fun setTitleLabelText(text: String) {
        songTitleTv.text = text
    }

    override fun setSongLyrics(lyricsHtml: String) {
        songLyricsTv.text = HtmlCompat.fromHtml(lyricsHtml, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

    override fun setLyricsFontSize(fontSize: Float) {
        songLyricsTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
    }

    override fun setFavouritesButtonFilled(filled: Boolean) {
        if (filled) songAddToFavouriteIv.setImageResource(R.drawable.ic_star_white_36dp)
        else songAddToFavouriteIv.setImageResource(R.drawable.ic_star_border_white_36dp)
    }

    override fun loadChords(chords: List<Chord>) {
        chordsAdapter.setChords(chords)
    }

    override fun openChordBar() {
        songChordsCl.visibility = View.VISIBLE
    }

    override fun closeChordBar() {
        songChordsCl.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        scrollJob?.cancel("")
    }


    @Parcelize
    data class SongArgs(val song: Song) : FragmentArgs<SongFragment>

}