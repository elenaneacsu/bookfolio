package com.elenaneacsu.bookfolio.ui.shelves.shelf

import com.elenaneacsu.bookfolio.models.google_books_api_models.Item
import com.elenaneacsu.bookfolio.networking.RetrofitRequests
import com.elenaneacsu.bookfolio.viewmodel.BaseRepository
import javax.inject.Inject

/**
 * Created by Elena Neacsu on 15/05/21
 */
class ShelfRepository @Inject constructor(private val retrofitRequests: RetrofitRequests) :
    BaseRepository() {

    suspend fun searchBooks(searchQuery: String): List<Item>? {
        val response = retrofitRequests.getVolumes(searchQuery)
        return response.items?.subList(0, 3)
    }
}