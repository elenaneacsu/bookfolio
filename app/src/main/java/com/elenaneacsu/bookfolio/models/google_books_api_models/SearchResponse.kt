package com.elenaneacsu.bookfolio.models.google_books_api_models

/**
 * Created by Elena Neacsu on 14/05/21
 */
data class SearchResponse(
    val kind: String? = null,
    val totalItems: Int? = null,
    val items: List<PartialItem>? = null
)