package com.harish.floatiq.ui.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.shadow
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import com.harish.floatiq.ui.scientific.ScientificCalculatorEngine
import com.harish.floatiq.ui.scientific.ScientificCalculatorButton
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.platform.LocalContext
import com.harish.floatiq.storage.HistoryStorage
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.viewinterop.AndroidView
import android.view.Gravity
import com.harish.floatiq.ui.components.FeatureGuideDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import android.media.SoundPool
import com.harish.floatiq.R
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.*
import com.harish.floatiq.storage.EngagementTracker
import android.media.AudioAttributes
@Composable
//fun CalculatorScreen() {
fun CalculatorScreen(
    onBack: () -> Unit,
    onOpenHistory: () -> Unit
) {

    var expression by remember {
        mutableStateOf(
            TextFieldValue("")
        )
    }
    var previousExpression by remember {
        mutableStateOf("")
    }
    var memoryValue by remember {
        mutableStateOf(0.0)
    }
    var showScientificFunctions by remember {
        mutableStateOf(false)
    }
    var showCalculatorGuide by remember {
        mutableStateOf(false)
    }

fun insertText(value: String) {

    val currentText =
        expression.text.replace(",", "")

    val start =
        expression.selection.start
            .coerceAtMost(currentText.length)

    val end =
        expression.selection.end
            .coerceAtMost(currentText.length)

    val newText =
        currentText.substring(0, start) +
                value +
                currentText.substring(end)

    expression =
        TextFieldValue(
            text = newText,
            selection =
                TextRange(
                    start + value.length
                )
        )
}
    BackHandler {

        onBack()
    }
    val context = LocalContext.current
    val soundPool = remember {
        SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .build()
    }

    val keyClickSound = remember {
        soundPool.load(
            context,
            R.raw.click12,
            1
        )
    }
    android.util.Log.d(
        "SOUND_TEST",
        "Sound ID = $keyClickSound"
    )
    LaunchedEffect(Unit) {

        soundPool.setOnLoadCompleteListener { _, sampleId, status ->

            android.util.Log.d(
                "SOUND_TEST",
                "Loaded sample=$sampleId status=$status"
            )
        }
    }
    DisposableEffect(Unit) {

        onDispose {
            soundPool.release()
        }
    }

fun playKeySound() {

    val result = soundPool.play(
        keyClickSound,
        1f,
        1f,
        1,
        0,
        1f
    )

    android.util.Log.d(
        "SOUND_TEST",
        "Play result = $result"
    )
}
    val buttons = listOf(
        "C", "%", "/", "⌫",
        "7", "8", "9", "×",
        "4", "5", "6", "-",
        "1", "2", "3", "+",
        "0", ".", "="
    )
    val scientificButtons = listOf(
        "sin",
        "cos",
        "tan",
        "log",
        "ln",
        "√",
        "^2",
        "π",
        "e",
        "(",
        ")"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(20.dp)
        ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),

            horizontalArrangement = Arrangement.SpaceBetween,

            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {

                Text(
                    text = "FloatIQ",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Calculator",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // Guide Button
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
                            RoundedCornerShape(18.dp)
                        )
                        .clickable {
                            showCalculatorGuide = true
                        },

                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = "Calculator Guide",
                        tint = Color.White
                    )
                }

                // History Button
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
                            RoundedCornerShape(18.dp)
                        )
                        .clickable {
                            onOpenHistory()
                        },

                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "History",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                    RoundedCornerShape(28.dp)
                )
                .padding(
                    horizontal = 24.dp,
                    vertical = 36.dp
                ),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom
        ){

            if (previousExpression.isNotEmpty()) {

                Text(
                    text = previousExpression,
                    color =
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Light
                )

                Spacer(modifier = Modifier.height(8.dp))
            }



            AndroidView(


                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),

                factory = { context ->


                    CursorEditText(context).apply {


                        textSize = 36f
                        showSoftInputOnFocus = false

                        isSingleLine = true

                        isCursorVisible = true
                        isFocusable = true
                        isFocusableInTouchMode = true
                        gravity = Gravity.END

                        background = null

                        setTextColor(
                            android.graphics.Color.WHITE
                        )
                    }
                },

                update = { editText ->


                    if (
                        editText.text.toString()
                        != expression.text
                    ) {

                        val cursorPosition =
                            expression.selection.start
                                .coerceAtMost(
                                    expression.text.length
                                )

                        editText.setText(
                            expression.text
                        )

                        editText.setSelection(
                            cursorPosition
                        )
                    }

                    editText.onSelectionChangedCallback =

                        { start, end ->


                            android.util.Log.d(
                                "CURSOR",
                                "start=$start end=$end"
                            )


                            expression =
                                expression.copy(
                                    selection =
                                        TextRange(
                                            start,
                                            end
                                        )
                                )
                        }
                }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (memoryValue != 0.0) {

            Text(

                text = "🧠 Memory: ${memoryValue.toInt()}",

                color =
                    MaterialTheme.colorScheme.onBackground.copy(
                        alpha = 0.7f
                    ),

                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),

            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            listOf("MC", "MR", "M+", "M-").forEach { memory ->

                ScientificCalculatorButton(

                    text = memory,

                    buttonColor = Color(0xFFFF9800),
                    buttonSize = 56.dp,
                    onClick = {
//                        vibrate()
                        playKeySound()
                        when (memory) {

                            "MC" -> {

                                memoryValue = 0.0
                            }

                            "MR" -> {

                                insertText(
                                    memoryValue.toString()
                                )
                            }

                            "M+" -> {


                                val current =
                                    expression.text
                                        .replace(",", "")
                                        .toDoubleOrNull()
                                android.util.Log.d(
                                    "MEMORY",
                                    "Expression=${expression.text}"
                                )

                                android.util.Log.d(
                                    "MEMORY",
                                    "Current=$current"
                                )

                                android.util.Log.d(
                                    "MEMORY",
                                    "Before Memory=$memoryValue"
                                )

                                if (current != null) {

                                    memoryValue += current
                                }
                                android.util.Log.d(
                                    "MEMORY",
                                    "After Memory=$memoryValue"
                                )
                            }

                            "M-" -> {


                                val current =
                                    expression.text
                                        .replace(",", "")
                                        .toDoubleOrNull()
                                if (current != null) {

                                    memoryValue -= current
                                }
                            }
                        }
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {

                    showScientificFunctions =
                        !showScientificFunctions
                }
                .padding(vertical = 8.dp),

            horizontalArrangement =
                Arrangement.SpaceBetween,

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                text = "Scientific Functions",

                color =
                    MaterialTheme.colorScheme.onBackground.copy(
                        alpha = 0.7f
                    ),

                fontSize = 14.sp,

                fontWeight = FontWeight.SemiBold
            )

            Text(
                text =
                    if (showScientificFunctions)
                        "▲"
                    else
                        "▼",

                color =
                    MaterialTheme.colorScheme.onBackground,

                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        if (showScientificFunctions) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(
                        rememberScrollState()
                    )
            ) {

                scientificButtons.forEach { button ->

                    ScientificCalculatorButton(

                        text = button,

                        onClick = {
                            playKeySound()
                            when (button) {


                                "sin" -> {

                                    if (
                                        expression.text == "Error" ||
                                        expression.text == "Unsupported"
                                    ) {

                                        expression =
                                            TextFieldValue("sin(")

                                    } else {

                                        insertText("sin(")
                                    }
                                }

                                "cos" -> {


                                    if (
                                        expression.text == "Error" ||
                                        expression.text == "Unsupported"
                                    ) {

                                        expression =
                                            TextFieldValue("cos(")

                                    } else {

                                        insertText("cos(")
                                    }
                                }

                                "tan" -> {


                                    if (
                                        expression.text == "Error" ||
                                        expression.text == "Unsupported"
                                    ) {

                                        expression =
                                            TextFieldValue("tan(")

                                    } else {

                                        insertText("tan(")
                                    }
                                }

                                "log" -> {


                                    if (
                                        expression.text == "Error" ||
                                        expression.text == "Unsupported"
                                    ) {

                                        expression =
                                            TextFieldValue("log(")

                                    } else {

                                        insertText("log(")
                                    }
                                }

                                "ln" -> {


                                    if (
                                        expression.text == "Error" ||
                                        expression.text == "Unsupported"
                                    ) {

                                        expression =
                                            TextFieldValue("ln(")

                                    } else {

                                        insertText("ln(")
                                    }
                                }

                                "^2" -> {

                                    if (
                                        expression.text == "Error" ||
                                        expression.text == "Unsupported"
                                    ) {

                                        expression = TextFieldValue("^2")

                                    } else {

                                        insertText("^2")
                                    }
                                }

                                else -> {


                                    if (
                                        expression.text == "Error" ||
                                        expression.text == "Unsupported"
                                    ) {

                                        expression =
                                            TextFieldValue(button)

                                    } else {

                                        insertText(button)
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
            fun deleteOneCharacter() {

                val start = expression.selection.start
                val end = expression.selection.end

                if (start != end) {

                    val newText =
                        expression.text.removeRange(start, end)

                    expression =
                        TextFieldValue(
                            text = newText,
                            selection = TextRange(start)
                        )
                }
                else if (start > 0) {

                    val newText =
                        expression.text.removeRange(
                            start - 1,
                            start
                        )

                    expression =
                        TextFieldValue(
                            text = newText,
                            selection =
                                TextRange(start - 1)
                        )
                }
            }
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
//            verticalArrangement = Arrangement.spacedBy(14.dp),
//            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),


            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()

        ) {

            items(buttons) { button ->
                if (button == "⌫") {

                    var deleteJob by remember {
                        mutableStateOf<Job?>(null)
                    }

                    Box(
                        modifier = Modifier
                            .padding(3.dp)
                            .aspectRatio(1f)
                            .shadow(
                                elevation = 10.dp,
                                shape = RoundedCornerShape(22.dp)
                            )
                            .background(
                                MaterialTheme.colorScheme.surface,
                                RoundedCornerShape(22.dp)
                            )
                            .pointerInput(Unit) {

                                detectTapGestures(

                                    onPress = {

                                        playKeySound()
                                        deleteOneCharacter()

                                        deleteJob =
                                            CoroutineScope(
                                                Dispatchers.Main
                                            ).launch {

                                                delay(350)

                                                while (isActive) {

                                                    deleteOneCharacter()

                                                    delay(50)
                                                }
                                            }

                                        tryAwaitRelease()

                                        deleteJob?.cancel()
                                    }
                                )
                            },

                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "⌫",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                else {

                    CalculatorButton(
                        text = button,
                        onClick = {
                            playKeySound()

                            when (button) {

                                "C" -> {
                                    expression = TextFieldValue("")
                                    previousExpression = ""
                                }

                                "=" -> {
                                    val cleanExpression =
                                        expression.text.replace(",", "")

                                    val result =
                                        ScientificCalculatorEngine
                                            .evaluate(cleanExpression)

                                    HistoryStorage.saveCalculation(
                                        context,
                                        cleanExpression,
                                        result
                                    )
                                    if (
                                        result != "Error" &&
                                        result != "Unsupported"
                                    ) {

                                        EngagementTracker
                                            .onSuccessfulCalculation(
                                                context
                                            )
                                    }

                                    previousExpression =
                                        cleanExpression

//                                    expression =
//                                        TextFieldValue(result)
                                    expression =
                                        TextFieldValue(
                                            text = result,
                                            selection =
                                                TextRange(
                                                    result.length
                                                )
                                        )
                                }

                                else -> {
                                    insertText(button)
                                }
                            }
                        }
                    )
                }

//                CalculatorButton(
//                    text = button,
//                    onClick = {
//                        playKeySound()
//                        when (button) {
//
//                            "C" -> {
//                                expression =
//                                    TextFieldValue("")
//                                previousExpression = ""
//                            }
//
//
//                            "⌫" -> {
//
//                                val start =
//                                    expression.selection.start
//
//                                val end =
//                                    expression.selection.end
//
//                                if (start != end) {
//
//                                    val newText =
//                                        expression.text.removeRange(
//                                            start,
//                                            end
//                                        )
//
//                                    expression =
//                                        TextFieldValue(
//                                            text = newText,
//
//                                            selection =
//                                                TextRange(start)
//                                        )
//                                }
//
//                                else if (start > 0) {
//
//                                    val newText =
//                                        expression.text.removeRange(
//                                            start - 1,
//                                            start
//                                        )
//
//                                    expression =
//                                        TextFieldValue(
//                                            text = newText,
//
//                                            selection =
//                                                TextRange(
//                                                    start - 1
//                                                )
//                                        )
//                                }
//                            }
//
//                            "=" -> {
//
//                                val cleanExpression =
//                                    expression.text.replace(",", "")
//
//                                val result =
//                                    ScientificCalculatorEngine
//                                        .evaluate(cleanExpression)
//
//                                HistoryStorage.saveCalculation(
//                                    context,
//                                    cleanExpression,
//                                    result
//                                )
//
//                                previousExpression =
//                                    cleanExpression
//
//                                expression =
//                                    TextFieldValue(result)
//                            }
//
//                            else -> {
//                                insertText(button)
//                            }
//                        }
//                    }
//                )
            }
        }
    }
        }
    if (showCalculatorGuide) {

        FeatureGuideDialog(

            title = "🧮 Calculator Guide",

            guideText =
                """
• Basic Arithmetic
• Scientific Functions
• Trigonometry
• Logarithms
• Memory Operations
• Calculation History

How to Use

1. Enter an expression
2. Use scientific functions if needed
3. Press =
4. View result
5. Access previous calculations from History

Scientific Symbols

sin(x) = Sine of angle x

cos(x) = Cosine of angle x

tan(x) = Tangent of angle x

log(x) = Base-10 logarithm

ln(x) = Natural logarithm

√(x) = Square root

x² = Square of a number

π = Pi (3.14159265...)

e = Euler's number (2.7182818...)

( ) = Group calculations

% = Percentage

Memory Buttons

MC = Clear Memory

MR = Recall Memory

M+ = Add current value to memory

M- = Subtract current value from memory

Examples

sin(30) = 0.5

cos(60) = 0.5

√(81) = 9

log(100) = 2

ln(e) = 1

2π = 6.28318

(5+5)^2 = 100

100+10% = 110

200-25% = 150

50% = 0.5
""".trimIndent(),

            onDismiss = {
                showCalculatorGuide = false
            }
        )
    }
}

@Composable
fun CalculatorButton(
    text: String,
    onClick: () -> Unit
) {


    val buttonColor =
        when (text) {

            "=" -> Color(0xFF00C853)

            "C" -> Color(0xFFDC2626)


            "%", "/", "×", "-", "+" ->
                MaterialTheme.colorScheme.primary

            else ->
                MaterialTheme.colorScheme.surface
        }



    Box(
        modifier = Modifier
            .padding(3.dp)
            .aspectRatio(1f)


            .shadow(
                elevation = 10.dp,
                RoundedCornerShape(22.dp),
                ambientColor = buttonColor.copy(alpha = 0.4f),
                spotColor = buttonColor.copy(alpha = 0.4f)
            )

            .background(
                buttonColor,
                RoundedCornerShape(22.dp)
            )


            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            color =
                MaterialTheme.colorScheme.onSurface,
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium
        )
    }
}