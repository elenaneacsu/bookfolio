package com.elenaneacsu.bookfolio.utils

import android.content.Context
import androidx.annotation.StringRes
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceString @Inject constructor(
    private val context: Context,
) {
    fun getString(@StringRes stringId: Int) = context.getString(stringId)
}