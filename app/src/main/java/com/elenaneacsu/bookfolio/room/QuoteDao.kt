package com.elenaneacsu.bookfolio.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.elenaneacsu.bookfolio.models.QuoteRoomEntity

@Dao
interface QuoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(roomQuote: QuoteRoomEntity)

    @Query("SELECT * FROM quotes")
    fun getAllQuotes(): LiveData<List<QuoteRoomEntity>>

    @Query("DELETE FROM quotes WHERE id=:quoteId")
    suspend fun deleteQuote(quoteId: String)

    @Update
    suspend fun updateQuote(roomQuote: QuoteRoomEntity)
}