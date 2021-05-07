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
 * Validate age if it's bigger than @DEFAULT_MIN_AGE and lower than @DEFAULT_MAX_AGE
 *
 * @throws NumberFormatException if String is not a number
 * @return - the state of the age, if is valid or not
 */
fun String.isValidAge(minAge: Int = 15, maxAge: Int = 99): Boolean {
    val i: Int

    try {
        i = Integer.parseInt(this)
        if (i < minAge || i > maxAge) {
            return false
        }
    } catch (e: NumberFormatException) {
        return false
    }

    return true
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

// FIXME: 06.11.2020 remove method and uncomment the other one. no password validation from the server side.
fun String?.isValidPassword(): Boolean {
    return this != null && !TextUtils.isEmpty(this)
}
/*fun String?.isValidPassword(): Boolean {
    *//**
This pattern matches password validation for having 3 of 4 of the following items:
lower case
upper case
numbers special
characters
at least 8 characters - password min length according to Magento settings

^ # beginning of line anchor
((?=.*[\d]) #check for digits
(?=.*[a-z]) #check for lower case
(?=.*[A-Z]) #check for upper case
|(?=.*[a-z]) (?=.*[A-Z]) (?=.*[^\w\d\s]) #check for special chars
|(?=.*[\d]) (?=.*[A-Z]) (?=.*[^\w\d\s])|(?=.*[\d]) (?=.*[a-z]) (?=.*[^\w\d\s])).{7,} - match any character from 8 start char at the end of line anchor
 **//*

    val regexPasswordPattern =
        "^((?=.*[\\d])(?=.*[a-z])(?=.*[A-Z])|(?=.*[a-z])(?=.*[A-Z])(?=.*[^\\w\\d\\s])|(?=.*[\\d])(?=.*[A-Z])(?=.*[^\\w\\d\\s])|(?=.*[\\d])(?=.*[a-z])(?=.*[^\\w\\d\\s])).{8,}\$".toRegex()
    return this != null && !TextUtils.isEmpty(this) && this.matches(regexPasswordPattern)
}*/

fun String?.isValidName(): Boolean {
    return this != null && !TextUtils.isEmpty(this) && this.length > NAME_MIN_LENGTH
}

fun String?.isValidField(): Boolean {
    return this != null && !TextUtils.isEmpty(this) && this.length > NAME_MIN_LENGTH
}

fun String?.isValidResetCode(): Boolean {
    return this != null && !TextUtils.isEmpty(this) && this.length == RESET_CODE_LENGTH
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