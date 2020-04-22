package com.example.pocketsongbook.ui.activity

import android.os.Bundle
import android.util.TypedValue
import androidx.core.text.HtmlCompat
import com.example.pocketsongbook.R
import com.example.pocketsongbook.data.Song
import com.example.pocketsongbook.ui.presenter.SongPresenter
import com.example.pocketsongbook.view.SongView
import kotlinx.android.synthetic.main.activity_song_view.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter

class SongViewActivity : MvpAppCompatActivity(), SongView {

    @InjectPresenter
    lateinit var presenter: SongPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_view)
        val song: Song = this.intent.getBundleExtra("bundle")?.getParcelable("song")!!
        presenter.setData(song)
        setOnClickListeners()
    }

    //TODO("adjustable auto scrolling in Scrollview ")
    //TODO("songs saving")

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
    }

    override fun setKeyLabelText(text: String, setDefault: Boolean) {
        songKeyAmountTv.text = if(setDefault) getString(R.string.song_key_label) else text
    }

    override fun setFontSizeLabelText(text: String, setDefault: Boolean) {
        songFontSizeTv.text = if(setDefault) getString(R.string.song_font_label) else text
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

}
