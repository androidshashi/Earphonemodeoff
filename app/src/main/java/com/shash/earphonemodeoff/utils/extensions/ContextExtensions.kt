package com.shash.earphonemodeoff.utils.extensions

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.view.ViewGroup
import android.view.Window
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.shashi.earphonemodeoff.R


/**
 *@author = Shashi
 *@date = 25/07/21
 *@description = This File contains Context extension functions
 */


/**
 * Shows alert dialog
 */
fun Context.showAlertDialog(
    title: String? = null,
    message: String,
    posBtnText:String? = null,
    negBtnText:String? = null,
    showNegBtn:Boolean = true,
    callback: () -> Unit
) {
    AlertDialog.Builder(this).also {
        it.setTitle(title ?: "Alert")
        it.setMessage(message)
        it.setPositiveButton(posBtnText?:"Yes") { _, _ ->
            callback()
        }
        if (showNegBtn) {
            it.setNegativeButton(negBtnText?:"No") { dialog, _ ->
                dialog.dismiss()
            }
        }
    }.create().show()
}

fun Context.shareApp() {
    val text =
        "Earphone mode off Download the app\nhttps://play.google.com/store/apps/details?id=$packageName"
    val intent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(intent, null)
    startActivity(shareIntent)
}

/**shows toast to the context
 * @param: text: String
 * @return: Unit
 * @author: Shashi
 */

fun Context.showToast(text: String?) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}



/**
 * Open play store with our app
 */
fun Context.openAppOnPlayStore(package_name: String = packageName) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$package_name")))
    } catch (e: java.lang.Exception) {
        showToast("Unable to open play store")
    }
}

/**
 * Open play store with developer id or page id
 */
fun Context.moreApps(developerId: String? = null, devPageId: String? = null) {
    val pageUri =
        if (devPageId != null) "https://play.google.com/store/apps/dev?id=$devPageId" else "https://play.google.com/store/apps/developer?id=$developerId"
    try {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(pageUri)
            )
        )
    } catch (e: java.lang.Exception) {
        showToast("Unable to open play store")
    }
}

fun Context.openSite(siteUrl: String) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(siteUrl)))
    } catch (e: java.lang.Exception) {
        showToast("Unable to open the site")
    }
}

fun Context.showProgressDialog(title: String="Loading",message: String = "Please wait..."):Dialog
{
    val dialog = Dialog(this)
    dialog.setContentView(R.layout.progress_dialog)
    dialog.setTitle(title)
    dialog.setCancelable(false)
    dialog.setCanceledOnTouchOutside(false)
    //set the custom dialog components - title, ProgressBar and button
    dialog.findViewById<TextView>(R.id.messageTV).text = message
    dialog.show()
    return dialog
}

fun Context.showRatingDialog(rateCallback:()->Unit, exitCallback:()->Unit ):Dialog
{
    val dialog = Dialog(this)
    dialog.window?.apply {
        setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        requestFeature(Window.FEATURE_NO_TITLE)
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    dialog.apply {
        setContentView(R.layout.rating_dialog_layout)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }
    //set the custom dialog components - title, ProgressBar and button
    val rateBtn =  dialog.findViewById<MaterialButton>(R.id.rateBtn).apply {
        setOnClickListener {
            rateCallback.invoke()
        }

        enable(false)
    }

    dialog.findViewById<MaterialButton>(R.id.laterBtn).setOnClickListener {
        exitCallback.invoke()
    }

    dialog.findViewById<RatingBar>(R.id.ratingBar).setOnRatingBarChangeListener { ratingBar, fl, b ->
        rateBtn.enable(ratingBar.rating>=0.5)
    }

    dialog.show()
    return dialog
}

fun Context.aboveSDK30() = android.os.Build.VERSION.SDK_INT>Build.VERSION_CODES.R
fun Context.belowSDK26() = android.os.Build.VERSION.SDK_INT<Build.VERSION_CODES.O
/**
 * launch Custom chrome tab
 */

fun Context.openCustomChromeTab(url:String)
{

    // initializing object for custom chrome tabs.
    // initializing object for custom chrome tabs.
    val customIntent = CustomTabsIntent.Builder()

    // below line is setting toolbar color
    // for our custom chrome tab.
    customIntent.setToolbarColor(ContextCompat.getColor(this, android.R.color.holo_green_light))
    val customTabsIntent = customIntent.build()
    // package name is the default package
    // for our custom chrome tab
    // package name is the default package
    // for our custom chrome tab
    val packageName = "com.android.chrome"
    try{
        // we are checking if the package name is not null
        // if package name is not null then we are calling
        // that custom chrome tab with intent by passing its
        // package name.
        customTabsIntent.intent.setPackage(packageName)

        // in that custom tab intent we are passing
        // our url which we have to browse.
        customTabsIntent.launchUrl( this, Uri.parse(url))
    } catch (e:java.lang.Exception) {
        // if the custom tabs fails to load then we are simply
        // redirecting our user to users device default browser.
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}
