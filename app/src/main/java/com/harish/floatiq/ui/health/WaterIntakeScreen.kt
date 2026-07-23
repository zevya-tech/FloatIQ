//package com.harish.floatiq.ui.health

package com.harish.floatiq.ui.health

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.material3.MaterialTheme
import kotlinx.coroutines.launch
import com.harish.floatiq.ui.components.FeatureGuideDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.platform.LocalContext
import com.harish.floatiq.storage.EngagementTracker

@Composable
fun WaterIntakeScreen(
    onBack: () -> Unit
) {

    BackHandler {
        onBack()
    }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val scope = rememberCoroutineScope()
    var weight by remember {
        mutableStateOf("")
    }
    var showWaterGuide by remember {
        mutableStateOf(false)
    }
    var resultLiters by remember {
        mutableStateOf("")
    }

    var glasses by remember {
        mutableStateOf("")
    }
    Box(
        modifier = Modifier.fillMaxSize()
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
//                .windowInsetsPadding(
//                    WindowInsets.safeDrawing.only(
//                        WindowInsetsSides.Top +
//                                WindowInsetsSides.Horizontal
//                    )
//                )
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .verticalScroll(rememberScrollState())
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
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = "Water Intake",
                        tint = Color(0xFF0EA5E9),
                        modifier = Modifier.size(38.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(12.dp)
                    )

                    Text(
                        text = "Water Intake",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 30.sp,
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
                            showWaterGuide = true
                        },

                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = "Water Guide",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Daily hydration calculator",

                color =
                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = weight,
                onValueChange = {
                    weight = it
                },

                label = {
                    Text("Weight (kg)")
                },

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),


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

                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {

                    focusManager.clearFocus()
                    if (weight.isBlank()) {

                        scope.launch {

                            snackbarHostState.showSnackbar(
                                "Weight is required to calculate daily water intake"
                            )
                        }

                        resultLiters = ""
                        glasses = ""

                        return@Button
                    }
                    try {

                        val weightKg =
                            weight.toDouble()

                        val waterMl =
                            weightKg * 35

                        val liters =
                            waterMl / 1000

                        resultLiters =
                            String.format("%.1f", liters)

                        glasses =
                            (waterMl / 250).toInt().toString()

                        EngagementTracker
                            .onSuccessfulCalculation(
                                context
                            )
                    } catch (e: Exception) {
                        scope.launch {

                            snackbarHostState.showSnackbar(
                                "Please enter a valid weight"
                            )
                        }

                        resultLiters = ""
                        glasses = ""
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),

                shape = RoundedCornerShape(18.dp),

                colors = ButtonDefaults.buttonColors(
//                containerColor = Color(0xFF7C3AED)
                    containerColor =
                        MaterialTheme.colorScheme.primary
                )
            ) {

                Text(
                    text = "Calculate Water Intake",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            if (resultLiters.isNotEmpty()) {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor =
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {


                        Icon(
                            imageVector = Icons.Default.WaterDrop,
                            contentDescription = null,
                            tint = Color(0xFF0EA5E9),
                            modifier = Modifier.size(56.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "$resultLiters L",

                            color =
                                MaterialTheme.colorScheme.onSurface,
                            fontSize = 52.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "~ $glasses glasses/day",
//                        color = Color(0xFF94A3B8)
                            color =
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Hydration Tip",
//                        color = Color.White,
                            color =
                                MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Drink a glass of water after waking up and before meals.",
//                        color = Color(0xFF94A3B8)
                            color =
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
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
    if (showWaterGuide) {

        FeatureGuideDialog(

            title = "💧 Water Intake Guide",

            guideText =
                """
What is this tool?

This calculator estimates your recommended daily water intake based on body weight.

Required Input

• Weight in kilograms (kg)

How it Works

• Formula: Weight × 35 mL
• Result shown in liters
• Also estimates number of glasses per day

How to Use

1. Enter your weight.
2. Tap Calculate Water Intake.
3. Review liters and glasses per day.

Example

70 kg person

70 × 35 = 2450 mL

Recommended intake ≈ 2.5 L/day

Important

Water needs vary based on:

• Weather
• Physical activity
• Health conditions
• Age

Use this result as a general guideline.
"""
                    .trimIndent(),

            onDismiss = {
                showWaterGuide = false
            }
        )
    }
}


