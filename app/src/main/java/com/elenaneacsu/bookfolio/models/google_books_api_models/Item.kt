package com.elenaneacsu.bookfolio.models.google_books_api_models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Elena Neacsu on 01/06/21
 */
@Parcelize
data class Item(
    val id: String? = null,
    val volumeInfo: VolumeInfo? = null
): Parcelable