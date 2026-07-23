package com.harish.floatiq.ui.ocr

object OCRQuestionProcessor {

    fun process(
        text: String
    ): OCRQuestionResult {

        val lower =
            text.lowercase()

        PercentageRules.parse(lower)?.let {

            return OCRQuestionResult(
                isQuestion = true,
                expression = it,
                detectedType = "Percentage Problem"
            )
        }

        GeometryRules.parse(lower)?.let {

            return OCRQuestionResult(
                isQuestion = true,
                expression = it,
                detectedType = "Geometry Problem"
            )
        }

        AlgebraRules.parse(text)?.let {

            return OCRQuestionResult(
                isQuestion = true,
                expression = it,
                detectedType = "Algebra Problem"
            )
        }

        WordProblemRules.parse(lower)?.let {

            return OCRQuestionResult(
                isQuestion = true,
                expression = it,
                detectedType = "Word Problem"
            )
        }

        return OCRQuestionResult(
            isQuestion = false,
            expression = text,
            detectedType = "Expression"
        )
    }
}