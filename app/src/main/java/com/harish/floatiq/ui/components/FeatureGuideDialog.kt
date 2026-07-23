package com.harish.floatiq.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.FilledTonalButton
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
@Composable
fun FeatureGuideDialog(
    title: String,
    guideText: String,
    onDismiss: () -> Unit
) {

    AlertDialog(

        onDismissRequest = onDismiss,

//        confirmButton = {
//
//            TextButton(
//                onClick = onDismiss
//            ) {
//                Text("Close")
//            }
//        },

        confirmButton = {

            FilledTonalButton(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "✓ Got It"
                )
            }
        },

        title = {
            Text(title)
        },

        text = {

            Column(
                modifier = Modifier
                    .heightIn(max = 500.dp)
                    .verticalScroll(
                        rememberScrollState()
                    )
            ) {

//                Text(
//                    """
//🫧 Floating Assistant
//
//• Works over any app
//• Quick access to tools
//• Drag bubble anywhere
//
//How to use:
//1. Grant overlay permission
//2. Tap Floating Assistant
//3. Use tools anywhere
//
//────────────────────
//
//🧮 Calculator
//
//• Scientific Calculator
//• Trigonometry
//• Logarithms
//• Algebra Support
//
//How to use:
//1. Enter expression
//2. Press =
//3. View result
//
//────────────────────
//
//📸 OCR Scanner
//
//• Scan equations
//• Scan text
//• Detect phone numbers
//• Detect emails
//• Detect websites
//
//How to use:
//1. Point camera
//2. Capture text
//3. Calculate if math detected
//
//────────────────────
//
//📏 Unit Converter
//
//• Length
//• Weight
//• Area
//• Volume
//• Speed
//• Time
//
//How to use:
//1. Select category
//2. Enter value
//3. Choose units
//
//────────────────────
//
//💱 Currency Converter
//
//• Live exchange rates
//
//How to use:
//1. Select currencies
//2. Enter amount
//
//────────────────────
//
//❤️ Health Hub
//
//• BMI
//• Calories
//• Water Intake
//• Sleep Calculator
//• Age Calculator
//
//────────────────────
//
//🧠 AI Formula Explorer
//
//• Formula search
//• Explanations
//• Variables
//• Examples
//                    """.trimIndent()
//                )
                Text(
                    text = guideText
                )
            }
        }
    )
}