package com.elenaneacsu.bookfolio.networking

import com.elenaneacsu.bookfolio.models.google_books_api_models.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Elena Neacsu on 14/05/21
 */
interface BooksService {

    @GET("volumes")
    suspend fun getVolumes(@Query("q") searchQuery: String): SearchResponse
}