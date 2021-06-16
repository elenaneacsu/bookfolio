package com.elenaneacsu.bookfolio.ui.search

import com.elenaneacsu.bookfolio.models.google_books_api_models.FullItemResponse
import com.elenaneacsu.bookfolio.models.google_books_api_models.Item
import com.elenaneacsu.bookfolio.networking.RetrofitRequests
import com.elenaneacsu.bookfolio.viewmodel.BaseRepository
import javax.inject.Inject

/**
 * Created by Elena Neacsu on 18/05/21
 */
class SearchRepository @Inject constructor(private val retrofitRequests: RetrofitRequests) :
    BaseRepository() {

    suspend fun searchBooks(searchQuery: String): List<Item>? {
        val response = retrofitRequests.getVolumes(searchQuery)
        return response.items
    }

    suspend fun getBooksDetails(id: String): FullItemResponse? =
        retrofitRequests.getSingleVolume(id)
    }