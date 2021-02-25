package com.example.pocketsongbook.domain.song

import com.example.pocketsongbook.domain.models.Chord

interface ChordsMapper {
    fun mapToChords(chordNames: List<String>): List<Chord>
}
