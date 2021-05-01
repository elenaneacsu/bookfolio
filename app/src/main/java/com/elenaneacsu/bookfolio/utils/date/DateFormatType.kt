package com.elenaneacsu.bookfolio.utils.date

import androidx.annotation.StringDef

@StringDef(
    DateFormatType.DATE,
    DateFormatType.DATE_TIME
)
annotation class DateFormatType {
    companion object {
        const val DATE = "dd.MM.yyyy"
        const val DATE_TIME = "dd.MM.yyyy HH:mm"
        const val DATE_TIME_FULL = "yyyy-MM-dd HH:mm:ss"
    }
}