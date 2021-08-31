package com.elenaneacsu.bookfolio.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * Created by Elena Neacsu on 30/08/2021
 */
@Entity(
    tableName = "quotes",
    indices = [Index(value = ["id"], unique = true)]
)
@Parcelize
data class QuoteRoomEntity(
    @PrimaryKey
    var id: String,
    var text: String? = null,
    var book: String? = null,
    var author: String? = null,
    var page: String? = null,
    var date: Long? = null,

    var shelfName: String? = null,
    var bookId: String? = null
) : Parcelable