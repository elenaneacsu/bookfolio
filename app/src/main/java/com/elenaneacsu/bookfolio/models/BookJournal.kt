package com.elenaneacsu.bookfolio.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Elena Neacsu on 21/08/2021
 */
@Parcelize
data class BookJournal(val quotes: List<Quote>? = null) : Parcelable