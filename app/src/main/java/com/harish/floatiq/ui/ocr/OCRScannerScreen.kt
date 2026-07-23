
package com.harish.floatiq.ui.ocr
import com.google.accompanist.permissions.*
import androidx.compose.runtime.LaunchedEffect
import android.Manifest
import android.util.Size
import androidx.activity.compose.BackHandler
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.harish.floatiq.core.OCRAnalyzer
import java.util.concurrent.Executors
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.platform.LocalClipboardManager
import com.harish.floatiq.ui.scientific.ScientificCalculatorEngine
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import android.content.Intent
import android.net.Uri
import com.harish.floatiq.ui.scientific.MathSolutionEngine
import android.speech.tts.TextToSpeech
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import java.util.Locale
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import com.harish.floatiq.ui.ai.ProblemClassifier
import com.harish.floatiq.ui.ai.ProblemInfo
import com.google.common.util.concurrent.ListenableFuture
import androidx.compose.foundation.layout.safeDrawingPadding
import com.harish.floatiq.ui.components.FeatureGuideDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.foundation.clickable
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import kotlinx.coroutines.delay
import androidx.compose.animation.togetherWith
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Navigation
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material.icons.rounded.Calculate
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Article
import androidx.compose.material.icons.rounded.FormatListNumbered
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.camera.core.Camera
import androidx.compose.material.icons.rounded.FlashOn
import androidx.compose.material.icons.rounded.FlashOff
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import android.provider.Settings
import android.util.Log
import com.harish.floatiq.storage.EngagementTracker
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.material3.SheetValue
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.material.icons.rounded.Settings
@OptIn(ExperimentalPermissionsApi::class,
    ExperimentalMaterial3Api::class)
@Composable
fun OCRScannerScreen(
    onBack: () -> Unit
) {

    BackHandler {
        onBack()
    }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var textToSpeech by remember {
        mutableStateOf<TextToSpeech?>(null)
    }
    var showOCRGuide by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded,
    skipHiddenState = true
    )

    val bottomSheetScaffoldState =  rememberBottomSheetScaffoldState(
            bottomSheetState = sheetState
            )
    LaunchedEffect(Unit) {

        textToSpeech =
            TextToSpeech(context) { status ->

                if (status == TextToSpeech.SUCCESS) {

                    textToSpeech?.language = Locale.US
                    textToSpeech?.setPitch(1.1f)
                    textToSpeech?.setSpeechRate(0.9f)
                }
            }
    }
    var camera by remember {
        mutableStateOf<Camera?>(null)
    }

    var flashEnabled by remember {
        mutableStateOf(false)
    }
    DisposableEffect(Unit) {


    onDispose {
        camera?.cameraControl?.enableTorch(false)

            textToSpeech?.stop()
            textToSpeech?.shutdown()
        }
    }
    val lifecycleOwner =
        LocalLifecycleOwner.current
    var canSolve by remember {
        mutableStateOf(false)
    }
    val cameraExecutor =
        remember {
            Executors.newSingleThreadExecutor()
        }

//    DisposableEffect(Unit) {
    DisposableEffect(cameraExecutor) {
        onDispose {

            cameraExecutor.shutdown()
        }
    }
    val cameraPermissionState =
        rememberPermissionState(
            Manifest.permission.CAMERA
        )

    LaunchedEffect(Unit) {

        cameraPermissionState.launchPermissionRequest()
    }

    if (!cameraPermissionState.status.isGranted) {

        val shouldShowRationale =
            cameraPermissionState.status.shouldShowRationale


        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Icon(
                    imageVector = Icons.Default.DocumentScanner,
                    contentDescription = null,
                    modifier = Modifier.size(72.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "OCR Scanner",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Camera access is required to scan text, equations, documents and QR-style information.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Icon(
                                imageVector = Icons.Rounded.CameraAlt,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = "Camera Permission",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text =
                                if (shouldShowRationale)
                                    "Allow camera access so FloatIQ can scan and recognize text in real time."
                                else
                                    "Camera permission has been disabled. Enable it from Settings to continue using the OCR Scanner.",

                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {

                                if (shouldShowRationale) {

                                    cameraPermissionState.launchPermissionRequest()

                                } else {

                                    val intent =
                                        Intent(
                                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                        ).apply {

                                            data = Uri.fromParts(
                                                "package",
                                                context.packageName,
                                                null
                                            )
                                        }

                                    context.startActivity(intent)
                                }
                            }
                        ) {

                            Icon(
                                imageVector =
                                    if (shouldShowRationale)
                                        Icons.Rounded.CameraAlt
                                    else
                                        Icons.Rounded.Settings,
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                if (shouldShowRationale)
                                    "Grant Permission"
                                else
                                    "Open Settings"
                            )
                        }
                    }
                }
            }
        }
        return
    }
    var liveText by remember {
        mutableStateOf("")
    }
    var previousLiveText by remember {
        mutableStateOf("")
    }
    var lastOCRUpdateTime by remember {
        mutableStateOf(0L)
    }
    var scannedText by remember {
        mutableStateOf("")
    }

    var detectedType by remember {
        mutableStateOf("Waiting for scan...")
    }
    var calculationResult by remember {
        mutableStateOf("")
    }
    var interpretedExpression by remember {
        mutableStateOf("")
    }
    var solutionSteps by remember {
        mutableStateOf<List<String>>(emptyList())
    }
    var problemInfo by remember {
        mutableStateOf<ProblemInfo?>(null)
    }

    var isSpeaking by remember {
        mutableStateOf(false)
    }
