package com.elenaneacsu.bookfolio.di

import android.content.Context
import com.elenaneacsu.bookfolio.BuildConfig
import com.elenaneacsu.bookfolio.networking.ApiKeyInterceptor
import com.elenaneacsu.bookfolio.networking.BooksService
import com.elenaneacsu.bookfolio.networking.IRetrofitRequests
import com.elenaneacsu.bookfolio.room.QuotesDatabase
import com.elenaneacsu.bookfolio.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideBaseUrl() = Constants.BASE_URL

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val apiKeyInterceptor = ApiKeyInterceptor()
        return if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(apiKeyInterceptor)
                .build()
        } else {
            OkHttpClient
                .Builder()
                .addInterceptor(apiKeyInterceptor)
                .build()
        }
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideBooksService(retrofit: Retrofit) = retrofit.create(BooksService::class.java)

    @Provides
    @Singleton
    fun provideRetrofitRequests(retrofitRequests: IRetrofitRequests) = retrofitRequests

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        QuotesDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideQuoteDao(database: QuotesDatabase) = database.quoteDao()

}