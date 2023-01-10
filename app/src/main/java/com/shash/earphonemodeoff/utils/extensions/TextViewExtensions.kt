package com.shash.earphonemodeoff.utils.extensions

import android.graphics.Paint
import android.widget.TextView
import androidx.core.text.HtmlCompat


/**
 *@author = Shashi
 *@date = 28/07/21
 *@description = This File contains TextView extension functions
 */
fun TextView.setHtmlText(source: String) {
    this.text = HtmlCompat.fromHtml(source, HtmlCompat.FROM_HTML_MODE_LEGACY)
}


/**
 * @author = shashi
 * @return = NA
 * @param = strike: Boolean
 * @description = ext function of textview for strike through the edit text
 */

fun TextView.strikeThrough(strike: Boolean) {
    //strike through the edit text
    paintFlags = if (strike)
        paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    else
        paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
}
