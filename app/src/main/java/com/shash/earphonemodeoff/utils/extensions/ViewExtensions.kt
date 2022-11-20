package com.shash.earphonemodeoff.utils.extensions

import android.content.res.Resources
import android.graphics.Color
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar

/**
 *@author = Shashi
 *@date = 28/07/21
 *@description = This File contains View extension functions
 */
/**
 * @param: isVisible: Boolean
 * @return: Unit
 * @author: Shashi
 * Shows and hides a view based on argument isVisible
 * */

fun View.visible(isVisible: Boolean, isGone: Boolean = true) {

    visibility = if (isGone) {
        if (isVisible) View.VISIBLE else View.GONE
    } else {
        if (isVisible) View.VISIBLE else View.INVISIBLE
    }
}

/**enables and disables a view based on argument isVisible
 * @param: enabled: Boolean
 * @return: Unit
 * @author: Shashi
 * */

fun View.enable(enabled: Boolean) {
    isEnabled = enabled
    alpha = if (enabled) 1f else 0.5f
}

/**
 * @param: (message: String, action: (() -> Unit)? = null)
 * @return: Unit
 * @author: Shashi
 * shows snakbar to the views
 */
fun View.snackBar(
    message: String,
    action: (() -> Unit)? = null,
    actionTitle:String? = null,
    length: Int = Snackbar.LENGTH_LONG
):Snackbar {
    "Retry"
    val snackbar = Snackbar.make(this, message,length )
    /**snackbar.setBackgroundTint(this.resources.getColor(R.color.primary,null))*/

    action?.let {
        snackbar.setAction(actionTitle) {
            it()
        }
    }
    snackbar.show()
    snackbar.view.setBackgroundColor(Color.WHITE)
    snackbar.setTextColor(Color.BLACK)
    return snackbar
}

fun View.setEnabledRecursively(enabled: Boolean) {
    isEnabled = enabled
    if (this is ViewGroup)
        (0 until childCount).map(::getChildAt).forEach { it.setEnabledRecursively(enabled)
            it.alpha = if (enabled) 1f else 0.9f }
}

