package com.harish.floatiq.ui.health

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
import com.harish.floatiq.ui.components.FeatureGuideDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material3.Icon
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.platform.LocalContext
import com.harish.floatiq.storage.EngagementTracker

@Composable
fun SleepCalculatorScreen(
    onBack: () -> Unit
) {

    BackHandler {
        onBack()
    }
    val context = LocalContext.current

    var wakeHour by remember {
        mutableStateOf("")
    }

    var wakeMinute by remember {
        mutableStateOf("")
    }

    var result by remember {
        mutableStateOf<List<String>>(emptyList())
    }
    var sleepScore by remember {
        mutableStateOf("")
    }
    val keyboardController =
        LocalSoftwareKeyboardController.current
    var sleepDebt by remember {
        mutableStateOf("")
    }
    var calculatorMode by remember {
        mutableStateOf("Sleep")
    }
    var caffeineCutoff by remember {
        mutableStateOf("")
    }
    var showSleepGuide by remember {
        mutableStateOf(false)
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showInputError by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier.fillMaxSize()
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
//                .windowInsetsPadding(
//                    WindowInsets.safeDrawing.only(
//                        WindowInsetsSides.Top +
//                                WindowInsetsSides.Horizontal
//                    )
//                )
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .verticalScroll(
                    rememberScrollState()
                )


                .padding(24.dp)
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
                        imageVector = Icons.Default.Bedtime,
                        contentDescription = "Sleep Calculator",
                        tint = Color(0xFF6366F1),
                        modifier = Modifier.size(40.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(12.dp)
                    )

                    Text(
                        text = "Sleep Calculator",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
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
                            showSleepGuide = true
                        },

                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = "Sleep Guide",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "Enter when you want to wake up and we'll suggest ideal bedtimes based on sleep cycles.",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = wakeHour,
                colors = OutlinedTextFieldDefaults.colors(

                    focusedBorderColor =
                        MaterialTheme.colorScheme.primary,

                    unfocusedBorderColor =
                        MaterialTheme.colorScheme.surface,

                    focusedLabelColor =
                        MaterialTheme.colorScheme.primary,

                    unfocusedLabelColor =
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),

                    focusedTextColor =
                        MaterialTheme.colorScheme.onSurface,

                    unfocusedTextColor =
                        MaterialTheme.colorScheme.onSurface
                ),
                onValueChange = {
                    wakeHour = it
                },
                label = {
                    Text("Wake-Up Hour (0-23)")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = wakeMinute,
                onValueChange = {
                    wakeMinute = it
                },
                colors = OutlinedTextFieldDefaults.colors(

                    focusedBorderColor =
                        MaterialTheme.colorScheme.primary,

                    unfocusedBorderColor =
                        MaterialTheme.colorScheme.surface,

                    focusedLabelColor =
                        MaterialTheme.colorScheme.primary,

                    unfocusedLabelColor =
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),

                    focusedTextColor =
                        MaterialTheme.colorScheme.onSurface,

                    unfocusedTextColor =
                        MaterialTheme.colorScheme.onSurface
                ),
                label = {
                    Text("Wake-Up Minute")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        MaterialTheme.colorScheme.primary
                ),
                onClick = {
                    if (
                        wakeHour.isBlank() ||
                        wakeMinute.isBlank()
                    ) {

                        result = emptyList()
                        sleepScore = ""
                        sleepDebt = ""
                        caffeineCutoff = ""

                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "Wake hour and wake minute are required"
                            )
                        }

                        return@Button
                    }
                    try {

                        if (
                            wakeHour.toInt() !in 0..23 ||
                            wakeMinute.toInt() !in 0..59
                        ) {

                            result = emptyList()
                            sleepScore = ""
                            sleepDebt = ""
                            caffeineCutoff = ""

                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "Enter a valid time"
                                )
                            }

                            return@Button
                        }
                        val wakeTime =
                            LocalTime.of(
                                wakeHour.toInt(),
                                wakeMinute.toInt()
                            )

                        val formatter =
                            DateTimeFormatter.ofPattern(
                                "hh:mm a"
                            )


                        result = listOf(
                            "8 Cycles → ${
                                wakeTime
                                    .minusMinutes((90L * 8) + 15)
                                    .format(formatter)
                            }",

                            "7 Cycles → ${
                                wakeTime
                                    .minusMinutes((90L * 7) + 15)
                                    .format(formatter)
                            }",


                            "⭐ BEST: 6 Cycles → ${
                                wakeTime
                                    .minusMinutes((90L * 6) + 15)
                                    .format(formatter)
                            }",

                            "5 Cycles → ${
                                wakeTime
                                    .minusMinutes((90L * 5) + 15)
                                    .format(formatter)
                            }"
                        )
                        sleepScore = "Excellent Sleep Schedule"
                        sleepDebt = "0 Hours Sleep Debt"
                        caffeineCutoff =
                            wakeTime
                                .minusHours(10)
                                .format(formatter)
                        keyboardController?.hide()
                        EngagementTracker
                            .onSuccessfulCalculation(
                                context
                            )
                    } catch (e: Exception) {

                        result = emptyList()
                        sleepScore = ""
                        sleepDebt = ""
                        caffeineCutoff = ""

                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "Invalid input"
                            )
                        }
                        keyboardController?.hide()
                    }
                }
            ) {

                Text("Find Best Bedtimes")
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (result.isNotEmpty()) {

                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor =
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.35f)
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Text(
                            text = "🌙 Best Bedtimes For Your Wake-Up Time",
                            color =
                                MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )


                        SleepInfoCard(
                            title = "Sleep Score",
                            value = sleepScore,
                            icon = Icons.Default.Star,
                            iconColor = Color(0xFFFFD54F)
                        )

                        SleepInfoCard(
                            title = "Sleep Debt",
                            value = sleepDebt,
                            icon = Icons.Default.Bedtime,
                            iconColor = Color(0xFF38BDF8)
                        )

                        SleepInfoCard(
                            title = "Last Coffee",
                            value = caffeineCutoff,
                            icon = Icons.Default.Coffee,
                            iconColor = Color(0xFFFF9800)
                        )
                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )
