package com.elenaneacsu.bookfolio.models.google_books_api_models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Elena Neacsu on 15/05/21
 */
@Parcelize
data class ImageLinks(
    val smallThumbnail: String? = null,
    val thumbnail: String? = null
) : Parcelable