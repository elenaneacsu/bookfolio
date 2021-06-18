package com.elenaneacsu.bookfolio.models

/**
 * Created by Elena Neacsu on 18/06/21
 */
data class Quote(
    var text: String? = null,
    var book: String? = null,
    var author: String? = null,
    var page: String? = null,
    var date: String? = null
) {
}