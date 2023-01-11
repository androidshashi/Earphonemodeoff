package com.shash.earphonemodeoff.view.ui.welcome

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.shash.earphonemodeoff.databinding.ActivityWelcomeBinding
import com.shash.earphonemodeoff.utils.InternetUtils
import com.shash.earphonemodeoff.utils.extensions.*
import com.shash.earphonemodeoff.view.ui.main.MainActivity

class WelcomeActivity : AppCompatActivity() {
    private var _binding: ActivityWelcomeBinding? = null
    private val binding get() = _binding!!
    private var mInterstitialAd: InterstitialAd? = null

    companion object {
        const val TAG = "WelcomeActivityTAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWelcomeBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        //this check will work only for debug mode
        if (expireDemoApp("30-01-2023 17:45:01")) {
            showAlertDialog(
                title = "Demo App Expired",
                showNegBtn = false,
                message = "Please contact developer",
                posBtnText = "Exit",
                callback = {
                    finish()
                })
        } else {
            MobileAds.initialize(this) {}
            observe()
        }
    }

    private fun observe() {
        val hasInternet = InternetUtils.checkConnectivity(this)
        binding.alertTV.visible(!hasInternet)
        if (hasInternet) {
            loadInterstitialAd()
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                startNewActivity(MainActivity::class.java)
            }, 800)
        }
    }

    /**
     * Loads and shows interstitial ads
     */
    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this,
            getInterstitialAdUnitId(), adRequest, object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.message)
                    mInterstitialAd = null
                    startNewActivity(MainActivity::class.java)
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded")
                    mInterstitialAd = interstitialAd
                    mInterstitialAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {

                            override fun onAdDismissedFullScreenContent() {
                                Log.d(TAG, "Ad was dismissed.")
                                //Navigate to MainActivity
                                startNewActivity(MainActivity::class.java)
                            }

                            override fun onAdShowedFullScreenContent() {
                                mInterstitialAd = null
                            }
                        }
                    mInterstitialAd?.show(this@WelcomeActivity)
                }
            })
    }
}