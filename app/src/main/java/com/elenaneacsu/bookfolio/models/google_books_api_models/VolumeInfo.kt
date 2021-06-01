package com.elenaneacsu.bookfolio.models.google_books_api_models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Elena Neacsu on 14/05/21
 */
@Parcelize
data class VolumeInfo(
    val title: String? = null,
    val authors: List<String>? = null,
    val publishedDate: String? = null,
    val printType: String? = null,
    val pageCount: Int? = null,
    val imageLinks: ImageLinks? = null
) : Parcelable