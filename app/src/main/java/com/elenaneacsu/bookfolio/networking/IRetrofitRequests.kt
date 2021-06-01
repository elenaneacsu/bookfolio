package com.elenaneacsu.bookfolio.networking

import com.elenaneacsu.bookfolio.models.google_books_api_models.SearchResponse
import com.elenaneacsu.bookfolio.models.google_books_api_models.VolumeDetailsResponse

/**
 * Created by Elena Neacsu on 15/05/21
 */
interface IRetrofitRequests {

    suspend fun getVolumes(searchQuery: String): SearchResponse

    suspend fun getSingleVolume(id: String): VolumeDetailsResponse

}