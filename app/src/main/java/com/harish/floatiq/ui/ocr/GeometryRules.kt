package com.harish.floatiq.ui.ocr

object GeometryRules {

    fun parse(
        text: String
    ): String? {

        if (
            text.contains("area") &&
            text.contains("circle")
        ) {

            val radiusRegex =
                Regex(
                    """radius\s*(\d+(\.\d+)?)"""
                )

            radiusRegex.find(text)?.let {

                val r =
                    it.groupValues[1]

                return "π*$r^2"
            }
        }

        return null
    }
}