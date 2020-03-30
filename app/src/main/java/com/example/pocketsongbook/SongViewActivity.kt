package com.example.pocketsongbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pocketsongbook.data_classes.SongText
import kotlinx.android.synthetic.main.activity_song_view.*

class SongViewActivity : AppCompatActivity() {
    private val songLines: ArrayList<Pair<String, Boolean>> = ArrayList()
    private lateinit var songTextAdapter: SongTextRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_view)
        val intent = this.intent
        var songText = ""
        intent.getStringArrayListExtra("text")
            .forEach { line -> songLines.add(line to ChordsTransponder.isChordLine(line)) }
        songLines.forEach { s -> songText += "${s.first}\n" }

        val song = SongText(
            intent.getStringExtra("artist"),
            intent.getStringExtra("title"),
            songText
        )
        artistTextView.text = song.artist
        titleTextView.text = song.songTitle
        initRecyclerView()
    }

    private fun initRecyclerView() {
        songRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@SongViewActivity)
            songTextAdapter = SongTextRecyclerAdapter()
            adapter = songTextAdapter
        }
        songTextAdapter.submitList(songLines)
    }
}
