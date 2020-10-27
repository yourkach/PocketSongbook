package com.example.pocketsongbook.feature.song.usecase

import com.example.pocketsongbook.common.BaseUseCase
import javax.inject.Inject

class ChangeFontSizeUseCase @Inject constructor() :
    BaseUseCase<ChangeFontSizeUseCase.Param, Int>() {

    override suspend fun execute(param: Param): Int {
        TODO("Not yet implemented")
    }

    sealed class Param {

    }

}
