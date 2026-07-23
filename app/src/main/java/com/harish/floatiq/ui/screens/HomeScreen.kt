package com.harish.floatiq.ui.screens

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.harish.floatiq.overlay.FloatingService

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.border

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
//import com.harish.floatiq.ui.ads.NativeAdCard
//import com.harish.floatiq.storage.LanguageStorage
import com.harish.floatiq.ui.components.FeatureGuideDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Settings
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import android.content.ComponentName
import android.content.pm.PackageManager
import androidx.compose.material3.Switch
import com.harish.floatiq.tiles.OverlayTileService
import android.app.Activity
import com.harish.floatiq.review.InAppReviewManager
import com.harish.floatiq.storage.EngagementTracker

@Composable
fun HomeScreen(
    onOpenCalculator: () -> Unit,
    onOpenHealthHub: () -> Unit,
    onOpenUnitConverter: () -> Unit,
    onOpenCurrencyConverter: () -> Unit,
    onOpenThemes: () -> Unit,
    onOpenFormulaExplorer: () -> Unit,
    onOpenOCR: () -> Unit,
//    onOpenLanguageSettings: () -> Unit
) {

    val context = LocalContext.current
//    val currentLanguage = remember {
//
//        LanguageStorage.getLanguage(context)
//    }
    val activity =
        context as Activity
    val prefs =
        context.getSharedPreferences(
            "floatiq_settings",
            android.content.Context.MODE_PRIVATE
        )
    var overlayEnabled by remember {
        mutableStateOf(Settings.canDrawOverlays(context))
    }

    DisposableEffect(Unit) {

        overlayEnabled = Settings.canDrawOverlays(context)

        onDispose { }
    }
    LaunchedEffect(Unit) {

        if (
            EngagementTracker.shouldAskForReview(
                context
            )
        ) {

            InAppReviewManager.requestReview(
                activity
            )
        }
    }
    val buttonColor =
        if (overlayEnabled)
            Color(0xFF00C853)
        else
            Color(0xFF1E293B)
    var showOverlaySettings by remember {
        mutableStateOf(false)
    }
    var showFeatureGuide by remember {
        mutableStateOf(false)
    }

    var defaultOverlay by remember {

        mutableStateOf(
            prefs.getString(
                "default_overlay",
                "Calculator"
            ) ?: "Calculator"
        )
    }
    var overlayTileEnabled by remember {

        mutableStateOf(
            prefs.getBoolean(
                "overlay_tile_enabled",
                false
            )
        )
    }


    Box(
        modifier = Modifier
            .fillMaxSize()

            .background(

                Brush.verticalGradient(
                    listOf(
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .shadow(
                        elevation = 50.dp,
                        shape = CircleShape,
                        ambientColor = MaterialTheme.colorScheme.primary,
                        spotColor = MaterialTheme.colorScheme.primary
                    )
                    .shadow(
                        elevation = 20.dp,
                        shape = CircleShape,
                        ambientColor = Color.White,
                        spotColor = Color.White
                    )
                    .border(
                        width = 4.dp,
                        color = Color.White,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.tertiary
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "FI",
                    color = Color.White,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "FloatIQ",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Floating Smart Utility Assistant",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(80.dp))
            Card(
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = buttonColor
                ),
                modifier = Modifier
                    .fillMaxWidth()
//                    .height(170.dp)
                    .height(140.dp)
                    .shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(32.dp),
                        ambientColor = buttonColor,
                        spotColor = buttonColor
                    )

                    .clickable {

                        val permissionGranted =
                            Settings.canDrawOverlays(context)

                        overlayEnabled = permissionGranted

                        Log.d(
                            "FLOATIQ_OVERLAY",
                            "Permission: $permissionGranted"
                        )

                        if (!permissionGranted) {

                            val intent = Intent(
                                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:${context.packageName}")
                            )

                            context.startActivity(intent)

                        } else {

                            Log.d(
                                "FLOATIQ_OVERLAY",
                                "Starting Floating Service"
                            )

                            val serviceIntent =
                                Intent(context, FloatingService::class.java)


                            androidx.core.content.ContextCompat.startForegroundService(
                                context,
                                serviceIntent
                            )
                        }
                    }


            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text =
                                    if (overlayEnabled)
                                        "Floating Bubble"
                                    else
                                        "Enable Floating Bubble",

                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 24.sp,
                                maxLines = 2
                            )

                            Spacer(
                                modifier = Modifier.height(10.dp)
                            )

                            Text(
                                text =
                                    if (overlayEnabled)
                                        "Overlay permission granted"
                                    else
                                        "Tap to allow floating bubble access",

                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 14.sp
                            )
                            if (overlayEnabled) {

                                Spacer(
                                    modifier = Modifier.height(8.dp)
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .background(
                                                Color.White,
                                                CircleShape
                                            )
                                    )

                                    Spacer(
                                        modifier = Modifier.width(6.dp)
                                    )

                                    Text(
                                        text = "Enabled",
                                        color = Color.White,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }


                        if (overlayEnabled) {

                            Spacer(
                                modifier = Modifier.width(12.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Color.White.copy(alpha = 0.15f)
                                    )
                                    .clickable {
                                        showOverlaySettings = true
                                    },

                                contentAlignment = Alignment.Center
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Overlay Settings",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }

                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            Card(
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF7C3AED)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clickable {

                        onOpenCalculator()
                    }
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),

                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Open Calculator",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
//                        text = "Launch smart calculator workspace",
                        text = "Arithmetic, Trigonometry & Logarithms",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            Card(
                shape = RoundedCornerShape(32.dp),

                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFEA580C)
                ),

                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clickable {

                        onOpenOCR()
                    }
            ) {

                Column(

                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),

                    verticalArrangement =
                        Arrangement.Center,

                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "OCR Scanner",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    Text(
                        text = "Smart OCR with math solving, URL opening, Email, Phone and Map address actions",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Card(
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF059669)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clickable {

                        onOpenHealthHub()
                    }
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),

                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Health Calculator",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "BMI, Calories, Water intake & more",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            Card(
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF0284C7)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clickable {

                        onOpenUnitConverter()
                    }
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),

                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Unit Converter",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Length, Weight, Temperature & more",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFDC2626)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clickable {

                        onOpenCurrencyConverter()
                    }
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),

                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Currency Converter",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    Text(
                        text = "USD, INR, EUR, GBP & more",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(
                modifier = Modifier.height(24.dp)
            )


//            Spacer(
//                modifier = Modifier.height(24.dp)
//            )
//
//            Card(
//                shape = RoundedCornerShape(32.dp),
//
//                colors = CardDefaults.cardColors(
//                    containerColor = Color(0xFF9333EA)
//                ),
//
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(140.dp)
//                    .clickable {
//
//                        onOpenLanguageSettings()
//                    }
//            ) {
//
//                Column(
//
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(24.dp),
//
//                    verticalArrangement =
//                        Arrangement.Center,
//
//                    horizontalAlignment =
//                        Alignment.CenterHorizontally
//                ) {
//
//                    Text(
//                        text = "Language",
//                        color = Color.White,
//                        fontSize = 22.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//
//                    Spacer(
//                        modifier = Modifier.height(10.dp)
//                    )
//
//                    Text(
//                        text = "Current: ${currentLanguage.displayName}",
//                        color = Color.White.copy(alpha = 0.8f),
//                        fontSize = 14.sp
//                    )
//                }
//            }
            Spacer(
                modifier = Modifier.height(24.dp)
            )
            Card(
                shape = RoundedCornerShape(32.dp),

                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF0EA5E9)
                ),

                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clickable {

                        onOpenFormulaExplorer()
                    }
            ) {

                Column(

                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),

                    verticalArrangement =
                        Arrangement.Center,

                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Formula Explorer",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    Text(
                        text = "Search formulas, Explanations, Variables and Examples",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(
                modifier = Modifier.height(24.dp)
            )
            Card(
                shape = RoundedCornerShape(32.dp),

                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF7C3AED)
                ),

                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clickable {

                        onOpenThemes()
                    }
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),

                    verticalArrangement =
                        Arrangement.Center,

                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Themes",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    Text(
                        text = "Customize FloatIQ Appearance",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(
                modifier = Modifier.height(24.dp)
            )
            Card(

                shape = RoundedCornerShape(32.dp),

                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFC107)
                ),

                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clickable {

//                        InAppReviewManager.requestReview(
//                            activity
//                        )
                            InAppReviewManager.openPlayStore(activity)
                    }
            ) {

                Column(

                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),

                    verticalArrangement =
                        Arrangement.Center,

                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Text(

                        text = "⭐ Rate FloatIQ",

                        color = Color.Black,

                        fontSize = 22.sp,

                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    Text(

                        text =
                            "Enjoying FloatIQ? Your rating helps improve the app and supports future updates.",

                        color = Color.Black.copy(alpha = 0.75f),

                        fontSize = 14.sp
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 32.dp,
                    end = 20.dp
                ),
            contentAlignment = Alignment.TopEnd
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        Color.White.copy(alpha = 0.15f)
                    )
                    .clickable {

                        showFeatureGuide = true
                    },

                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Help,
                    contentDescription = "Feature Guide",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
        if (showOverlaySettings) {

            AlertDialog(

                onDismissRequest = {
                    showOverlaySettings = false
                },

                title = {
                    Text(
                        "Overlay Settings"
                    )
                },

                text = {

                    Column {

                        Text(
                            "Default Opening Overlay"
                        )

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        listOf(
                            "Calculator",
                            "Unit Converter",
                            "Currency Converter"
                        ).forEach { option ->

                            Row(
                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {


                                RadioButton(
                                    selected =
                                        defaultOverlay == option,

                                    onClick = {

                                        defaultOverlay = option

                                        prefs.edit()
                                            .putString(
                                                "default_overlay",
                                                option
                                            )
                                            .apply()
                                    }
                                )


                                Text(
                                    text = option,

                                    modifier = Modifier.clickable {

                                        defaultOverlay = option

                                        prefs.edit()
                                            .putString(
                                                "default_overlay",
                                                option
                                            )
                                            .apply()
                                    }
                                )
                            }
                        }
                        Spacer(
                            modifier = Modifier.height(24.dp)
                        )

                        Text(
                            text = "Quick Settings"
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement =
                                Arrangement.SpaceBetween,
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Text(
                                "FloatIQ Overlay Tile"
                            )

                            Switch(
                                checked = overlayTileEnabled,
                                onCheckedChange = { enabled ->

                                    overlayTileEnabled = enabled

                                    prefs.edit()
                                        .putBoolean(
                                            "overlay_tile_enabled",
                                            enabled
                                        )
                                        .apply()

                                    val component =
                                        ComponentName(
                                            context,
                                            OverlayTileService::class.java
                                        )

                                    context.packageManager
                                        .setComponentEnabledSetting(
                                            component,
                                            if (enabled)
                                                PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                                            else
                                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                            PackageManager.DONT_KILL_APP
                                        )
                                }
                            )
                        }

                    }
                },

                confirmButton = {

                    Text(
                        text = "Close",
                        modifier = Modifier.clickable {

                            showOverlaySettings = false
                        }
                    )
                }
            )
        }
        if (showFeatureGuide) {


            FeatureGuideDialog(

                title = "📘 FloatIQ Feature Guide",

                guideText =
                    """
FloatIQ is a privacy-focused utility toolkit that works both inside the app and through the floating assistant overlay.

────────────────────
🫧 Floating Bubble

Use FloatIQ anywhere on your device with the floating bubble overlay.

Features

• Works over other apps
• Draggable floating bubble
• Quick access to tools
• Customizable default overlay

Touch Controls

👆 Single Tap

Opens your default overlay instantly.

Examples:

• Calculator
• Unit Converter
• Currency Converter (internet connection required)

You can choose the default overlay from:

Floating Bubble → Settings

✋ Press and Hold

Opens the Tool Menu.

From the Tool Menu you can launch:

• Calculator
• Unit Converter
• Currency Converter (internet connection required)
• OCR Scanner

How to use:

1. Grant overlay permission.
2. Tap Floating Bubble to start.
3. Drag the bubble anywhere.
4. Single tap for your default tool.
5. Press and hold and release to open the full tool menu.
6. Change the default tool from Floating Bubble Card by clicking Settings icon button.

────────────────────

⚡ Quick Settings Tile

FloatIQ includes an optional Quick Settings Tile for faster access to the floating bubble.

What it does

• Launch FloatIQ Overlay directly from Quick Settings
• No need to open the main app
• One-tap access to the floating bubble
• Works from anywhere on your device

How to Enable

1. Open Floating Bubble Settings
2. Enable "FloatIQ Overlay Tile"
3. Open Android Quick Settings
4. Tap Edit (✏️)
5. Add the FloatIQ Tile
6. Tap the FloatIQ Tile to launch the FloatIQ floating Bbubble
7. Swipe up or return to your previous app and the FloatIQ overlay bubble will remain available for instant access

Requirements

• Overlay permission must be granted
• Tile must be enabled from Overlay Settings

Tip

The Quick Settings Tile is the fastest way to launch FloatIQ without opening the app.

────────────────────

🧮 Calculator

• Simple Calculations
• Scientific Calculator
• Trigonometry
• Logarithms


How to use:
1. Enter expression
2. Press =
3. View result

────────────────────

📸 OCR Scanner

Features

• Completely Offline
• No internet required
• Scan equations
• Scan text
• Detect phone numbers
• Detect emails
• Detect websites
• Detect addresses
• Detect WiFi credentials
• OCR Math Classification
• Step-by-step Math Solver

Privacy

• Processing happens on-device
• Images are not uploaded
• Works without cloud services

How to use:

1. Point camera at text.
2. Wait for live detection.
3. Tap Capture Text.
4. Review detected content.
5. Calculate if math is detected.

Supported Math

• Arithmetic
• Algebra
• Quadratics
• Factorization
• Difference of Squares
• Simultaneous Equations
• Trigonometry

Supported Actions

• Copy Text
• Speak Text
• Open Websites
• Send Emails
• Call Numbers
• Open Maps
• Navigate Using Maps
• Copy WiFi Passwords

────────────────────

📏 Unit Converter

• Length
• Weight
• Temprature
• Area
• Volume
• Speed
• Time
• Data Storage

How to use:
1. Select category
2. Enter value
3. Choose units
4. Click Convert

────────────────────

💱 Currency Converter

• Live exchange rates

How to use:
1. Select currencies
2. Enter amount
3. Click Convert

Important:

• Currency Converter uses live exchange rate data and requires an active internet connection.

────────────────────

❤️ Health Hub

• BMI Calculator
  Requires: Weight and Height

• Calorie Calculator
  Requires: Weight

• Water Intake Calculator
  Requires: Age, Weight and Height

• Age Calculator
  Requires: Date of Birth

• Sleep Calculator
  Requires: Wake-up Time

────────────────────

🧠 Formula Explorer

• Formula search
• Explanations
• Variables
• Examples

Categories:

•Mathematics
•Physics
•Chemistry
•Statistics
""".trimIndent(),

                onDismiss = {
                    showFeatureGuide = false
                }
            )
        }
    }
}