//                        result.forEach {
//
//                            Text(
//                                it,
//
//                                color =
//                                    MaterialTheme.colorScheme.onSurface
//                            )
//
//                            Spacer(
//                                modifier = Modifier.height(8.dp)
//                            )
//                        }
                        BedtimeCard(
                            title = "8 Sleep Cycles",
                            time = result[0].substringAfter("→ ").trim()
                        )

                        BedtimeCard(
                            title = "7 Sleep Cycles",
                            time = result[1].substringAfter("→ ").trim()
                        )

                        BedtimeCard(
                            title = "Recommended (6 Cycles)",
                            time = result[2].substringAfter("→ ").trim(),
                            isRecommended = true
                        )

                        BedtimeCard(
                            title = "5 Sleep Cycles",
                            time = result[3].substringAfter("→ ").trim()
                        )
                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        Text(
                            text = "💡 Includes an estimated 15 minutes to fall asleep.",

                            color =
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
    if (showSleepGuide) {

        FeatureGuideDialog(

            title = "😴 Sleep Calculator Guide",

            guideText =
                """
What is this tool?

This calculator estimates the best bedtime based on complete sleep cycles.

How Sleep Cycles Work

A typical sleep cycle lasts about 90 minutes.

Waking up at the end of a cycle usually feels more refreshing than waking up in the middle of one.

Required Inputs

• Wake-up hour (0-23)
• Wake-up minute (0-59)

Results Explained

🌙 Bedtime Suggestions

Recommended times to fall asleep based on:

• 5 cycles
• 6 cycles ⭐ Recommended
• 7 cycles
• 8 cycles

⭐ Sleep Score

Indicates the quality of the suggested schedule.

☕ Last Coffee Time

Suggested caffeine cutoff time to help improve sleep quality.

😴 Sleep Debt

Displays estimated sleep debt information.

How to Use

1. Enter the time you want to wake up.
2. Tap Find Best Bedtimes.
3. Review the recommended sleep schedule.
4. Choose a bedtime that matches your routine.

Sleep Tips

• Avoid caffeine late in the day.
• Keep a consistent sleep schedule.
• Reduce screen exposure before bed.
• Aim for 7–9 hours of sleep.

Important

Results are estimates based on average sleep cycle durations.

Individual sleep needs may vary.
"""
                    .trimIndent(),

            onDismiss = {
                showSleepGuide = false
            }
        )
    }
}
@Composable
fun SleepInfoCard(
    title: String,
    value: String,
    icon: ImageVector,
    iconColor: Color
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),

        shape = RoundedCornerShape(18.dp),

        colors = CardDefaults.cardColors(

            containerColor =
                MaterialTheme.colorScheme.surface.copy(alpha = 0.35f)
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(28.dp)
            )

            Spacer(
                modifier = Modifier.width(16.dp)
            )

            Column {

                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = value,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
@Composable
fun BedtimeCard(
    title: String,
    time: String,
    isRecommended: Boolean = false
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),

        shape = RoundedCornerShape(18.dp),

        colors = CardDefaults.cardColors(
            containerColor =
                if (isRecommended)
                    Color(0xFF4F46E5).copy(alpha = 0.25f)
                else
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            horizontalArrangement =
                Arrangement.SpaceBetween,

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Column {

                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = time,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (isRecommended) {

                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFD54F),
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}