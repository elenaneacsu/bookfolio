package com.elenaneacsu.bookfolio.extensions

import androidx.lifecycle.MutableLiveData


fun MutableLiveData<String>.checkStringValue(
    condition: (String) -> Boolean,
    noError: (String) -> Unit,
    hasError: () -> Unit,
    isNullOrEmpty: () -> Unit,
) {
    this.value?.let {
        if (it.isBlank()) {
            isNullOrEmpty()
        } else {
            if (condition(it))
                noError(it)
            else
                hasError()
        }
    } ?: run {
        isNullOrEmpty()
    }
}

fun MutableLiveData<String>.checkStringValue(
    isNullOrEmpty: () -> Unit,
    noError: (String) -> Unit,
) {
    this.value?.let {
        if (it.isBlank()) {
            isNullOrEmpty()
        } else {
            noError(it)
        }
    } ?: run {
        isNullOrEmpty()
    }
}

fun MutableLiveData<String>.checkPasswordsValue(
    retypedPassword: String?,
    hasError: (List<PasswordErrors>) -> Unit,
    noError: (String) -> Unit,
) {
    val passwordErrors = this.value.isPasswordTheSameAs(retypedPassword)
    if (passwordErrors.isEmpty()) {
        noError(this.value!!)
    } else {
        hasError(passwordErrors)
    }
}

fun <T> MutableLiveData<T>.checkValue(
    condition: (T) -> Boolean,
    noError: (T) -> Unit,
    hasError: () -> Unit,
    isNullOrEmpty: (() -> Unit)? = null,
) {
    this.value?.let {
        if (condition(it))
            noError(it)
        else
            hasError()
    } ?: run {
        isNullOrEmpty?.invoke()
    }
}