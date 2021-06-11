package com.example.pocketsongbook.domain.song.impl

import com.ybond.core_entities.models.Chord
import com.example.pocketsongbook.domain.song.ChordsMapper
import javax.inject.Inject

class ChordsMapperImpl @Inject constructor() : ChordsMapper {
    override fun mapToChords(chordNames: List<String>): List<Chord> {
        return chordPicturePatterns.flatMap { urlPattern ->
            chordNames.map { chordName ->
                Chord(
                    chordName = chordName,
                    imgUrl = urlPattern.buildUrl(chordName)
                )
            }
        }
    }

    companion object {
        private const val NAME_PLACEHOLDER = "%NAME%"
        private val chordPicturePatterns: List<ChordPictureUrlPattern> by lazy {
            listOf(
                ChordPictureUrlPattern(
                    baseUrl = "https://amdm.ru/images/chords/${NAME_PLACEHOLDER}_0.gif",
                    nameSharpReplacement = "w"
                ),
                ChordPictureUrlPattern(
                    baseUrl = "https://hm6.ru/i/img/akkords/${NAME_PLACEHOLDER}.png?ch201",
                    nameSharpReplacement = "sharp"
                )
            )
        }
    }

    /**
     * Pattern for building chord picture url
     * [baseUrl] should contain [NAME_PLACEHOLDER], which will be replaced with chord name
     */
    private class ChordPictureUrlPattern(
        val baseUrl: String,
        val nameSharpReplacement: String
    ) {
        fun buildUrl(chordName: String): String {
            val formattedName = chordName.replace("#", nameSharpReplacement)
            return baseUrl.replace(NAME_PLACEHOLDER, formattedName)
        }
    }
}