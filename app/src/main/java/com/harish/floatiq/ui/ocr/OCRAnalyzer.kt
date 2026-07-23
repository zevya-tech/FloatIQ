package com.harish.floatiq.core

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class OCRAnalyzer(

    private val onTextDetected: (String) -> Unit

) : ImageAnalysis.Analyzer {

    private val recognizer =
        TextRecognition.getClient(
            TextRecognizerOptions.DEFAULT_OPTIONS
        )

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(
        imageProxy: ImageProxy
    ) {

        val mediaImage =
            imageProxy.image

        if (mediaImage != null) {

            val image =
                InputImage.fromMediaImage(
                    mediaImage,
                    imageProxy.imageInfo.rotationDegrees
                )


            recognizer.process(image)

                .addOnSuccessListener { visionText ->

                    val result = StringBuilder()

                    val imageWidth =
                        image.width

                    val imageHeight =
                        image.height


                    val scanLeft =
                        (imageWidth * 0.1f).toInt()

                    val scanRight =
                        (imageWidth * 0.9f).toInt()

                    val scanTop =
                        (imageHeight * 0.25f).toInt()

                    val scanBottom =
                        (imageHeight * 0.75f).toInt()


                    visionText.textBlocks.forEach { block ->

                        val box =
                            block.boundingBox ?: return@forEach


                        if (
                            box.right > scanLeft &&
                            box.left < scanRight &&
                            box.bottom > scanTop &&
                            box.top < scanBottom
                        ){

                            result.append(block.text)
                            result.append("\n")
                        }
                    }

                    onTextDetected(
                        result.toString().trim()
                    )
                }

                .addOnCompleteListener {

                    imageProxy.close()
                }
        } else {

            imageProxy.close()
        }
    }
}