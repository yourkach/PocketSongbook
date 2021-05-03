package com.example.pocketsongbook.utils

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router

interface GlobalCiceroneHolder {
    val globalCicerone: Cicerone<Router>
}