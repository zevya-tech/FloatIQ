package com.harish.floatiq.ui.ocr

object OCRCaptureManager {

    var isScannerOpen = false

    var onCapture: (() -> Unit)? = null
}