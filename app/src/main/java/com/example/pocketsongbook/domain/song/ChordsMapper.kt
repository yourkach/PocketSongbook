package com.example.pocketsongbook.domain.song

import com.ybond.core_entities.models.Chord

interface ChordsMapper {
    fun mapToChords(chordNames: List<String>): List<Chord>
}
