package com.shash.earphonemodeoff.utils.extensions

import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.shashi.earphonemodeoff.R

/**
 *@author = Shashi
 *@date = 28/07/21
 *@description = This File contains Context extension functions
 */


/**
 * load image from url
 * @param: imageUrl: String
 * @return: Unit
 * @author: Shashi
 * */
fun ImageView.loadImage(
    imageUrl: String,
    errorIcon: Int = R.drawable.ic_close,
    mCenterCrop: Boolean = false,
    imageSize : Int?=null,
    allowCache : Boolean = true
) {
    //load url
    val g = Glide.with(this).load(imageUrl)

    //crop if allowed
    if (mCenterCrop) g.centerCrop()

    //image size
    imageSize?.let { g.override(it,it) }

    //caching
    if (allowCache) g.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

    //Error icon , image view
    g.error(errorIcon).into(this)

}

/**
 * load image from url
 * @param: context: Context
 * @return: CircularProgressDrawable
 * @author: Shashi
 * */
fun ImageView.getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 7f
        centerRadius = 30f
        start()
    }
}

