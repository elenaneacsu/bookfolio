package com.elenaneacsu.bookfolio.models

import android.os.Parcelable
import com.elenaneacsu.bookfolio.extensions.formattedBookDetails
import com.elenaneacsu.bookfolio.models.google_books_api_models.FullItemResponse
import com.elenaneacsu.bookfolio.models.google_books_api_models.Item
import com.elenaneacsu.bookfolio.utils.Constants.Companion.AUTHORS_SEPARATOR
import com.elenaneacsu.bookfolio.utils.Constants.Companion.CATEGORIES_SEPARATOR
import kotlinx.android.parcel.Parcelize

/**
 * Created by Elena Neacsu on 07/08/2021
 */
@Parcelize
class BookDetailsMapper(
    private val userBook: UserBook? = null,
    private val apiBook: FullItemResponse? = null
) : Parcelable {
    fun getItemApiBook() = Item(apiBook?.id, apiBook?.volumeInfo)

    fun getId() = apiBook?.id

    fun getCover() = getBook()?.imageLinks?.thumbnail

    fun getSmallCover() = getBook()?.imageLinks?.smallThumbnail

    fun getTitle() = getBook()?.title

    fun getSubtitle() = getBook()?.subtitle

    fun getAuthors() = getBook()?.authors?.formattedBookDetails(AUTHORS_SEPARATOR)

    fun getCategories() = getBook()?.categories?.formattedBookDetails(CATEGORIES_SEPARATOR)

    fun getPrintType() = getBook()?.printType

    fun getPageCount() = getBook()?.pageCount

    fun getDescription() = getBook()?.description

    fun getStartDate() = userBook?.startDate

    fun getEndDate() = userBook?.endDate

    fun areQuotesAvailable() =
        userBook != null // the empty list case is treated in the Book Journal screen

    // region Private helpers

    private fun getUserBookInfo() = userBook?.item?.volumeInfo

    private fun getApiBookInfo() = apiBook?.volumeInfo

    private fun getBook() = getUserBookInfo() ?: getApiBookInfo()

    // endregion
}