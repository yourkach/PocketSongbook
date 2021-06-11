package com.example.pocketsongbook.domain.song

import com.ybond.core_entities.models.FontChangeDefaults
import com.ybond.core_entities.models.KeyChangeDefaults

interface DefaultsProvider {

    fun getKeyDefaults(): KeyChangeDefaults

    fun getFontDefaults(): FontChangeDefaults

}

