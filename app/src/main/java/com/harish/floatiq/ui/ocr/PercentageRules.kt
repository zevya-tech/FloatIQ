package com.harish.floatiq.ui.ocr

object PercentageRules {

    fun parse(
        text: String
    ): String? {

        val regex1 =
            Regex(
                """(\d+)\s*percent\s*of\s*(\d+)"""
            )

        regex1.find(text)?.let {

            val percent =
                it.groupValues[1].toDouble()

            val value =
                it.groupValues[2].toDouble()

            return "$value*${percent/100}"
        }

        val regex2 =
            Regex(
                """(\d+)%\s*of\s*(\d+)"""
            )

        regex2.find(text)?.let {

            val percent =
                it.groupValues[1].toDouble()

            val value =
                it.groupValues[2].toDouble()

            return "$value*${percent/100}"
        }

        return null
    }
}