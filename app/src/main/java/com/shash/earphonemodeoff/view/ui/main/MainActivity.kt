package com.shash.earphonemodeoff.view.ui.main

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.shash.earphonemodeoff.R
import com.shash.earphonemodeoff.databinding.ActivityMainBinding
import com.shash.earphonemodeoff.utils.InternetUtils
import com.shash.earphonemodeoff.utils.PRIVACY_POLICY_URL
import com.shash.earphonemodeoff.utils.TNC_URL
import com.shash.earphonemodeoff.utils.VIDEO_URL
import com.shash.earphonemodeoff.utils.extensions.*
import com.shash.earphonemodeoff.view.ui.EarphoneOnOffActivity

class MainActivity : AppCompatActivity() {

    /**Binding*/
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView
    private lateinit var goCardView: RelativeLayout
    private var headerView: View? = null
    private var videoImageView: ImageView? = null
    private var videoTitle: TextView? = null

    var zoomout :Animation? = null


    var status = 0

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater,null,false)
        setContentView(binding.root)

        initViews()

        //action bar
        setSupportActionBar(binding.appBarMain.toolbar)

        //setup navigation drawer
        setUpNavigationView()

        showBannerAd()

        showProgressBar()

        listeners()

    }

    private fun listeners() {
        goCardView.setOnClickListener {
            startNewActivity(EarphoneOnOffActivity::class.java,flags = false)
        }
        videoImageView?.setOnClickListener {
            openCustomChromeTab(VIDEO_URL)
        }

        videoTitle?.setOnClickListener {
            openCustomChromeTab(VIDEO_URL)
        }
    }

    private fun initViews() {
        progressBar = binding.appBarMain.mainContentLayout.progressBar
        progressText = binding.appBarMain.mainContentLayout.progressText
        goCardView = binding.appBarMain.mainContentLayout.goCardView
    }

    private fun showProgressBar() {

        val handler = Handler(mainLooper)
        handler.postDelayed(object : Runnable {
            override fun run() {
                // set the limitations for the numeric
                // text under the progress bar
                if (status <= 100) {
                    "$status%".also { progressText.text = it }
                    progressBar.progress = status
                    status++
                    handler.postDelayed(this, progressDelayTime())
                    val hasInternet = InternetUtils.checkConnectivity(this@MainActivity)
                    binding.appBarMain.mainContentLayout.netAlertTV.visible(!hasInternet)
                    binding.appBarMain.mainContentLayout.scanningTV.visible(hasInternet)
                    if (status==100)
                    {
                        progressText.visible(false)
                        goCardView.visible(true)
                        binding.appBarMain.mainContentLayout.netAlertTV.visible(false)
                        zoomout = AnimationUtils.loadAnimation(this@MainActivity, R.anim.zoomout)
                        goCardView.animation = zoomout
                        goCardView.startAnimation(zoomout)
                        binding.appBarMain.mainContentLayout.scanningTV.text = getString(R.string.earphone_mode_is_off)
                    }
                } else {
                    handler.removeCallbacks(this)
                }
            }
        }, progressDelayTime())

    }

    private fun showBannerAd() {
        val adView = AdView(this)

        adView.setAdSize(AdSize.BANNER)

        adView.adUnitId = getBannerAdUnitId()

        val adRequest = AdRequest.Builder()
            .build()

        if (!(this.isDestroyed && !this.isFinishing)) {
            adView.loadAd(adRequest)
            binding.appBarMain.mainContentLayout.bannerContainerLL.addView(adView)
        }
    }

    private fun setUpNavigationView() {
        // nav host fragment
        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = binding.drawerLayout
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, com.shash.earphonemodeoff.R.string.nav_open, com.shash.earphonemodeoff.R.string.nav_close)

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        // to make the Navigation drawer icon always appear on the action bar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //inflating the header view
        headerView = binding.navView.getHeaderView(0)
        // val profileImage = headerView.findViewById(R.id.navProfileCIV) as CircleImageView
        videoImageView = headerView?.findViewById<ImageView>(R.id.thumbnail_imageview)
        videoTitle = headerView?.findViewById<TextView>(R.id.thumbnail_title)



        binding.navView.setNavigationItemSelectedListener { dest ->

            when (dest.itemId) {

                R.id.nav_share_this_app -> {
                    shareApp()
                }

                R.id.nav_rate_us -> {
                    openAppOnPlayStore()
                }

                R.id.nav_privacy_policy -> {
                    openCustomChromeTab(PRIVACY_POLICY_URL)
                }

                R.id.nav_tnc -> {
                    openCustomChromeTab(TNC_URL)
                }

                else -> {

                }
            }
            //close drawer
            binding.drawerLayout.closeDrawers()

            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return super.onOptionsItemSelected(item)
        } else {
            when (item.itemId) {

                R.id.action_share -> {
                    shareApp()
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
}