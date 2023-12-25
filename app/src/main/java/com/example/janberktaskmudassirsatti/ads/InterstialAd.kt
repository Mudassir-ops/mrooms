package com.example.janberktaskmudassirsatti.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.janberktaskmudassirsatti.R
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


private var interstitialAd: InterstitialAd? = null
private var adIsLoading: Boolean = false
fun Context.loadInterAd() {
    if (adIsLoading || interstitialAd != null) {
        return
    }
    adIsLoading = true
    val adRequest = AdRequest.Builder().build()

    InterstitialAd.load(
        this,
        getString(R.string.interstitial_id),
        adRequest,
        object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d("TAG", adError.toString())
                interstitialAd = null
                adIsLoading = false

            }

            override fun onAdLoaded(interstitialAd1: InterstitialAd) {
                Log.d("TAG", "Ad was loaded.")
                interstitialAd = interstitialAd1
                adIsLoading = false
            }
        })

}

fun Activity.showInterAd() {
    if (interstitialAd != null) {
        interstitialAd?.show(this)
        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                Log.d("TAG", "Ad was clicked.")
            }
            override fun onAdDismissedFullScreenContent() {
                Log.d("TAG", "Ad dismissed fullscreen content.")
                interstitialAd = null
                loadInterAd()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                Log.e("TAG", "Ad failed to show fullscreen content.")
                interstitialAd = null

            }

            override fun onAdImpression() {
                Log.d("TAG", "Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d("TAG", "Ad showed fullscreen content.")
            }
        }
    } else {
        Log.d("TAG", "The interstitial ad wasn't ready yet.")
        loadInterAd()
    }
}