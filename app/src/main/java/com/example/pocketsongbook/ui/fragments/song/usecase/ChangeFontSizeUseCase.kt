package com.example.pocketsongbook.ui.fragments.song.usecase

import com.example.pocketsongbook.data.BaseUseCase
import javax.inject.Inject

class ChangeFontSizeUseCase @Inject constructor() :
    BaseUseCase<ChangeFontSizeUseCase.Param, Int>() {

    override suspend operator fun invoke(param: Param): Int {
        TODO("Not yet implemented")
    }

    sealed class Param {

    }

}
