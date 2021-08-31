package com.elenaneacsu.bookfolio.networking

import com.elenaneacsu.bookfolio.models.google_books_api_models.FullItemResponse
import com.elenaneacsu.bookfolio.models.google_books_api_models.SearchResponse
import com.elenaneacsu.bookfolio.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Elena Neacsu on 14/05/21
 */
interface BooksService {

    @GET("volumes")
    suspend fun getVolumes(
        @Query("q") searchQuery: String,
        @Query("maxResults") maxResults: Int = 40,
        @Query("fields") fields: String = Constants.PARTIAL_SEARCH_QUERY_FIELDS
    ): SearchResponse

    @GET("volumes/{id}")
    suspend fun getSingleVolume(
        @Path("id") id: String
    ): FullItemResponse
}