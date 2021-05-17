package com.elenaneacsu.bookfolio.networking

import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by Elena Neacsu on 14/05/21
 */
@Singleton
class RetrofitRequests @Inject constructor(private val booksService: BooksService) :
    IRetrofitRequests {

    override suspend fun getVolumes(searchQuery: String) = booksService.getVolumes(searchQuery)
}