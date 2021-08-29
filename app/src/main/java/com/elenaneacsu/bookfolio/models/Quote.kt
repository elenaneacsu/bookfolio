package com.elenaneacsu.bookfolio.models

import android.os.Parcelable
import com.elenaneacsu.bookfolio.utils.date.toStringDate
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Created by Elena Neacsu on 18/06/21
 */
@Parcelize
data class Quote(
    val id: String = UUID.randomUUID().toString(),
    var text: String? = null,
    var book: String? = null,
    var author: String? = null,
    var page: String? = null,
    var date: Long? = null
) : Parcelable {
    fun getFormattedDate() = date?.toStringDate()
}