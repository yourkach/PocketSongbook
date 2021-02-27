package com.example.pocketsongbook.domain.song

interface DefaultsProvider {

    fun getKeyDefaults(): KeyChangeDefaults

    fun getFontDefaults(): FontChangeDefaults

}

data class KeyChangeDefaults(
    val chordsKeyMax: Int,
    val chordsKeyMin: Int,
    val chordsKeyDefault: Int,
)

data class FontChangeDefaults(
    val minFontSize: Int,
    val maxFontSize: Int,
    val defaultFontSize: Int,
    val fontSizeChangeAmount: Int,
)
