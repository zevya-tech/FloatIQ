package com.harish.floatiq.ui.utils

import java.text.DecimalFormat

object NumberFormatter {

    private val formatter =
        DecimalFormat("#,###.########")

    fun format(
        value: Double
    ): String {

        return formatter.format(value)
    }
}