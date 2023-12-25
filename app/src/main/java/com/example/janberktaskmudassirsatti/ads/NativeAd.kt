package com.example.janberktaskmudassirsatti.ads

import android.app.Activity
import com.example.janberktaskmudassirsatti.R
import com.example.janberktaskmudassirsatti.databinding.CustomNativeViewBinding
import com.example.janberktaskmudassirsatti.utill.gone
import com.example.janberktaskmudassirsatti.utill.invisible
import com.example.janberktaskmudassirsatti.utill.printLog
import com.example.janberktaskmudassirsatti.utill.show
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions

private var currentNativeAd: NativeAd? = null
var isAdLoaded = false


fun Activity.loadNativeAd(onAdLoaded: (NativeAd?) -> Unit) {
    if (isAdLoaded) {
        onAdLoaded.invoke(currentNativeAd)
        return
    }
    val builder = AdLoader.Builder(this, getString(R.string.native_id))
    builder.forNativeAd { nativeAd ->
        "Native Ad Loaded".printLog()
        if (isDestroyed || isFinishing || isChangingConfigurations) {
            nativeAd.destroy()
            return@forNativeAd
        }
        isAdLoaded = true
        currentNativeAd?.destroy()
        currentNativeAd = nativeAd
        onAdLoaded.invoke(currentNativeAd)


    }
    val videoOptions = VideoOptions.Builder().setStartMuted(true).build()

    val adOptions = NativeAdOptions.Builder().setVideoOptions(videoOptions).build()

    builder.withNativeAdOptions(adOptions)

    val adLoader = builder.withAdListener(object : AdListener() {
        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            isAdLoaded = false
            val error = """
           domain: ${loadAdError.domain}, code: ${loadAdError.code}, message: ${loadAdError.message}
          """"
            "Failed to load native ad with error $error".printLog()
        }
    }).build()

    adLoader.loadAd(AdRequest.Builder().build())

}

fun populateNativeAdView(nativeAd: NativeAd, unifiedAdBinding: CustomNativeViewBinding) {
    val nativeAdView = unifiedAdBinding.root
    nativeAdView.headlineView = unifiedAdBinding.adHeadline
    nativeAdView.bodyView = unifiedAdBinding.adBody
    nativeAdView.callToActionView = unifiedAdBinding.adCallToAction
    nativeAdView.iconView = unifiedAdBinding.adAppIcon
    nativeAdView.starRatingView = unifiedAdBinding.adStars
    unifiedAdBinding.adHeadline.text = nativeAd.headline
    if (nativeAd.body == null) {
        unifiedAdBinding.adBody.invisible()
    } else {
        unifiedAdBinding.adBody.show()
        unifiedAdBinding.adBody.text = nativeAd.body
    }

    if (nativeAd.callToAction == null) {
        unifiedAdBinding.adCallToAction.invisible()
    } else {
        unifiedAdBinding.adCallToAction.show()
        unifiedAdBinding.adCallToAction.text = nativeAd.callToAction
    }

    if (nativeAd.icon == null) {
        unifiedAdBinding.adAppIcon.gone()
    } else {
        unifiedAdBinding.adAppIcon.setImageDrawable(nativeAd.icon?.drawable)
        unifiedAdBinding.adAppIcon.show()
    }
    if (nativeAd.starRating == null) {
        unifiedAdBinding.adStars.invisible()
    } else {
        unifiedAdBinding.adStars.rating = nativeAd.starRating!!.toFloat()
        unifiedAdBinding.adStars.show()
    }
    nativeAdView.setNativeAd(nativeAd)
}
