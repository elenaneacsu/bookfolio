package com.elenaneacsu.bookfolio.utils

/**
 * Created by Elena Neacsu on 07/05/21
 */
class Constants {
    companion object {
        const val BASE_URL = "https://www.googleapis.com/books/v1/"
        const val PARTIAL_SEARCH_QUERY_FIELDS = "kind,totalItems," +
                "items(id,volumeInfo(title, authors, imageLinks))"

        const val USERS_COLLECTION = "users"
        const val CURRENTLY_READING_COLLECTION = "currently_reading"
        const val TO_READ_COLLECTION = "to_read"
        const val READ_COLLECTION = "read"

        const val NUMBER_OF_BOOKS = "number_of_books"
        const val NUMBER_OF_BOOKS_FIELD_NAME = "numberOfBooks"

        const val SPACE = " "
        const val PLUS = "+"

        const val AUTHORS_SEPARATOR = ", "
        const val CATEGORIES_SEPARATOR = " / "
    }
}