//    val sheetPeakHeight=197.dp
    val configuration = LocalConfiguration.current

    val cameraHeight =
        if (configuration.screenHeightDp < 700)
            360.dp
        else
            450.dp
    val clipboardManager =
        LocalClipboardManager.current


    val sheetPeekHeight =
        when {

            configuration.screenHeightDp < 650 ->
                240.dp

            configuration.screenHeightDp < 700 ->
                260.dp

            configuration.screenHeightDp < 800 ->
                280.dp

            else ->
                300.dp
        }
    fun captureOCR() {

        // Clear previous scan results
        calculationResult = ""
        solutionSteps = emptyList()
        interpretedExpression = ""
        problemInfo = null
        canSolve = false
        isSpeaking = false
        detectedType = "Analyzing..."
        scannedText = liveText
        if (
            scannedText.contains("@") ||
            Regex(
                "(gmail|yahoo|outlook|hotmail|icloud)\\.?",
                RegexOption.IGNORE_CASE
            ).containsMatchIn(scannedText)
        ) {
            scannedText = normalizeEmail(scannedText)
        }
        val questionResult =
            OCRQuestionProcessor.process(
                scannedText
            )
        android.util.Log.d(
            "OCR_PROCESS",
            """
Text: $scannedText
Question: ${questionResult.isQuestion}
Expression: ${questionResult.expression}
Type: ${questionResult.detectedType}
""".trimIndent()
        )
        if (questionResult.isQuestion) {

            detectedType =
                questionResult.detectedType
        }
        interpretedExpression =
            questionResult.expression

        problemInfo = null
        android.util.Log.d(
            "ProblemClassifier",
            problemInfo.toString()
        )

        val cleanedText =
            OCRProcessor.normalizeMath(
                scannedText
            )
                .trim()
        Log.d(
            "OCR_DEBUG",
            """
cleanedText = $cleanedText
containsDigit = ${cleanedText.any { it.isDigit() }}
containsSin = ${cleanedText.contains("sin")}
containsCos = ${cleanedText.contains("cos")}
containsTan = ${cleanedText.contains("tan")}
containsLog = ${cleanedText.contains("log")}
containsLn = ${cleanedText.contains("ln")}
containsSqrt = ${cleanedText.contains("sqrt")}
containsPi = ${cleanedText.contains("pi")}
hasEulerNumber = ${'$'}{hasEulerNumber}
contains² = ${cleanedText.contains("²")}
contains³ = ${cleanedText.contains("³")}
matchesMathRegex = ${cleanedText.matches(MATH_REGEX)}
""".trimIndent()
        )
        val hasEulerNumber =
            cleanedText == "e" ||
                    cleanedText.contains("e^") ||
                    cleanedText.contains("(e)") ||
                    cleanedText.contains("sin(e)") ||
                    cleanedText.contains("cos(e)") ||
                    cleanedText.contains("tan(e)") ||
                    cleanedText.contains("ln(e)") ||
                    cleanedText.contains("log(e)")
        val hasMathOperator =
            cleanedText.contains("+") ||
                    cleanedText.contains("-") ||
                    cleanedText.contains("*") ||
                    cleanedText.contains("/") ||
                    cleanedText.contains("^") ||
                    cleanedText.contains("=")

        val isPureNumber =
            cleanedText.matches(
                Regex("""^\d+(\.\d+)?$""")
            )
        detectedType =
            when {

                scannedText.replace(" ", "")
                    .matches(
                        PHONE_REGEX
                    ) ->
                    "📞 Phone Number"

                URL_REGEX.matches(scannedText.trim()) ->
                    "🌐 Website / URL"

                EMAIL_REGEX.matches(
                    normalizeEmail(scannedText)
                ) -> "📧 Email Address"

                isWifiCredentials(scannedText) ->
                    "📶 WiFi Credentials"

                ADDRESS_REGEX.containsMatchIn(scannedText) ->
                    "📍 Address"

                scannedText.contains("=") &&
                        scannedText.contains("x")
                    -> "📘 Algebra Equation"

                cleanedText.matches(

                    MATH_REGEX
                ) &&
                        (
                                isPureNumber ||
                                        hasMathOperator ||
                                        cleanedText.contains("sin(") ||
                                        cleanedText.contains("cos(") ||
                                        cleanedText.contains("tan(") ||
                                        cleanedText.contains("log(") ||
                                        cleanedText.contains("ln(") ||
                                        cleanedText.contains("sqrt(") ||
                                        cleanedText.contains("π") ||
                                        cleanedText.contains("pi") ||
                                        hasEulerNumber ||
                                        cleanedText.contains("²") ||
                                        cleanedText.contains("³")
                                ) -> "🧮 Math Expression"


                scannedText.length > 100 ->
                    "📄 Document Text"

                else ->
                    "📝 General Text"
            }
        canSolve =
            detectedType == "🧮 Math Expression" ||
                    detectedType == "📘 Algebra Equation"
        Log.d(
            "OCR_DEBUG",
            """
DetectedType: $detectedType
CanSolve: $canSolve
ScannedText: $scannedText
CleanedText: $cleanedText
""".trimIndent()
        )

        if (
            detectedType == "🧮 Math Expression" ||
            detectedType == "📘 Algebra Equation"
        ) {
            problemInfo =
                ProblemClassifier.classify(
                    scannedText
                )
            Log.d(
                "OCR_DEBUG",
                "ProblemInfo = $problemInfo"
            )
        }
        if (scannedText.isNotBlank()) {

            EngagementTracker
                .onSuccessfulCalculation(
                    context
                )
        }
        scope.launch {
            bottomSheetScaffoldState.bottomSheetState.expand()
        }
    }
    DisposableEffect(Unit) {

        OCRCaptureManager.isScannerOpen = true
        OCRCaptureManager.onCapture = {
            captureOCR()
        }

        onDispose {
            OCRCaptureManager.isScannerOpen = false
            OCRCaptureManager.onCapture = null
        }
    }
    BottomSheetScaffold(

        scaffoldState = bottomSheetScaffoldState,

        sheetPeekHeight = sheetPeekHeight,
        sheetContainerColor = MaterialTheme.colorScheme.surface,

        sheetContent = {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
            ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AssistChip(
                        onClick = {},
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor =
                                MaterialTheme.colorScheme.primary
                        ),
                        label = {

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Rounded.Visibility,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )

                                Spacer(modifier = Modifier.width(6.dp))

                                Text("Live Detection Active")
                            }


                        }
                    )


                    FilledTonalButton(
                        onClick = {
                            captureOCR()
                        },
                        contentPadding = PaddingValues(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        )
                    ) {

                        Icon(
                            Icons.Rounded.CameraAlt,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text("Scan")
                    }
                }
                Spacer(
                    modifier = Modifier.height(12.dp)
                )
                Text(
                        "Tip: You can also press Volume Up to Scan",
                        style = MaterialTheme.typography.bodySmall
                )
                Spacer(
                    modifier = Modifier.height(12.dp)
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor =
                            MaterialTheme.colorScheme.surface
                    )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "LIVE DETECTION",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLarge
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(
                                    rememberScrollState()
                                )
                        ) {

                            Text(
                                text =
                                    if (liveText.isBlank())
                                        "Point camera at text..."
                                    else
                                        liveText,

                                color = Color.White
                            )
                        }
                    }
                }
                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Rounded.Article,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Scanned Text",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor =
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Characters: ${scannedText.length}",
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text = detectedType,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLarge
                        )

                        if (scannedText.isBlank()) {

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Icon(
                                    Icons.Rounded.CameraAlt,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                                )

                                Spacer(
                                    modifier = Modifier.width(8.dp)
                                )

                                Text(
                                    text = "Capture text, equations, links and more",
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                                )
                            }

                        } else {

                            Text(
                                text = scannedText,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (
                                interpretedExpression.isNotBlank() &&
                                interpretedExpression != scannedText
                            ) {

                                Spacer(
                                    modifier = Modifier.height(8.dp)
                                )

                                Text(
                                    text = "Interpreted Expression",
                                    color =
                                        MaterialTheme.colorScheme.primary
                                )

                                Text(
                                    text = interpretedExpression,
                                    color = Color.White
                                )
                            }
                        }
                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )
                        if (
                            detectedType == "🧮 Math Expression" ||
                            detectedType == "📘 Algebra Equation"
                        ) {
//                    if (canSolve){
                            problemInfo?.let { info ->

                                Spacer(
                                    modifier = Modifier.height(12.dp)
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {

                                    Box(
                                        modifier = Modifier.weight(1f)
                                    ) {

                                        OCRResultCard(
                                            title = "Category",
                                            value = info.category.name
                                        )
                                    }

                                    Spacer(
                                        modifier = Modifier.width(8.dp)
                                    )

                                    Box(
                                        modifier = Modifier.weight(1f)
                                    ) {

                                        OCRResultCard(
                                            title = "Type",
                                            value = info.type.name
                                        )
                                    }
                                }

                                Spacer(
                                    modifier = Modifier.height(8.dp)
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {

                                    Box(
                                        modifier = Modifier.weight(1f)
                                    ) {

                                        OCRResultCard(
                                            title = "Method",
                                            value = info.method
                                        )
                                    }

                                    Spacer(
                                        modifier = Modifier.width(8.dp)
                                    )

                                    Box(
                                        modifier = Modifier.weight(1f)
                                    ) {

                                        OCRResultCard(
                                            title = "Confidence",
                                            value = "${info.confidence}%"
                                        )
                                    }
                                }
                            }

                        }
                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )
                        if (detectedType == "📧 Email Address") {

                            Button(
                                modifier = Modifier.fillMaxWidth(),

                                onClick = {

                                    val intent = Intent(
                                        Intent.ACTION_SENDTO
                                    ).apply {

                                        data = Uri.parse(
                                            "mailto:${scannedText.trim()}"
                                        )
                                    }

                                    context.startActivity(intent)
                                }
                            ) {

//                            Text("📧 Send Email")
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Rounded.Email,
                                        contentDescription = null
                                    )

                                    Spacer(
                                        modifier = Modifier.width(8.dp)
                                    )

                                    Text("Send Email")
                                }
                            }

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )
                        }
                        if (detectedType == "📞 Phone Number") {

                            Button(
                                modifier = Modifier.fillMaxWidth(),

                                onClick = {

                                    val intent = Intent(
                                        Intent.ACTION_DIAL
                                    ).apply {

                                        data = Uri.parse(
                                            "tel:${scannedText.trim()}"
                                        )
                                    }

                                    context.startActivity(intent)
                                }
                            ) {

//                            Text("📞 Call Number")
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Rounded.Phone,
                                        contentDescription = null
                                    )

                                    Spacer(
                                        modifier = Modifier.width(8.dp)
                                    )

                                    Text("Call Number")
                                }
                            }

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )
                        }
                        if (detectedType == "🌐 Website / URL") {

                            Button(
                                modifier = Modifier.fillMaxWidth(),

                                onClick = {

                                    try {

                                        var url = scannedText.trim()

                                        if (
                                            !url.startsWith("http://") &&
                                            !url.startsWith("https://")
                                        ) {

                                            url = "https://$url"
                                        }

                                        val intent = Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse(url)
                                        )

                                        context.startActivity(intent)

                                    } catch (e: Exception) {

                                    }
                                }
                            ) {

//                            Text("🌐 Open Website")
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Rounded.Language,
                                        contentDescription = null
                                    )

                                    Spacer(
                                        modifier = Modifier.width(8.dp)
                                    )

                                    Text("Open Website")
                                }
                            }

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )
                        }

                        if (detectedType == "📍 Address") {

                            Button(
                                modifier = Modifier.fillMaxWidth(),

                                onClick = {

                                    val address =
                                        Uri.encode(scannedText)

                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(
                                            "geo:0,0?q=$address"
                                        )
                                    )

                                    context.startActivity(intent)
                                }
                            ) {

//                            Text("📍 Open in Maps")
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Rounded.LocationOn,
                                        contentDescription = null
                                    )

                                    Spacer(
                                        modifier = Modifier.width(8.dp)
                                    )

                                    Text("Open in Maps")
                                }
                            }

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )
                            Button(
                                modifier = Modifier.fillMaxWidth(),

                                onClick = {

                                    val address =
                                        Uri.encode(scannedText)

                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(
                                            "google.navigation:q=$address"
                                        )
                                    )

                                    context.startActivity(intent)
                                }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Rounded.Navigation,
                                        contentDescription = null
                                    )

                                    Spacer(
                                        modifier = Modifier.width(8.dp)
                                    )

                                    Text("Navigate")
                                }
