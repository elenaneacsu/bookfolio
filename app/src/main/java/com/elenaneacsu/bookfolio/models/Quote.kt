package com.elenaneacsu.bookfolio.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Elena Neacsu on 18/06/21
 */
@Parcelize
data class Quote(
    var text: String? = null,
    var book: String? = null,
    var author: String? = null,
    var page: String? = null,
    var date: String? = null
): Parcelable