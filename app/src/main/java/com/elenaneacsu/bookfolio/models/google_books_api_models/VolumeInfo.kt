package com.elenaneacsu.bookfolio.models.google_books_api_models

/**
 * Created by Elena Neacsu on 14/05/21
 */
data class VolumeInfo(
    val title: String? = null,
    val authors: List<String>? = null,
    val publishedDate: String? = null,
    val printType: String? = null,
    val pageCount: Int? = null,
    val imageLinks: ImageLinks? = null
)