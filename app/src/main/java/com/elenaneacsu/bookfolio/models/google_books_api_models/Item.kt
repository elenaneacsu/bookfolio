package com.elenaneacsu.bookfolio.models.google_books_api_models

/**
 * Created by Elena Neacsu on 14/05/21
 */
data class Item(
    val kind: String? = null,
    val id: String? = null,
    val volumeInfo: VolumeInfo? = null,
)