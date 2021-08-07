package com.elenaneacsu.bookfolio.extensions

import android.text.TextUtils
import com.elenaneacsu.bookfolio.extensions.PasswordErrors.*
import java.text.SimpleDateFormat
import java.util.*

private const val NAME_MIN_LENGTH = 1
private const val RESET_CODE_LENGTH = 4

enum class PasswordErrors {
    NOT_THE_SAME,
    PASSWORD_TOO_SHORT,
    PASSWORD_IS_EMPTY,
    RETYPED_PASSWORD_TOO_SHORT,
    RETYPED_PASSWORD_IS_EMPTY
}

/**
 * Validate email if match format (text@text.text), where "text" could be anything
 *
 * @return - the state of email, if is valid or not
 */

fun String?.isValidEmail(): Boolean {
    val regexEmailPattern = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}".toRegex()
    return this != null && !TextUtils.isEmpty(this) && this.matches(regexEmailPattern)
}

/**
 * Validate phone number if match format (10 digits), where "text" could be anything
 *
 * @return - the state of the phone number, if is valid or not
 */

fun String?.isValidPhoneNumber(): Boolean {
    val regexPhoneNumberPattern = "^[+]?[0-9]{8,20}$".toRegex()
    return this != null && !TextUtils.isEmpty(this) && this.matches(regexPhoneNumberPattern)
}

fun String?.isValidPassword(): Boolean {
    return this != null && !TextUtils.isEmpty(this)
}

fun String?.isValidName(): Boolean {
    return this != null && !TextUtils.isEmpty(this) && this.length > NAME_MIN_LENGTH
}

fun String?.isValidField(): Boolean {
    return this != null && !TextUtils.isEmpty(this) && this.length > NAME_MIN_LENGTH
}

fun String?.isPasswordTheSameAs(retypedPassword: String?): List<PasswordErrors> {
    val passwordErrors = mutableListOf<PasswordErrors>()
    if (this.isNullOrEmpty()) passwordErrors.add(PASSWORD_IS_EMPTY)
    if (!this.isValidPassword()) passwordErrors.add(PASSWORD_TOO_SHORT)
    if (retypedPassword.isNullOrEmpty()) passwordErrors.add(RETYPED_PASSWORD_IS_EMPTY)
    if (!retypedPassword.isValidPassword()) passwordErrors.add(RETYPED_PASSWORD_TOO_SHORT)
    if (this.isValidPassword() && retypedPassword.isValidPassword() && retypedPassword != this) passwordErrors.add(
        NOT_THE_SAME
    )
    return passwordErrors.toList()
}

fun String?.checkPasswordsValue(
    retypedPassword: String?,
    hasError: (List<PasswordErrors>) -> Unit,
    noError: (String) -> Unit,
) {
    val passwordErrors = this.isPasswordTheSameAs(retypedPassword)
    if (passwordErrors.isEmpty()) {
        noError(this!!)
    } else {
        hasError(passwordErrors)
    }
}

fun String?.checkStringValue(
    condition: (String) -> Boolean,
    noError: (String) -> Unit,
    hasError: () -> Unit,
    isNullOrEmpty: () -> Unit,
) {
    this?.let {
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


fun String.stringToDate(dateFormat: String, locale: Locale = Locale.US): Date {
    val simpleDateFormat = SimpleDateFormat(dateFormat, locale)
    return try {
        simpleDateFormat.parse(this)
    } catch (e: Exception) {
        e.printStackTrace()
        Date()
    }
}

fun List<String>.formattedBookDetails(separator: String): String {
    val formattedAuthorsStringBuilder = StringBuilder()
    for (author in this)
        formattedAuthorsStringBuilder.append(author).append(separator)
    return formattedAuthorsStringBuilder.dropLast(separator.length).toString()
}