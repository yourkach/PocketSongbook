package com.example.pocketsongbook.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

private val defaultFormat: DateFormat by lazy {
    SimpleDateFormat("HH:mm dd.MM.yy", Locale.getDefault())
}

fun Date.formatByDefault(): String = defaultFormat.format(this)

fun Long.utcMillisToLocalDate(): Date {
    return Calendar.getInstance()
        .apply {
            timeZone = TimeZone.getDefault()
            timeInMillis = this@utcMillisToLocalDate
        }
        .time
}