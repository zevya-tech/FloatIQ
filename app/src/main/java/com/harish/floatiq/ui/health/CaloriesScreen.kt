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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.material3.MaterialTheme
import kotlinx.coroutines.launch
import com.harish.floatiq.ui.components.FeatureGuideDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Chair
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.platform.LocalContext
import com.harish.floatiq.storage.EngagementTracker

@Composable
fun CaloriesScreen(
    onBack: () -> Unit
) {

    BackHandler {
        onBack()
    }
    val context = LocalContext.current

    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }

    var gender by remember {
        mutableStateOf("Male")
    }
    var activityLevel by remember {
        mutableStateOf("Sedentary")
    }
    var bmr by remember {
        mutableStateOf("")
    }

    var maintenance by remember {
        mutableStateOf("")
    }

    var loss by remember {
        mutableStateOf("")
    }

    var gain by remember {
        mutableStateOf("")
    }
    var showCaloriesGuide by remember {
        mutableStateOf(false)
    }
    var selectedActivity by remember {
        mutableStateOf("")
    }
    val keyboardController =
        LocalSoftwareKeyboardController.current
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
            .verticalScroll(
                rememberScrollState()
            )
            .padding(24.dp)
    ) {

//        Text(
//            text = "🔥 Calories Calculator",
//
//            color = MaterialTheme.colorScheme.onBackground,
//            fontSize = 30.sp,
//            fontWeight = FontWeight.Bold
//        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = "Calories",
                    tint = Color(0xFFF97316),
                    modifier = Modifier.size(38.dp)
                )

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Text(
                    text = "Calories Calculator",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 24.sp,
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
                        showCaloriesGuide = true
                    },

                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.HelpOutline,
                    contentDescription = "Calories Guide",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }


        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = age,
            onValueChange = {
                age = it
            },
            label = {
                Text("Age")
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
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = weight,
            onValueChange = {
                weight = it
            },
            label = {
                Text("Weight (kg)")
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
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = height,
            onValueChange = {
                height = it
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
                Text("Height (cm)")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row {

            FilterChip(
                selected = gender == "Male",
                onClick = {
                    gender = "Male"
                },

                label = {
                    Text("Male")
                }
            )

            Spacer(modifier = Modifier.width(12.dp))

            FilterChip(
                selected = gender == "Female",
                onClick = {
                    gender = "Female"
                },
                label = {
                    Text("Female")
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Activity Level",
//            color = Color.White,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column {

            listOf(
                "Sedentary",
                "Light Exercise",
                "Moderate Exercise",
                "Heavy Exercise",
                "Athlete"
            ).forEach { level ->

                FilterChip(

                    selected = activityLevel == level,

                    onClick = {
                        activityLevel = level
                    },
                    colors = FilterChipDefaults.filterChipColors(

                        selectedContainerColor =
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),

                        selectedLabelColor =
                            MaterialTheme.colorScheme.onBackground
                    ),


                    label = {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Icon(
                                imageVector = when(level) {

                                    "Sedentary" ->
                                        Icons.Default.Chair

                                    "Light Exercise" ->
                                        Icons.Default.DirectionsWalk

                                    "Moderate Exercise" ->
                                        Icons.Default.DirectionsRun

                                    "Heavy Exercise" ->
                                        Icons.Default.FitnessCenter

                                    else ->
                                        Icons.Default.Whatshot
                                },

                                contentDescription = level,

                                tint = when(level) {

                                    "Sedentary" ->
                                        Color(0xFF64748B)

                                    "Light Exercise" ->
                                        Color(0xFF06B6D4)

                                    "Moderate Exercise" ->
                                        Color(0xFF10B981)

                                    "Heavy Exercise" ->
                                        Color(0xFFF59E0B)

                                    else ->
                                        Color(0xFFEF4444)
                                },

                                modifier = Modifier.size(18.dp)
                            )

                            Spacer(
                                modifier = Modifier.width(8.dp)
                            )

                            Text(level)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor =
                    MaterialTheme.colorScheme.primary
            ),
            onClick = {
                if (
                    age.isBlank() ||
                    weight.isBlank() ||
                    height.isBlank()
                ) {

                    scope.launch {

                        snackbarHostState.showSnackbar(
                            "Age, Weight and Height are required"
                        )
                    }
                    selectedActivity = ""
                    bmr = ""
                    maintenance = ""
                    loss = ""
                    gain = ""

                    return@Button
                }
                try {

                    val a = age.toDouble()
                    val w = weight.toDouble()
                    val h = height.toDouble()

                    val calculatedBmr =
                        if (gender == "Male") {

                            10 * w +
                                    6.25 * h -
                                    5 * a +
                                    5

                        } else {

                            10 * w +
                                    6.25 * h -
                                    5 * a -
                                    161
                        }


                    val multiplier =
                        when(activityLevel) {

                            "Sedentary" -> 1.2

                            "Light Exercise" -> 1.375

                            "Moderate Exercise" -> 1.55

                            "Heavy Exercise" -> 1.725

                            else -> 1.9
                        }

                    val maintenanceCalories =
                        calculatedBmr * multiplier

                    bmr =
                        calculatedBmr.toInt().toString()

                    maintenance =
                        maintenanceCalories.toInt().toString()

                    loss =
                        (maintenanceCalories - 500)
                            .toInt()
                            .toString()

                    gain =
                        (maintenanceCalories + 500)
                            .toInt()
                            .toString()
                    selectedActivity = activityLevel
                    keyboardController?.hide()
                    EngagementTracker
                        .onSuccessfulCalculation(
                            context
                        )
                } catch (_: Exception) {   scope.launch {

                    snackbarHostState.showSnackbar(
                        "Please enter valid numeric values"
                    )
                }
                    selectedActivity = ""
                    bmr = ""
                    maintenance = ""
                    loss = ""
                    gain = ""
                    keyboardController?.hide()
                }
            }
        ) {

            Text("Calculate Calories")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (bmr.isNotEmpty()) {

            Card(
                shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                        containerColor =
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                        )
            ) {

                Column(
                    modifier = Modifier.padding(20.dp),

                ) {
                    Text(
                        text = "🏃 Activity: $selectedActivity",
                        color =
                            MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )
                    Text("🔥 BMR: $bmr kcal",
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        "🍽 Maintenance: $maintenance kcal",
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        "⬇ Weight Loss: $loss kcal",
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        "⬆ Weight Gain: $gain kcal",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    Text(
                        text = "Recommendation",
                        color =
                            MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(
                        text =
                            when(selectedActivity) {

                                "Sedentary" ->
                                    "Try increasing daily movement and walking."

                                "Light Exercise" ->
                                    "Great start. Stay consistent."

                                "Moderate Exercise" ->
                                    "Good balance of activity and recovery."

                                "Heavy Exercise" ->
                                    "Focus on recovery, protein, and sleep."

                                else ->
                                    "High activity level. Ensure adequate nutrition."
                            },
                        color =
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
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
    if (showCaloriesGuide) {

        FeatureGuideDialog(

            title = "🔥 Calories Calculator Guide",

            guideText =
                """
What is this tool?

This calculator estimates your daily calorie needs using the BMR (Basal Metabolic Rate) formula and your activity level.

Required Inputs

• Age
• Weight (kg)
• Height (cm)
• Gender
• Activity level

Results Explained

🔥 BMR
Calories your body burns at rest.

🍽 Maintenance
Calories needed to maintain current weight.

⬇ Weight Loss
Recommended calories for gradual fat loss.

⬆ Weight Gain
Recommended calories for healthy weight gain.

Activity Levels

🪑 Sedentary
Little or no exercise.

🚶 Light Exercise
1–3 exercise days per week.

🏃 Moderate Exercise
3–5 exercise days per week.

💪 Heavy Exercise
6–7 exercise days per week.

🔥 Athlete
Very intense training.

How to Use

1. Enter age, weight and height.
2. Select gender.
3. Select activity level.
4. Tap Calculate Calories.
5. Review calorie recommendations.

Important

Results are estimates and may vary depending on body composition, metabolism, and health conditions.

Consult a healthcare professional for personalized nutrition advice.
"""
                    .trimIndent(),

            onDismiss = {
                showCaloriesGuide = false
            }
        )
    }
}