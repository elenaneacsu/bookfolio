package com.elenaneacsu.bookfolio.models

import android.os.Parcelable
import com.elenaneacsu.bookfolio.models.google_books_api_models.Item
import kotlinx.android.parcel.Parcelize

/**
 * Created by Elena Neacsu on 18/06/21
 */
@Parcelize
data class UserBook(
    var item: Item? = null,
    var startDate: String? = null,
    var endDate: String? = null,
    var rating: Int? = null,
    var quotes: List<Quote>? = null
): Parcelable