package com.elenaneacsu.bookfolio.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.elenaneacsu.bookfolio.models.QuoteRoomEntity


@Database(version = 1, entities = [QuoteRoomEntity::class], exportSchema = false)
abstract class QuotesDatabase : RoomDatabase() {

    abstract fun quoteDao(): QuoteDao

    companion object {

        @Volatile
        private var INSTANCE: QuotesDatabase? = null

        fun getDatabase(context: Context): QuotesDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuotesDatabase::class.java,
                    "quotes_database"
                )
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}