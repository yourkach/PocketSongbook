package com.example.pocketsongbook.utils

import java.util.*
import java.util.regex.Pattern

// TODO: 31.10.20 потестить, использовать если работает

object ChordsTransponderKotlin {
    private val scale = arrayOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")
    private val scaleIndex: MutableMap<String, Int?> = HashMap()

    init {
        val normalize = arrayOf("Cb","B","Db","C#","Eb","D#","Fb","E","Gb","F#","Ab","G#","Bb","A#","E#","F","B#","C")
        for (i in scale.indices) scaleIndex[scale[i]] = i
        var i = 0
        while (i < normalize.size) {
            scaleIndex[normalize[i]] = scaleIndex[normalize[i + 1]]
            i += 2
        }
    }

    fun transposeChord(chordRegel: String, amount: Int): String {
        val normalizedAmount = (amount % scale.size + scale.size) % scale.size
        val buf = StringBuffer()
        val m = Pattern.compile("[CDEFGAB][b#]?").matcher(chordRegel)
        while (m.find()) m.appendReplacement(
            buf,
            scale[(scaleIndex[m.group()]!! + normalizedAmount) % scale.size]
        )
        return m.appendTail(buf).toString()
    }
}