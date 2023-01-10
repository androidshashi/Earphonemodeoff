package com.shash.earphonemodeoff.utils.extensions

import com.shashi.earphonemodeoff.BuildConfig
import com.shash.earphonemodeoff.utils.LIVE_BANNER_AD
import com.shash.earphonemodeoff.utils.LIVE_INTERSTITIAL_AD
import com.shash.earphonemodeoff.utils.TEST_BANNER_AD
import com.shash.earphonemodeoff.utils.TEST_INTERSTITIAL_AD

/**
 * @author: Shashi
 * @date : 17-09-2022
 * @description : Admob extensions functions
 **/

fun getBannerAdUnitId() = if(BuildConfig.DEBUG) TEST_BANNER_AD else LIVE_BANNER_AD
fun getInterstitialAdUnitId() = if(BuildConfig.DEBUG) TEST_INTERSTITIAL_AD else LIVE_INTERSTITIAL_AD



