package com.harish.floatiq.ui.ocr

object AlgebraRules {

    fun parse(
        text: String
    ): String? {

        val lower =
            text.lowercase()

        if (
            lower.contains("solve") &&
            text.contains("=")
        ) {

            return text
                .replace(
                    "Solve",
                    "",
                    true
                )
                .trim()
        }

        return null
    }
}