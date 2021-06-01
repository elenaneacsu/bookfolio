package com.elenaneacsu.bookfolio.models.google_books_api_models

/**
 * Created by Elena Neacsu on 01/06/21
 */
data class PartialVolumeInfo(
    val title: String? = null,
    val authors: List<String>? = null,
    val imageLinks: ImageLinks? = null
)