package com.elenaneacsu.bookfolio.utils.date

import java.text.SimpleDateFormat
import java.util.*

enum class Format(val format: String) {
    FORMAT1("MM/dd/yyyy HH:mm:ss"),
    FORMAT2("dd/MM/yyyy hh:mm:ss"),
    FORMAT3("hh:mm:ss dd/MM/yyyy"),
    FORMAT4("HH:mm:ss dd/MM/yyyy"),
    FORMAT5("HH:mm:ss"),
    FORMAT6("hh:mm:ss"),
    FORMAT7("HH:mm"),
    FORMAT8("dd/MM/yyyy"),
    FORMAT10("yyyy-MM-dd HH:mm:ss"),
    FORMAT9("dd MMM yyyy");
}

fun Date.dateFormattedToString(dateFormat: Format, locale: Locale = Locale.ENGLISH): String {
    val simpleDateFormat = SimpleDateFormat(dateFormat.format, locale)
    return simpleDateFormat.format(this)
}

fun String.stringToDate(dateFormat: Format, locale: Locale = Locale.ENGLISH): Date? {

    val simpleDateFormat = SimpleDateFormat(dateFormat.format, locale)
    return try {
        simpleDateFormat.parse(this)
    } catch (e: Exception) {
        e.printStackTrace()
        Date()
    }

}

fun Long.toStringDate(dateFormat: Format = Format.FORMAT8, locale: Locale = Locale.ENGLISH): String {
    val date = Date(this)
    val simpleDateFormat = SimpleDateFormat("dd/MM/yy")
    return simpleDateFormat.format(date)
}