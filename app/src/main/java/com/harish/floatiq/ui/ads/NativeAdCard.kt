
package com.harish.floatiq.ui.ads
//import android.graphics.Color
//import android.graphics.Typeface
//import android.util.Log
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.FrameLayout
//import android.widget.LinearLayout
//import android.widget.TextView
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.wrapContentHeight
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.viewinterop.AndroidView
//import com.google.android.gms.ads.AdListener
//import com.google.android.gms.ads.AdLoader
//import com.google.android.gms.ads.AdRequest
//import com.google.android.gms.ads.LoadAdError
//import com.google.android.gms.ads.nativead.MediaView
//import com.google.android.gms.ads.nativead.NativeAd
//import com.google.android.gms.ads.nativead.NativeAdView
//
//@Composable
//fun NativeAdCard() {
//    val context = LocalContext.current
//
//    // Track the loaded ad so we can destroy it when the composable leaves the screen
//    var loadedNativeAd by remember { mutableStateOf<NativeAd?>(null) }
//    var adFailedToLoad by remember { mutableStateOf(false) }
//
//    // Kick off the Ad Loading process ONLY ONCE when this Composable enters the composition
//    LaunchedEffect(Unit) {
//        val adLoader = AdLoader.Builder(context, AdConstants.NATIVE_AD_UNIT_ID)
//            .forNativeAd { nativeAd ->
//                // Store the loaded ad inside our state variable
//                loadedNativeAd = nativeAd
//            }
//            .withAdListener(object : AdListener() {
//                override fun onAdLoaded() {
//                    Log.d("FLOATIQ_ADS", "Native Ad Loaded")
//                    adFailedToLoad = false
//                }
//
//                override fun onAdFailedToLoad(error: LoadAdError) {
//                    Log.e(
//                        "FLOATIQ_ADS",
//                        "Code=${error.code}, Message=${error.message}"
//                    )
//                    adFailedToLoad = true
//                }
//            })
//            .build()
//
//        adLoader.loadAd(AdRequest.Builder().build())
//    }
//
//    // CRITICAL: Prevent memory leaks by destroying the ad when leaving the screen
//    DisposableEffect(Unit) {
//        onDispose {
//            loadedNativeAd?.destroy()
//        }
//    }
//
//    // UI Rendering Logic
//    if (adFailedToLoad) {
//        // If the ad fails to load completely, hide the widget or show a placeholder
//        Box(modifier = Modifier.fillMaxWidth().wrapContentHeight())
//    } else if (loadedNativeAd != null) {
//        // Only render the AndroidView structure once the ad has successfully arrived
//        AndroidView(
//            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
//            factory = { ctx ->
//                val nativeAdView = NativeAdView(ctx)
//
//                val layout = LinearLayout(ctx).apply {
//                    orientation = LinearLayout.VERTICAL
//                    setPadding(40, 40, 40, 40)
//                    setBackgroundColor(Color.parseColor("#0F172A"))
//                    elevation = 20f
//                }
//
//                val sponsored = TextView(ctx).apply {
//                    text = "Sponsored"
//                    textSize = 12f
//                    setTextColor(Color.parseColor("#94A3B8"))
//                }
//                layout.addView(sponsored)
//
////                val mediaView = MediaView(ctx).apply {
////                    layoutParams = LinearLayout.LayoutParams(
////                        LinearLayout.LayoutParams.MATCH_PARENT,
////                        400
////                    )
////                }
//                val screenWidth =
//                    ctx.resources.displayMetrics.widthPixels
//
//                val mediaView = MediaView(ctx).apply {
////
//
//                    layoutParams =
//                        LinearLayout.LayoutParams(
//                            LinearLayout.LayoutParams.MATCH_PARENT,
//                            (screenWidth * 9f / 16f).toInt()
//                        )
//                }
//                layout.addView(mediaView)
//
//                val headline = TextView(ctx).apply {
//                    textSize = 20f
//                    setTypeface(null, Typeface.BOLD)
//                    setTextColor(Color.WHITE)
//                    setPadding(0, 20, 0, 16)
//                }
//                layout.addView(headline)
//
//                val body = TextView(ctx).apply {
//                    textSize = 15f
//                    setTextColor(Color.parseColor("#CBD5E1"))
//                    setPadding(0, 0, 0, 20)
//                }
//                layout.addView(body)
//
//                val cta = Button(ctx).apply {
//                    setBackgroundColor(Color.parseColor("#7C3AED"))
//                    setTextColor(Color.WHITE)
//                    layoutParams = LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT
//                    )
//                }
//                layout.addView(cta)
//
//                nativeAdView.addView(
//                    layout,
//                    FrameLayout.LayoutParams(
//                        FrameLayout.LayoutParams.MATCH_PARENT,
//                        FrameLayout.LayoutParams.WRAP_CONTENT
//                    )
//                )
//
//                // Bind references to the parent NativeAdView container
//                nativeAdView.headlineView = headline
//                nativeAdView.bodyView = body
//                nativeAdView.callToActionView = cta
//                nativeAdView.mediaView = mediaView
//
//                nativeAdView
//            },
//            update = { view ->
//                // This block runs dynamically when loadedNativeAd updates
//                loadedNativeAd?.let { ad ->
//                    val headline = view.headlineView as? TextView
//                    val body = view.bodyView as? TextView
//                    val cta = view.callToActionView as? Button
//                    val media = view.mediaView as? MediaView
//
//                    headline?.text = ad.headline
//                    body?.text = ad.body ?: ""
//                    cta?.text = ad.callToAction ?: ""
//                    media?.mediaContent = ad.mediaContent
//
//                    // Assign the native asset container over to Google AdMob for tracking clicks/impressions
//                    view.setNativeAd(ad)
//                }
//            }
//        )
//    }
//}