package com.elenaneacsu.bookfolio.di

import android.content.Context
import com.elenaneacsu.bookfolio.utils.ResourceString
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object BaseModule {

    @Provides
    fun provideResourceString(@ApplicationContext context: Context): ResourceString {
        return ResourceString(context)
    }
}