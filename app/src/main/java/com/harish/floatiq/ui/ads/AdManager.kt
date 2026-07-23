package com.harish.floatiq.ui.ads

//import android.content.Context
//import android.util.Log
//import com.google.android.gms.ads.AdLoader
//import com.google.android.gms.ads.AdRequest
//import com.google.android.gms.ads.LoadAdError
//import com.google.android.gms.ads.AdListener
//import com.google.android.gms.ads.nativead.NativeAd
//
//object AdManager {
//
//    var cachedNativeAd: NativeAd? = null
//        private set
//
//    fun loadNativeAd(
//        context: Context
//    ) {
//
//        if (cachedNativeAd != null) {
//
//            Log.d(
//                "FLOATIQ_ADS",
//                "Using Cached Native Ad"
//            )
//
//            return
//        }
//
//        AdLoader.Builder(
//            context,
//            AdConstants.NATIVE_AD_UNIT_ID
//        )
//
//            .forNativeAd { nativeAd ->
//
//                cachedNativeAd?.destroy()
//
//                cachedNativeAd =
//                    nativeAd
//
//                Log.d(
//                    "FLOATIQ_ADS",
//                    "Native Ad Cached"
//                )
//            }
//
//            .withAdListener(
//                object : AdListener() {
//
//                    override fun onAdFailedToLoad(
//                        error: LoadAdError
//                    ) {
//
//                        Log.e(
//                            "FLOATIQ_ADS",
//                            "Cache Failed: ${error.message}"
//                        )
//                    }
//                }
//            )
//
//            .build()
//
//            .loadAd(
//                AdRequest.Builder().build()
//            )
//    }
//
//    fun clearAd() {
//
//        cachedNativeAd?.destroy()
//
//        cachedNativeAd = null
//    }
//}