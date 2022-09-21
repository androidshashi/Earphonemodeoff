package com.shash.earphonemodeoff.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import java.text.SimpleDateFormat
import java.util.*

/**
 *@author = Shashi
 *@date = 28/07/21
 *@description = This File contains Activity extension functions
 */

/**
 * @param: (activity: Class<A>, flags: Boolean = true)
 * @return: Unit
 * @author: Shashi
 * Takes activity as argument and launch the activity*/



fun <A : Activity> Activity.startNewActivity(activity: Class<A>, flags: Boolean = true) {
    Intent(this, activity).also {
        if (flags) it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(it)
    }
}

/**
 * @param: null
 * @return: Unit
 * @author: Shashi
 * shows snakbar to the views
 * hide soft keyboard forcefully
 * */
fun Activity.hideKeyboard() {
    try {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        /** Check if no view has focus*/
        val currentFocusedView = currentFocus
        currentFocusedView?.let {
            inputMethodManager.hideSoftInputFromWindow(
                currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    } catch (e: Exception) {
        /**Some exception caught*/
    }
}

/**
make the screen touch disable and enable based on disable variable value
* @param: disable: Boolean
* @return: Unit
* @author: Shashi
*/
fun Activity.disableInteraction(disable: Boolean) {
    if (disable)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        );
    else {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}


/**
 * navigating user to app settings
 *@param: null
 *@return: Unit
 *@author: Shashi
 * */
private fun Activity.openSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", this.packageName, null)
    intent.data = uri
    startActivityForResult(intent, 121)
}

fun Activity.expireDemoApp(dateString: String):Boolean
{
   val sfd = SimpleDateFormat("dd-MM-yyyy hh:mm:ss",Locale.ROOT)
    val expDateInMilli = sfd.parse(dateString).time
    Log.d("adfs",expDateInMilli.toString())

    return System.currentTimeMillis()>=expDateInMilli

}

