package com.shash.earphonemodeoff.view.ui

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.shash.earphonemodeoff.utils.InternetUtils
import com.shash.earphonemodeoff.utils.extensions.*
import com.shashi.earphonemodeoff.R
import com.shashi.earphonemodeoff.databinding.ActivityEarphoneOnOffBinding

class EarphoneOnOffActivity : AppCompatActivity() {
    private var _binding: ActivityEarphoneOnOffBinding? = null
    private val binding get() = _binding!!
    private var mAudioMgr: AudioManager? = null
    private var player: MediaPlayer? = null
    private var mInterstitialAd: InterstitialAd? = null
    private var progressDialog: Dialog? = null

    companion object {
        const val TAG = "EarphoneActivityTAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityEarphoneOnOffBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        //init required references
        initViews()
        //listeners
        listeners()
        //show banner layout
        showBannerAd()
        //load interstitial ad
        if (InternetUtils.checkConnectivity(this))
            loadInterstitialAd()

        if (belowSDK26()) {
            showToast("Scroll down to TEST")
        }
    }

    /**
     * Loads and shows interstitial ads
     */
    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        progressDialog = showProgressDialog()
        InterstitialAd.load(this,
            getInterstitialAdUnitId(), adRequest, object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.message)
                    mInterstitialAd = null
                    progressDialog?.dismiss()
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded")
                    mInterstitialAd = interstitialAd
                    mInterstitialAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {

                            override fun onAdDismissedFullScreenContent() {
                                Log.d(TAG, "Ad was dismissed.")
                                progressDialog?.dismiss()
                            }

                            override fun onAdShowedFullScreenContent() {
                                mInterstitialAd = null
                                progressDialog?.dismiss()
                            }
                        }
                    mInterstitialAd?.show(this@EarphoneOnOffActivity)
                }
            })
    }

    override fun onDestroy() {
        progressDialog?.dismiss()
        super.onDestroy()
    }

    private fun showBannerAd() {
        val adView = AdView(this)

        adView.setAdSize(AdSize.BANNER)

        adView.adUnitId = getBannerAdUnitId()

        val adRequest = AdRequest.Builder()
            .build()

        if (!(this.isDestroyed && !this.isFinishing)) {
            adView.loadAd(adRequest)
            binding.bannerLayout.addView(adView)
        }
    }

    private fun initViews() {
        mAudioMgr = getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    private fun listeners() {

        wiredHeadSetSelected(mAudioMgr!!.isWiredHeadsetOn)

        binding.speakerSwitch.setOnCheckedChangeListener { compoundButton, b ->
            wiredHeadSetSelected(!b)
            activateSpeaker(b)

        }

        binding.clickToChangeBtn.setOnClickListener {
            wiredHeadSetSelected(false)
            if (mAudioMgr!!.isWiredHeadsetOn) {
                mAudioMgr = getSystemService(Context.AUDIO_SERVICE) as AudioManager
                mAudioMgr?.isWiredHeadsetOn = false
                mAudioMgr?.isSpeakerphoneOn = true
                mAudioMgr?.mode = AudioManager.MODE_IN_COMMUNICATION
                showToast("SpeakerPhone On")
            } else {
                activateSpeaker(true)
            }
        }

        binding.earphoneSwitch.setOnCheckedChangeListener { compoundButton, b ->
            wiredHeadSetSelected(b)
            activateSpeaker(!b)
        }

        binding.testBtn.setOnClickListener {
            playAudio()
        }

        binding.resetBtn.setOnClickListener {
            showToast("Restart the phone to reset.")
        }

        binding.dialerBtn.setOnClickListener {

            try {
                val intent = Intent(Intent.ACTION_DIAL)
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                showToast("Sorry! Can not open Dialer")
            }
        }
    }

    private fun activateSpeaker(activate: Boolean) {
        if (activate) {
            //enable speaker
            mAudioMgr = getSystemService(Context.AUDIO_SERVICE) as AudioManager
            mAudioMgr?.isWiredHeadsetOn = false
            mAudioMgr?.isSpeakerphoneOn = true
            mAudioMgr?.mode = AudioManager.MODE_IN_COMMUNICATION
            showToast("SpeakerPhone On")
        } else {
            //disable speaker
            mAudioMgr?.mode = AudioManager.MODE_IN_COMMUNICATION
            mAudioMgr?.isSpeakerphoneOn = false;
            mAudioMgr?.isWiredHeadsetOn = true;
            showToast("Wired Headset On")
        }
    }

    private fun wiredHeadSetSelected(earphone: Boolean) {
        if (earphone) {
            binding.apply {
                speakerSwitch.isChecked = false
                earphoneSwitch.isChecked = true
                speakerLine.background =
                    ResourcesCompat.getDrawable(resources, R.color.color_red, null)
                speakerLine1.background =
                    ResourcesCompat.getDrawable(resources, R.color.color_red, null)
                speakerLine2.background =
                    ResourcesCompat.getDrawable(resources, R.color.color_red, null)

                earphoneLine.background =
                    ResourcesCompat.getDrawable(resources, R.color.color_green, null)
                earphoneLine1.background =
                    ResourcesCompat.getDrawable(resources, R.color.color_green, null)
                earphoneLine2.background =
                    ResourcesCompat.getDrawable(resources, R.color.color_green, null)

                speakerImg.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.red_circle, null)
                headphoneImg.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.green_circle, null)
            }


        } else {
            binding.apply {

                speakerSwitch.isChecked = true
                earphoneSwitch.isChecked = false
                speakerLine.background =
                    ResourcesCompat.getDrawable(resources, R.color.color_green, null)
                speakerLine1.background =
                    ResourcesCompat.getDrawable(resources, R.color.color_green, null)
                speakerLine2.background =
                    ResourcesCompat.getDrawable(resources, R.color.color_green, null)

                earphoneLine.background =
                    ResourcesCompat.getDrawable(resources, R.color.color_red, null)
                earphoneLine1.background =
                    ResourcesCompat.getDrawable(resources, R.color.color_red, null)
                earphoneLine2.background =
                    ResourcesCompat.getDrawable(resources, R.color.color_red, null)

                headphoneImg.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.red_circle, null)
                speakerImg.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.green_circle, null)
            }

        }
    }

    private fun playAudio() {
        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        if (player == null) {
            player = MediaPlayer.create(this, notification)
            player?.start()
            binding.testBtn.text = getString(R.string.stop)
        } else {
            if (binding.testBtn.text == getString(R.string.stop)) {
                player?.reset()
                binding.testBtn.text = getString(R.string.test)
            } else {
                player?.reset()
                player = MediaPlayer.create(this, notification)
                player?.start()
                binding.testBtn.text = getString(R.string.stop)
            }
        }
    }
}