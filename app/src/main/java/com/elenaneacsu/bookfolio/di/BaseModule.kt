package com.elenaneacsu.bookfolio.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import com.elenaneacsu.bookfolio.utils.ResourceString

/**
 * Created by Grigore Cristian-Andrei on 11.03.2021.
 */

@Module
@InstallIn(ViewModelComponent::class)
object BaseModule {

    @Provides
    fun provideResourceString(@ApplicationContext context: Context): ResourceString {
        return ResourceString(context)
    }
}