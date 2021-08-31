package com.elenaneacsu.bookfolio.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * Created by Elena Neacsu on 08/05/21
 */
@Parcelize
data class Shelf(
    var name: String? = null,
    var numberOfBooks: Int? = null
) : Parcelable

enum class ShelfType(val valueAsString: String) {
    CURRENTLY_READING("Currently reading"),
    TO_READ("To read"),
    READ("Read");

    companion object {
        fun getShelfType(value: String) = values().firstOrNull { it.valueAsString == value }
    }
}

fun Shelf.getShelfTypeName() = name?.let { shelfName ->
    ShelfType.getShelfType(
        shelfName
    )?.valueAsString
}