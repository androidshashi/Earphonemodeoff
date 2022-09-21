package com.shash.earphonemodeoff.utils.extensions

import android.util.Patterns
import android.widget.EditText

/**
 *@author = Shashi
 *@date = 28/07/21
 *@description = This File contains EditText extension functions
 */

/**
 *
 * If validation passes (edittext is not empty) then returns true else false
 */
fun EditText.validateEmpty(fieldName: String): Boolean {
    return if (this.text.isEmpty()) {
        this.requestFocus()
        this.error = "Please enter $fieldName"
        true
    } else false
}

/**
 *
 * If validation passes (correct email pattern) then returns false else true
 */
fun EditText.validateEmailPattern(): Boolean {
    return if (!Patterns.EMAIL_ADDRESS.matcher(this.text).matches()) {// validation failed
        this.requestFocus()
        this.error = "Please enter correct email"
        true
    } else false //validation pass
}

/**
 *
 * If validation passes (edittext text length>$length) then returns false else true
 */
fun EditText.validateMinLength(fieldName: String, length: Int): Boolean {
    return if (this.text.length < length) { // validation failed
        this.requestFocus()
        this.error = "$fieldName length should be at least $length character"
        true
    } else false
}

/**
 *
 * If validation passes (edittext text length==$length) then returns false else true
 */
fun EditText.validateExactLength(fieldName: String, length: Int): Boolean {
    val len = this.text.toString().trim().length
    return if (len != length) {// validation failed
        this.requestFocus()
        this.error = "$fieldName length must be $length"
        true
    } else false
}


/**
 * Clear edit texts
 */
fun EditText.clear() {
    this.setText("")
}

fun EditText.compare(newPasswordET: EditText,fieldName1:String,fieldName2:String): Boolean {
    return if (this.text.toString() != newPasswordET.text.toString()) { // validation failed
        this.requestFocus()
        this.error = "$fieldName1 must be same as $fieldName2"
        true
    } else false
}
