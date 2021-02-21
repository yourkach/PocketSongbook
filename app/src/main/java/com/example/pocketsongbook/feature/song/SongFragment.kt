package com.example.pocketsongbook.feature.song

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.SeekBar
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pocketsongbook.R
import com.example.pocketsongbook.common.extensions.setAndCancelJob
import com.example.pocketsongbook.common.navigation.ArgsFragment
import com.example.pocketsongbook.common.navigation.FragmentArgs
import com.example.pocketsongbook.data.models.Chord
import com.example.pocketsongbook.data.models.SongModel
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_song.*
import kotlinx.coroutines.*
import moxy.ktx.moxyPresenter
import javax.inject.Inject

// TODO: 14.02.21 загружать песню на этом экране, показывать шиммер
class SongFragment : ArgsFragment<SongFragment.SongArgs>(R.layout.fragment_song), SongView {


    @Inject
    lateinit var songPresenterFactory: SongPresenter.Factory

    private val presenter: SongPresenter by moxyPresenter {
        songPresenterFactory.create(args.song)
    }

    override val hideBottomNavigationBar: Boolean = true

    private val chordsAdapter by lazy { ChordsAdapter() }

    override val enterTransitionRes: Int = R.transition.slide_right

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        setUpChordsRecycler()
        setUpSeekBar()
    }

    private fun setUpSeekBar() {
        songScrollSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                songScrollSb.animate().apply {
                    alpha(1.0f)
                    duration = 200
                    start()
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.progress?.let(::setAutoScrollSpeed)
                songScrollSb.animate().apply {
                    alpha(0.3f)
                    duration = 200
                    start()
                }
            }
        })
    }


    private var scrollJob: Job? by setAndCancelJob()

    /**
     * [speed] must be in 0..30 range
     */
    fun setAutoScrollSpeed(speed: Int) {
        if (speed != 0) {
            scrollJob = CoroutineScope(Dispatchers.Main).launch {
                coroutineTimer(repeatMillis = (35 - speed).toLong()) {
                    svSongLyrics.smoothScrollBy(0, 1)
                    songLinearLayout.measuredHeight > (svSongLyrics.scrollY + svSongLyrics.height)
                }
            }.apply {
                invokeOnCompletion {
                    if (it !is CancellationException) songScrollSb.progress = 0
                }
            }
        } else {
            scrollJob?.cancel()
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
            presenter.onKeyPlusClick()
        }
        songKeyDownBtn.setOnClickListener {
            presenter.onKeyMinusClick()
        }
        songKeyAmountTv.setOnClickListener {
            presenter.onKeyLabelClick()
        }
        songFontPlusBtn.setOnClickListener {
            presenter.onFontPlusClick()
        }
        songFontMinusBtn.setOnClickListener {
            presenter.onFontMinusClick()
        }
        songFontSizeTv.setOnClickListener {
            presenter.onFontLabelClick()
        }
        songAddToFavouriteIv.setOnClickListener {
            presenter.onFavouritesButtonClick()
        }
        songOpenChordsFb.setOnClickListener {
            presenter.onOpenChordsClick()
        }
        svSongLyrics.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY - oldScrollY < 0) {
                songOpenChordsFb.show()
            } else {
                songOpenChordsFb.hide()
            }
        }
    }

    override fun setSongInfo(artist: String, title: String) {
        tvSongArtist.text = artist
        tvSongTitle.text = title
    }

    override fun setKeyText(chordsKeyText: String, isDefault: Boolean) {
        songKeyAmountTv.text =
            if (isDefault) getString(R.string.song_key_default) else chordsKeyText
    }

    override fun updateLyricsFontSize(newSize: Int, isDefault: Boolean) {
        songFontSizeTv.text =
            if (isDefault) getString(R.string.song_font_default) else newSize.toString()
        songLyricsTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, newSize.toFloat())
    }

    override fun setSongLyrics(lyricsHtml: String) {
        songLyricsTv.text = HtmlCompat.fromHtml(lyricsHtml, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

    override fun setFavouritesButtonFilled(filled: Boolean) {
        if (filled) songAddToFavouriteIv.setImageResource(R.drawable.ic_star_white_36dp)
        else songAddToFavouriteIv.setImageResource(R.drawable.ic_star_border_white_36dp)
    }

    override fun setChords(chords: List<Chord>) {
        chordsAdapter.setChords(chords)
    }

    override fun openChordBar() {
        songChordsCl.visibility = View.VISIBLE
    }

    override fun closeChordBar() {
        songChordsCl.visibility = View.GONE
    }

    override fun onDestroy() {
        scrollJob?.cancel()
        super.onDestroy()
    }

    @Parcelize
    data class SongArgs(val song: SongModel) : FragmentArgs<SongFragment>

}