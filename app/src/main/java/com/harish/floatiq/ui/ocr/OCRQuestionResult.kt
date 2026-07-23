package com.harish.floatiq.ui.ocr

data class OCRQuestionResult(

    val isQuestion: Boolean,

    val expression: String,

    val detectedType: String
)