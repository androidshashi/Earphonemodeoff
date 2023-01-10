package com.shash.earphonemodeoff.utils.extensions

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 *@author = Shashi
 *@date = 25/07/21
 *@description = This File String Context extension functions
 */


/**
 *
 * Extract OTP from string and populate it in this edittext
 */
fun String.extractOTPFromMessageString(): Int? {
    val otpPattern: Pattern = Pattern.compile("(|^)\\d{6}")
    val matcher: Matcher = otpPattern.matcher(this)
    if (matcher.find()) {
        return matcher.group(0)?.toIntOrNull()
    }

    return null
}




