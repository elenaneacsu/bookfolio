package com.elenaneacsu.bookfolio.networking

import com.elenaneacsu.bookfolio.models.google_books_api_models.FullItemResponse
import com.elenaneacsu.bookfolio.models.google_books_api_models.SearchResponse
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by Elena Neacsu on 14/05/21
 */
@Singleton
class RetrofitRequests @Inject constructor(private val booksService: BooksService) :
    IRetrofitRequests {

    override suspend fun getVolumes(searchQuery: String): SearchResponse =
        booksService.getVolumes(searchQuery)

    override suspend fun getSingleVolume(id: String): FullItemResponse =
        booksService.getSingleVolume(id)

}