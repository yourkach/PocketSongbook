package com.example.pocketsongbook.feature.song

import android.os.Bundle
import android.os.Parcelable
import android.util.TypedValue
import android.view.View
import android.widget.SeekBar
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.pocketsongbook.R
import com.example.pocketsongbook.common.BaseFragment
import com.example.pocketsongbook.common.extensions.setAndCancelJob
import com.example.pocketsongbook.common.navigation.ParcelableArgument
import com.example.pocketsongbook.databinding.FragmentSongBinding
import com.example.pocketsongbook.domain.models.Chord
import com.example.pocketsongbook.domain.models.SongModel
import com.example.pocketsongbook.domain.song.models.ChordsKey
import com.example.pocketsongbook.domain.song.models.FontSize
import com.example.pocketsongbook.feature.song.mvi.state_models.*
import kotlinx.coroutines.*
import kotlinx.parcelize.Parcelize
import moxy.ktx.moxyPresenter
import javax.inject.Inject

// TODO: 14.02.21 загружать песню на этом экране, показывать шиммер
class SongFragment : BaseFragment(R.layout.fragment_song), SongView {

    private var args: Arguments by ParcelableArgument("song_args")

    @Inject
    lateinit var songPresenterFactory: SongPresenter.Factory

    private val presenter: SongPresenter by moxyPresenter {
        songPresenterFactory.create(args.song)
    }

    override val hideBottomNavigationBar: Boolean = true

    private val chordsAdapter by lazy { ChordsAdapter() }

    private val binding: FragmentSongBinding by viewBinding(FragmentSongBinding::bind)

    override val enterTransitionRes: Int = R.transition.slide_right

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        setUpChordsRecycler()
        setUpSeekBar()
    }

    override fun onPause() {
        super.onPause()
        presenter.onViewPaused()

    }

    private fun setUpSeekBar() {
        binding.songScrollSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                binding.songScrollSb.animate().apply {
                    alpha(1.0f)
                    duration = 200
                    start()
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.progress?.let(::setAutoScrollSpeed)
                binding.songScrollSb.animate().apply {
                    alpha(0.3f)
                    duration = 200
                    start()
                }
            }
        })
    }

    private var currentState: SongScreenState? = null
    override fun applyState(state: SongScreenState) {
        if (state != currentState) {
            bindSongState(state.songState)
            bindChordsBarState(state.chordsBarState)
        }
    }

    private fun bindSongState(songState: SongViewStateModel) {
        if (songState is SongViewStateModel.Loaded && songState != currentState?.songState) {
            bindKeyOption(songState.optionsState.chordsOption)
            bindFontSizeOption(songState.optionsState.fontOption)
            bindSongInfo(
                artist = songState.songArtist,
                title = songState.songTitle,
                lyricsHtml = songState.formattedLyricsHtml
            )
            bindFavoriteButton(songState.isFavorite)
        }
    }

    private fun bindChordsBarState(chordsBarState: ChordBarViewStateModel) {
        setChordsBarOpened(chordsBarState.isOpened)
        setChordsBarItems(chordsBarState.chords)
    }

    private var scrollJob: Job? by setAndCancelJob()

    /**
     * [speed] must be in 0..30 range
     */
    fun setAutoScrollSpeed(speed: Int) {
        with(binding) {
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
        binding.songChordsRv.apply {
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false
            )
            adapter = chordsAdapter
        }
    }

    private fun setOnClickListeners() {
        binding.songKeyUpBtn.setOnClickListener {
            presenter.onChordsKeyChangeInteraction(ChangeType.Increment)
        }
        binding.songKeyDownBtn.setOnClickListener {
            presenter.onChordsKeyChangeInteraction(ChangeType.Decrement)
        }
        binding.songKeyAmountTv.setOnClickListener {
            presenter.onChordsKeyChangeInteraction(ChangeType.SetDefault)
        }
        binding.songFontPlusBtn.setOnClickListener {
            presenter.onFontSizeChangeInteraction(ChangeType.Increment)
        }
        binding.songFontMinusBtn.setOnClickListener {
            presenter.onFontSizeChangeInteraction(ChangeType.Decrement)
        }
        binding.songFontSizeTv.setOnClickListener {
            presenter.onFontSizeChangeInteraction(ChangeType.SetDefault)
        }
        binding.songOpenChordsFb.setOnClickListener {
            presenter.onChordsBarButtonClick()
        }
        binding.songAddToFavouriteIv.setOnClickListener {
            presenter.onSongToggleFavoriteClicked()
        }
        binding.svSongLyrics.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY - oldScrollY < 0) {
                binding.songOpenChordsFb.show()
            } else {
                binding.songOpenChordsFb.hide()
            }
        }
    }

    private fun bindSongInfo(artist: String, title: String, lyricsHtml: String) {
        with(binding) {
            tvSongArtist.text = artist
            tvSongTitle.text = title
            songLyricsTv.text = HtmlCompat.fromHtml(lyricsHtml, HtmlCompat.FROM_HTML_MODE_COMPACT)
        }
    }

    private var currentKeyOption: ChangeableOption<ChordsKey>? = null
    private fun bindKeyOption(keyOption: ChangeableOption<ChordsKey>) {
        if (currentKeyOption == keyOption) return
        binding.songKeyAmountTv.text = when {
            keyOption.isDefault -> getString(R.string.song_key_default)
            else -> keyOption.selectedValue.key.toString()
        }
        currentKeyOption = keyOption
    }

    private var currentFontSizeOption: ChangeableOption<FontSize>? = null
    private fun bindFontSizeOption(sizeOption: ChangeableOption<FontSize>) {
        if (currentFontSizeOption == sizeOption) return
        binding.songFontSizeTv.text = when {
            sizeOption.isDefault -> getString(R.string.song_font_default)
            else -> sizeOption.selectedValue.size.toString()
        }
        binding.songLyricsTv.setTextSize(
            TypedValue.COMPLEX_UNIT_SP,
            sizeOption.selectedValue.size.toFloat()
        )
        currentFontSizeOption = sizeOption
    }

    private fun bindFavoriteButton(isFavorite: Boolean) {
        val resource = when (isFavorite) {
            true -> R.drawable.ic_star_white_36dp
            else -> R.drawable.ic_star_border_white_36dp
        }
        binding.songAddToFavouriteIv.setImageResource(resource)
    }

    private fun setChordsBarItems(chords: List<Chord>) {
        chordsAdapter.setChords(chords)
    }

    private fun setChordsBarOpened(isVisible: Boolean) {
        binding.songChordsBarFl.isVisible = isVisible
    }

    override fun onDestroy() {
        scrollJob?.cancel()
        super.onDestroy()
    }

    @Parcelize
    data class Arguments(val song: SongModel) : Parcelable

    companion object {
        fun newInstance(song:SongModel) : SongFragment {
            return SongFragment().apply {
                args = Arguments(song)
            }
        }
    }


}