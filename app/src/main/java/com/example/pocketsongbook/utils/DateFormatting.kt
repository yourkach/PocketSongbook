package com.example.pocketsongbook.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

val dateFormat: DateFormat
    get() = SimpleDateFormat("HH:mm dd.MM.yy", Locale.getDefault())

fun Date.formatByDefault(): String {
    return dateFormat.format(this)
}

fun Long.millisToLocalDate(): Date {
    return Calendar.getInstance()
        .apply {
            timeZone = TimeZone.getDefault()
            timeInMillis = this@millisToLocalDate
        }
        .time
}