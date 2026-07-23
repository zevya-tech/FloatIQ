
package com.harish.floatiq.overlay

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.TextView

import android.view.View
object OverlayPanelFactory {



fun createUnitConverterPanel(
    context: Context
): View {

    return OverlayUnitConverterView
        .create(context)
}

//    fun createCurrencyPanel(
//        context: Context
//    ): TextView {
//
//        return TextView(context).apply {
//
//            text = "Currency Converter\nComing Soon"
//
//            textSize = 24f
//
//            gravity = Gravity.CENTER
//
//            setTextColor(Color.WHITE)
//        }
//    }
fun createCurrencyPanel(
    context: Context
): View {

    return OverlayCurrencyConverterView
        .create(context)
}
}