//                            Text("🧭 Navigate")
                            }
                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )
                        }
                        if (detectedType == "📶 WiFi Credentials") {

                            val ssid =
                                extractSSID(scannedText)

                            val password =
                                extractPassword(scannedText)

                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {

                                    val intent = Intent(
                                        android.provider.Settings.ACTION_WIFI_SETTINGS
                                    )

                                    context.startActivity(intent)
                                }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Rounded.Wifi,
                                        contentDescription = null
                                    )

                                    Spacer(
                                        modifier = Modifier.width(8.dp)
                                    )

                                    Text("Open WiFi Settings")
                                }
//                            Text("📶 Open WiFi Settings")
                            }

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {

                                    clipboardManager.setText(
                                        AnnotatedString(password)
                                    )
                                }
                            ) {

//                            Text("📋 Copy Password")
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Rounded.Key,
                                        contentDescription = null
                                    )

                                    Spacer(
                                        modifier = Modifier.width(8.dp)
                                    )

                                    Text("Copy Password")
                                }
                            }

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )


                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Column(
                                    modifier = Modifier.padding(12.dp)
                                ) {

//                                Text(
//                                    text = "📶 WiFi Credentials Detected",
//                                    style = MaterialTheme.typography.titleMedium
//                                )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Rounded.Wifi,
                                            contentDescription = null
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Text(
                                            text = "WiFi Credentials Detected",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }

                                    Spacer(
                                        modifier = Modifier.height(8.dp)
                                    )

                                    Text(
                                        text = "SSID: $ssid"
                                    )

                                    Text(
                                        text = "Password: $password"
                                    )
                                }
                            }
                        }
                        Button(
                            onClick = {
                                clipboardManager.setText(
                                    AnnotatedString(scannedText)
                                )
                            }
                        ) {
//                        Text("📋 Copy Text")
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Rounded.ContentCopy,
                                    contentDescription = null
                                )

                                Spacer(
                                    modifier = Modifier.width(8.dp)
                                )

                                Text("Copy Text")
                            }
                        }
                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )
                        if (scannedText.isNotBlank()) {

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )

                            Button(
                                modifier = Modifier.fillMaxWidth(),

                                onClick = {


                                    val shareText =
                                        """
FloatIQ OCR Scan

Type: $detectedType

Content:
$scannedText
""".trimIndent()

                                    val shareIntent =
                                        Intent(Intent.ACTION_SEND).apply {

                                            type = "text/plain"

                                            putExtra(
                                                Intent.EXTRA_TEXT,
                                                shareText
                                            )

                                            putExtra(
                                                Intent.EXTRA_SUBJECT,
                                                "FloatIQ OCR Scan"
                                            )
                                        }

                                    context.startActivity(
                                        Intent.createChooser(
                                            shareIntent,
                                            "Share via"
                                        )
                                    )
                                }
                            ) {

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Rounded.Share,
                                        contentDescription = null
                                    )

                                    Spacer(
                                        modifier = Modifier.width(8.dp)
                                    )

                                    Text("Share")
                                }
                            }
                        }
                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )
                        Button(
                            onClick = {

                                if (scannedText.isNotBlank()) {

                                    val speakText =
                                        OCRProcessor.speechText(
                                            scannedText
                                        )

                                    textToSpeech?.speak(
                                        speakText,
                                        TextToSpeech.QUEUE_FLUSH,
                                        null,
                                        null
                                    )

                                    isSpeaking = true
                                }
                            }
                        ) {

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Icon(
                                    Icons.Rounded.VolumeUp,
                                    contentDescription = null
                                )

                                Spacer(
                                    modifier = Modifier.width(8.dp)
                                )

                                Text(
                                    if (isSpeaking)
                                        "Speaking..."
                                    else
                                        "Speak"
                                )
                            }
                        }
                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Button(
                            onClick = {
                                textToSpeech?.stop()
                                isSpeaking = false
                                problemInfo = null
                                flashEnabled = false
                                camera?.cameraControl?.enableTorch(false)
                                liveText = ""

                                scannedText = ""
                                interpretedExpression = ""
                                canSolve = false
                                calculationResult = ""

                                solutionSteps =
                                    emptyList()
                                detectedType =
                                    "Waiting for scan..."


                            }
                        ) {

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Icon(
                                    Icons.Rounded.DeleteSweep,
                                    contentDescription = null
                                )

                                Spacer(
                                    modifier = Modifier.width(8.dp)
                                )

                                Text("Clear")
                            }
                        }
                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )



                        if (canSolve) {
                            Button(
                                onClick = {

                                    try {

                                        val expression =
                                            OCRProcessor.normalizeForSolver(
                                                interpretedExpression.ifBlank {
                                                    scannedText
                                                }
                                            )
                                        android.util.Log.d(
                                            "OCR_DEBUG",
                                            expression
                                        )
                                        val solution =
                                            MathSolutionEngine.solve(
                                                expression
                                            )

                                        calculationResult =
                                            solution.result

                                        solutionSteps =
                                            solution.steps
                                    } catch (e: Exception) {


                                        calculationResult =
                                            "Invalid expression"

                                        solutionSteps =
                                            emptyList()
                                    }
                                }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Icon(
                                        Icons.Rounded.Calculate,
                                        contentDescription = null
                                    )

                                    Spacer(
                                        modifier = Modifier.width(8.dp)
                                    )

                                    Text("Calculate")
                                }
                            }
                        }
                        if (calculationResult.isNotEmpty()) {

                            Spacer(
                                modifier = Modifier.height(12.dp)
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Rounded.FormatListNumbered,
                                    contentDescription = null
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = "Solution Steps",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White
                                )
                            }
                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )

                            solutionSteps.forEachIndexed { index, step ->

                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor =
                                            MaterialTheme.colorScheme.surface
                                    )
                                ) {

                                    Column(
                                        modifier = Modifier.padding(12.dp)

                                    ) {

                                        Text(
                                            text = "Step ${index + 1}"
                                        )

                                        Spacer(
                                            modifier = Modifier.height(4.dp)
                                        )

                                        Text(step)
                                    }
                                }

                                Spacer(
                                    modifier = Modifier.height(8.dp)
                                )
                            }
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor =
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                )
                            ) {

                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Rounded.CheckCircle,
                                            contentDescription = null
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Text(
                                            text = "Final Result",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.White
                                        )
                                    }

                                    Spacer(
                                        modifier = Modifier.height(6.dp)
                                    )

                                    Text(
                                        text = calculationResult,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(
                    modifier = Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing)
                )
            }
          }
        }

    ) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.DocumentScanner,
                        contentDescription = "OCR Scanner",
                        tint = Color(0xFF0EA5E9),
                        modifier = Modifier.size(40.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(12.dp)
                    )

                    Text(
                        text = "OCR Scanner",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
                            RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            showOCRGuide = true
                        },

                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = "OCR Guide",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "Scan text, notes, links and equations instantly",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(cameraHeight)
                ) {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(cameraHeight),
                        factory = {

                            val previewView =
                                PreviewView(it)

                            val cameraProviderFuture =
                                ProcessCameraProvider.getInstance(it)

                            cameraProviderFuture.addListener({

                                val cameraProvider =
                                    cameraProviderFuture.get()

                                val preview =
                                    Preview.Builder()
                                        .build()

                                preview.surfaceProvider =
                                    previewView.surfaceProvider

                                val imageAnalysis =
                                    ImageAnalysis.Builder()

                                        .setTargetResolution(
                                            Size(
                                                640,
                                                480
                                            )
                                        )
                                        .setBackpressureStrategy(
                                            ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
                                        )

                                        .build()


                                imageAnalysis.setAnalyzer(

                                    cameraExecutor,
                                    OCRAnalyzer { detectedText ->

                                        val now =
                                            System.currentTimeMillis()

                                        if (
                                            now - lastOCRUpdateTime < 300
                                        ) {
                                            return@OCRAnalyzer
                                        }

                                        lastOCRUpdateTime = now

                                        if (
                                            detectedText.isNotBlank() &&
                                            detectedText != previousLiveText
                                        ) {

                                            previousLiveText =
                                                detectedText

                                            liveText =
                                                detectedText
                                        }
                                    }
                                )

                                val cameraSelector =
                                    CameraSelector.DEFAULT_BACK_CAMERA

                                cameraProvider.unbindAll()


                                camera =
                                    cameraProvider.bindToLifecycle(

                                        lifecycleOwner,

                                        cameraSelector,

                                        preview,

                                        imageAnalysis
                                    )

                            }, ContextCompat.getMainExecutor(it))

                            previewView
                        }
                    )

                    EquationScanOverlay()
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .size(48.dp)
                            .background(
                                Color.Black.copy(alpha = 0.6f),
                                RoundedCornerShape(12.dp)
                            )
                            .clickable {

                                flashEnabled = !flashEnabled

                                camera?.cameraControl
                                    ?.enableTorch(flashEnabled)
                            },

                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector =
                                if (flashEnabled)
                                    Icons.Rounded.FlashOn
                                else
                                    Icons.Rounded.FlashOff,

                            contentDescription = "Flashlight",

                            tint =
                                if (flashEnabled)
                                    Color.Yellow
                                else
                                    Color.White
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

        }


    }
    }
    if (showOCRGuide) {

        FeatureGuideDialog(

            title = "📸 OCR Scanner Guide",

            guideText =
                """
What is this tool?

OCR Scanner uses your camera to detect and extract text in real time.

Supported Detection

📄 General Text
Notes, books, documents and printed text.

🧮 Math Expressions
Arithmetic, algebra, trigonometry and formulas.

📞 Phone Numbers
Quick dial detected numbers.

📧 Email Addresses
Open email app instantly.

🌐 Websites
Launch detected URLs directly.

📍 Addresses
Open locations in Maps.

📶 WiFi Credentials
Detect SSID and passwords.

Supported Math Symbols

Use these formats when scanning equations:

Basic Operators

+   Addition
-   Subtraction
*   Multiplication
/   Division
^   Power

Examples

2+3
10-5
4*6
20/4
2^3

Constants

pi  → π
e   → Euler's Number

Examples

2*pi
pi*r^2
e^x

Powers

x^2
x^3

OCR also supports:

x²
x³

Square Root

sqrt(25)

Example

sqrt(x+9)

Trigonometry

sin(x)
cos(x)
tan(x)

Examples

sin(30)
cos(60)
tan(45)

Logarithms

log(100)
ln(10)

Examples

log(x)
ln(x)

Algebra

2x + 5 = 15
x^2 - 9
x^2 + 5x + 6 = 0

Parentheses

(2+3)*4

Recommended

Use brackets for complex expressions.

AI Features

🧠 Problem Classification

For math problems the scanner identifies:

• Category
• Type
• Method
• Confidence

Step-by-Step Solver

Supported:

• Arithmetic
• Algebra
• Factorization
• Simultaneous Equations
• Quadratics
• Difference of Squares

Available Actions

📋 Copy Text

🔊 Speak Text

🧮 Calculate Expression

📞 Call Number

📧 Send Email

🌐 Open Website

📍 Open Maps

📶 Copy WiFi Password

How to Use

1. Point camera at text.
2. Wait for live detection.
3. Tap Capture Text.
4. Review detected content.
5. Use actions or calculate if math is detected.

Math Tips

• Use pi instead of the π symbol when possible.
• Use sqrt() for square roots.
• Use ^ for powers.
• Keep equations clear and centered in the scan frame.
• Avoid handwritten text for best accuracy.
• Good lighting improves OCR accuracy.

Tips

• Use good lighting.
• Keep text inside the scan frame.
• Hold device steady.
• Large clear text gives best results.

Development Notice

🚧 FloatIQ OCR is still under active development.

While most printed text, equations and basic word problems are supported, some complex questions, handwriting and low-quality scans may not be recognized correctly.

New improvements and enhancements will be added in future updates.
"""
                    .trimIndent(),

            onDismiss = {
                showOCRGuide = false
            }
        )
    }
}
data class ScanHint(
    val icon: ImageVector,
    val text: String
)
@Composable
fun EquationScanOverlay() {

    val scanHints = listOf(

        ScanHint(
            Icons.Default.Calculate,
            "Scan Equation"
        ),

        ScanHint(
            Icons.Default.Calculate,
            "Scan Math Problem"
        ),

        ScanHint(
            Icons.Default.Phone,
            "Scan Phone Number"
        ),

        ScanHint(
            Icons.Default.Email,
            "Scan Email"
        ),

        ScanHint(
            Icons.Default.Language,
            "Scan Website"
        ),

        ScanHint(
            Icons.Default.LocationOn,
            "Scan Address"
        ),

        ScanHint(
            Icons.Default.Description,
            "Scan Text"
        ),

        ScanHint(
            Icons.Default.Wifi,
            "Scan WiFi"
        )
    )

    var currentHintIndex by remember {
        mutableStateOf(0)
    }

    LaunchedEffect(Unit) {

        while (true) {

            delay(3000)

            currentHintIndex =
                (currentHintIndex + 1) %
                        scanHints.size
        }
    }
    val frameColor =
        when (currentHintIndex) {

            0 -> Color.Cyan
            1 -> Color(0xFF00E676)
            2 -> Color(0xFF42A5F5)
            3 -> Color(0xFFFF9800)
            4 -> Color(0xFFFFC107)
            5 -> Color(0xFFFF7043)
            6 -> Color(0xFFAB47BC)
            else -> Color.Cyan
        }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Canvas(
                modifier = Modifier
                    .width(280.dp)
                    .height(140.dp)
            ) {

                val stroke = 8f
                val corner = 50f

                // Top Left
                drawLine(
                    frameColor,
                    start = Offset(0f, 0f),
                    end = Offset(corner, 0f),
                    strokeWidth = stroke
                )

                drawLine(
                    frameColor,
                    start = Offset(0f, 0f),
                    end = Offset(0f, corner),
                    strokeWidth = stroke
                )

                // Top Right
                drawLine(
                    frameColor,
                    start = Offset(size.width, 0f),
                    end = Offset(size.width - corner, 0f),
                    strokeWidth = stroke
                )

                drawLine(
                    frameColor,
                    start = Offset(size.width, 0f),
                    end = Offset(size.width, corner),
                    strokeWidth = stroke
                )

                // Bottom Left
                drawLine(
                    frameColor,
                    start = Offset(0f, size.height),
                    end = Offset(corner, size.height),
                    strokeWidth = stroke
                )

                drawLine(
                    frameColor,
                    start = Offset(0f, size.height),
                    end = Offset(0f, size.height - corner),
                    strokeWidth = stroke
                )

                // Bottom Right
                drawLine(
                    frameColor,
                    start = Offset(size.width, size.height),
                    end = Offset(size.width - corner, size.height),
                    strokeWidth = stroke
                )

                drawLine(
                    frameColor,
                    start = Offset(size.width, size.height),
                    end = Offset(size.width, size.height - corner),
                    strokeWidth = stroke
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            AnimatedContent(

                targetState =
                    scanHints[currentHintIndex],

                transitionSpec = {

                    slideInVertically {
                        it
                    } + fadeIn() togetherWith

                            slideOutVertically {
                                -it
                            } + fadeOut()
                },

                label = "OCRHint"
            ) { hint ->

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = hint.icon,
                        contentDescription = null,
                        tint = frameColor,
                        modifier =
                            Modifier.size(22.dp)
                    )

                    Spacer(
                        modifier =
                            Modifier.width(8.dp)
                    )

                    Text(
                        text = hint.text,
                        color = Color.White,
                        style =
                            MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}
private val PHONE_REGEX =
    Regex("^[+]?[0-9]{10,15}$")

private val URL_REGEX =
    Regex(
        "^(https?://)?(www\\.)?[a-zA-Z0-9-]+\\.[a-zA-Z]{2,}.*$"
    )
private val EMAIL_REGEX =
    Regex(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    )
private val MATH_REGEX =
    Regex(
        "[a-zA-Z0-9+\\-*/(). πe²³^]+"
    )
private fun isWifiCredentials(text: String): Boolean {

    val value = text.lowercase()

    return (
            value.contains("ssid") &&
                    value.contains("password")
            )
}
private fun extractSSID(text: String): String {

    val regex =
        Regex("ssid\\s*:?\\s*(.+)", RegexOption.IGNORE_CASE)

    return regex.find(text)
        ?.groupValues
        ?.getOrNull(1)
        ?.trim()
        ?.lines()
        ?.firstOrNull()
        ?: ""
}
private fun extractPassword(text: String): String {

    val regex =
        Regex("password\\s*:?\\s*(.+)", RegexOption.IGNORE_CASE)

    return regex.find(text)
        ?.groupValues
        ?.getOrNull(1)
        ?.trim()
        ?.lines()
        ?.firstOrNull()
        ?: ""
}
private val ADDRESS_REGEX =
    Regex(
        ".*\\b(street|st|road|rd|avenue|ave|lane|colony|nagar|city|district|apartment|flat|plot|village|mandal|state|pincode|main road|mg road|cross|layout|sector|block)\\b.*",
        RegexOption.IGNORE_CASE
    )

private fun normalizeEmail(text: String): String {

    return text
        .replace("\\s*@\\s*".toRegex(), "@")
        .replace("\\s*\\.\\s*".toRegex(), ".")
        .replace("\\s+".toRegex(), "")
        .trim()
}