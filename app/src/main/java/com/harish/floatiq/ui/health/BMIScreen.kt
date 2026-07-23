
package com.harish.floatiq.ui.health

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.material3.MaterialTheme
import kotlinx.coroutines.launch
import com.harish.floatiq.ui.components.FeatureGuideDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.platform.LocalContext
import com.harish.floatiq.storage.EngagementTracker
@Composable
fun BMIScreen(
    onBack: () -> Unit
) {

    BackHandler {
        onBack()
    }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var weight by remember {
        mutableStateOf("")
    }

    var height by remember {
        mutableStateOf("")
    }
    var showBMIGuide by remember {
        mutableStateOf(false)
    }
    var bmiResult by remember {
        mutableStateOf("")
    }

    var bmiCategory by remember {
        mutableStateOf("")
    }
    var healthyRange by remember {
        mutableStateOf("")
    }

    var healthTip by remember {
        mutableStateOf("")
    }

    var categoryColor by remember {
        mutableStateOf(Color.White)
    }
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val scope = rememberCoroutineScope()
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
//            .windowInsetsPadding(
//                WindowInsets.safeDrawing.only(
//                    WindowInsetsSides.Top +
//                            WindowInsetsSides.Horizontal
//                )
//            )
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {

        Spacer(modifier = Modifier.height(24.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.MonitorWeight,
                    contentDescription = "BMI",
                    tint = Color(0xFF8B5CF6),
                    modifier = Modifier.size(42.dp)
                )

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Text(
                    text = "BMI Calculator",
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
                        showBMIGuide = true
                    },

                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.HelpOutline,
                    contentDescription = "BMI Guide",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Check your Body Mass Index",
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

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = height,
            onValueChange = {
                height = it
            },
            label = {
                Text("Height (cm)")
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
            colors = ButtonDefaults.buttonColors(
                containerColor =
                    MaterialTheme.colorScheme.primary
            ),

            shape = RoundedCornerShape(18.dp),

            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            onClick = {
                if (
                    weight.isBlank() ||
                    height.isBlank()
                ) {

                    scope.launch {

                        snackbarHostState.showSnackbar(
                            "Weight and Height are required to calculate BMI"
                        )
                    }

                    bmiResult = ""
                    bmiCategory = ""
                    healthyRange = ""
                    healthTip = ""

                    return@Button
                }

                try {
//                try {
                    focusManager.clearFocus()
                    val weightKg =
                        weight.toDouble()

                    val heightMeters =
                        height.toDouble() / 100

                    val bmi =
                        weightKg /
                                (heightMeters * heightMeters)

                    bmiResult =
                        String.format("%.1f", bmi)


                    when {

                        bmi < 18.5 -> {

                            bmiCategory = "Underweight"

                            categoryColor = Color(0xFF38BDF8)

                            healthTip =
                                "Increase nutritious calorie intake and strength training."
                        }

                        bmi < 25 -> {

                            bmiCategory = "Normal Weight"

                            categoryColor = Color(0xFF00C853)

                            healthTip =
                                "Maintain your current lifestyle and stay active."
                        }

                        bmi < 30 -> {

                            bmiCategory = "Overweight"

                            categoryColor = Color(0xFFFF9800)

                            healthTip =
                                "Focus on regular exercise and balanced eating."
                        }

                        else -> {

                            bmiCategory = "Obese"

                            categoryColor = Color(0xFFDC2626)

                            healthTip =
                                "Consider a structured weight management plan."
                        }
                    }

                    val minWeight =
                        18.5 * (heightMeters * heightMeters)

                    val maxWeight =
                        24.9 * (heightMeters * heightMeters)

                    healthyRange =
                        "${String.format("%.1f", minWeight)} kg - ${
                            String.format("%.1f", maxWeight)
                        } kg"
                    EngagementTracker
                        .onSuccessfulCalculation(
                            context
                        )
                } catch (e: Exception) {
                    scope.launch {

                        snackbarHostState.showSnackbar(
                            "Please enter valid numeric values"
                        )
                    }
                    bmiResult = "Invalid Input"
                    bmiCategory = ""
                    healthyRange = ""
                    healthTip = ""
                }
            },


        ) {


            Text(
                text = "Calculate BMI",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (bmiResult.isNotEmpty()) {

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                ),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 12.dp
                )

            ) {

                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "YOUR BMI",
                        color =
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        letterSpacing = 2.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = bmiResult,
                        color = categoryColor,
                        fontSize = 60.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = bmiCategory,
                        color = categoryColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = when (bmiCategory) {

                            "Underweight" -> "🥗"

                            "Normal Weight" -> "💪"

                            "Overweight" -> "🏃"

                            "Obese" -> "❤️"

                            else -> ""
                        },

                        fontSize = 42.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))


                    LinearProgressIndicator(

                        progress =
                            when {

                                bmiResult == "Invalid Input" -> 0f

                                bmiResult.toFloat() < 18.5f -> 0.25f

                                bmiResult.toFloat() < 25f -> 0.50f

                                bmiResult.toFloat() < 30f -> 0.75f

                                else -> 1f
                            },

                        color = categoryColor,


                        trackColor =
                            MaterialTheme.colorScheme.surface,

                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Healthy Weight Range",

                        color =
                            MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))


                    Text(
                        text = healthyRange,
                        color =
                            MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(16.dp))


                    Text(
                        text = "Health Tip",
                        color =
                            MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor =
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                        ),

                        shape = RoundedCornerShape(16.dp),

                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text(
                            text = healthTip,
                            color =
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),

                            modifier = Modifier.padding(16.dp)
                        )
                    }
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
    if (showBMIGuide) {

        FeatureGuideDialog(

            title = "⚖️ BMI Calculator Guide",

            guideText =
                """
What is BMI?

BMI (Body Mass Index) estimates whether your weight is healthy for your height.

Required Inputs

• Weight in kilograms (kg)
• Height in centimeters (cm)

BMI Categories

• Underweight: Below 18.5
• Normal Weight: 18.5 - 24.9
• Overweight: 25 - 29.9
• Obese: 30+

How to Use

1. Enter weight in kg.
2. Enter height in cm.
3. Tap Calculate BMI.
4. Review your BMI score and category.

Important

BMI is a screening tool only.

It does not directly measure body fat and should not replace professional medical advice.
"""
                    .trimIndent(),

            onDismiss = {
                showBMIGuide = false
            }
        )
    }
}
