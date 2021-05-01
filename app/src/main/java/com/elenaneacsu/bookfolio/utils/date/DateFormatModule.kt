package com.elenaneacsu.bookfolio.utils.date

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
object DateFormatModule {

    @Provides
    @Named(DateFormatType.DATE)
    fun dateFormat(): DateFormat = SimpleDateFormat(DateFormatType.DATE, Locale.ENGLISH)

    @Provides
    @Named(DateFormatType.DATE_TIME)
    fun dateTimeFormat(): DateFormat = SimpleDateFormat(DateFormatType.DATE_TIME, Locale.ENGLISH)

    @Provides
    @Named(DateFormatType.DATE_TIME_FULL)
    fun dateTimeFullFormat(): DateFormat =
        SimpleDateFormat(DateFormatType.DATE_TIME_FULL, Locale.ENGLISH)

}