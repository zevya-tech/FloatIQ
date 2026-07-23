package com.harish.floatiq.ui.health
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import com.harish.floatiq.ui.components.FeatureGuideDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
@Composable
fun HealthHubScreen(

    onOpenBMI: () -> Unit,
    onOpenWaterIntake: () -> Unit,
    onOpenCalories: () -> Unit,
    onOpenAgeCalculator: () -> Unit,
    onOpenSleepCalculator: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler {
        onBack()
    }
    var showHealthGuide by remember {
        mutableStateOf(false)
    }

    val tools = listOf(

        "BMI Calculator",
        "Water Intake",
        "Calories",
        "Age Calculator",
        "Sleep Calculator"
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
//                .windowInsetsPadding(
//                    WindowInsets.safeDrawing.only(
//                        WindowInsetsSides.Top +
//                                WindowInsetsSides.Horizontal
//                    )
//                )
                .windowInsetsPadding(WindowInsets.safeDrawing)

                .padding(20.dp)
        ){

            Spacer(modifier = Modifier.height(20.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {

                Column {

                    Text(
                        text = "Health Hub",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "Smart health utilities",
                        color =
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        fontSize = 16.sp
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

                            showHealthGuide = true
                        },

                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = "Health Guide",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            Spacer(modifier = Modifier.height(28.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 20.dp),
                modifier = Modifier.weight(1f)

            ) {


                items(tools) { tool ->

                    HealthCard(
                        title = tool,


                        cardColor =
                            when (tool) {

                                "BMI Calculator" ->
                                    MaterialTheme.colorScheme.primary

                                "Water Intake" ->
                                    MaterialTheme.colorScheme.secondary

                                "Calories" ->
                                    MaterialTheme.colorScheme.tertiary

                                "Age Calculator" ->
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)

                                "Sleep Calculator" ->
                                    MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f)

                                else ->
                                    MaterialTheme.colorScheme.surface
                            },
                        onClick = {

                            when (tool) {

                                "BMI Calculator" -> {
                                    onOpenBMI()
                                }

                                "Water Intake" -> {
                                    onOpenWaterIntake()
                                }
                                "Calories" -> {
                                    onOpenCalories()
                                }
                                "Age Calculator" -> {
                                    onOpenAgeCalculator()
                                }
                                "Sleep Calculator" -> {
                                    onOpenSleepCalculator()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
    if (showHealthGuide) {

        FeatureGuideDialog(

            title = "❤️ Health Hub Guide",

            guideText =
                """
Available Tools

⚖️ BMI Calculator
Calculate Body Mass Index using height and weight.

💧 Water Intake
Estimate recommended daily water consumption.

🔥 Calories Calculator
Estimate daily calorie requirements.

🎂 Age Calculator
Calculate exact age from date of birth.

😴 Sleep Calculator
Plan bedtime and wake-up times.

How to Use

1. Select a health tool.
2. Enter the required information.
3. Tap Calculate.
4. Review the results.

Important

These tools provide estimates and educational information only.

They are not a substitute for professional medical advice.
""".trimIndent(),

            onDismiss = {

                showHealthGuide = false
            }
        )
    }
}

@Composable
fun HealthCard(
    title: String,
    cardColor: Color,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .border(
                width = 2.dp,
                color = cardColor,
                shape = RoundedCornerShape(28.dp)
            )
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(28.dp),


        colors = CardDefaults.cardColors(
//            containerColor = cardColor.copy(alpha = 0.25f)
            containerColor = cardColor.copy(alpha = 0.35f)
        ),
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),

            verticalArrangement = Arrangement.SpaceBetween
        ) {


//            Text(
//                text = icon,
//                fontSize = 42.sp
//            )
            Icon(

                imageVector = when(title) {

                    "BMI Calculator" ->
                        Icons.Default.MonitorWeight

                    "Water Intake" ->
                        Icons.Default.WaterDrop

                    "Calories" ->
                        Icons.Default.LocalFireDepartment

                    "Age Calculator" ->
                        Icons.Default.Cake

                    "Sleep Calculator" ->
                        Icons.Default.Bedtime

                    else ->
                        Icons.Default.MonitorWeight
                },

                contentDescription = title,

//                tint = cardColor,
                tint = when(title) {

                    "BMI Calculator" ->
                        Color(0xFF8B5CF6)   // Purple

                    "Water Intake" ->
                        Color(0xFF0EA5E9)   // Sky Blue

                    "Calories" ->
                        Color(0xFFF97316)   // Orange

                    "Age Calculator" ->
                        Color(0xFF10B981)   // Emerald

                    "Sleep Calculator" ->
                        Color(0xFF6366F1)   // Indigo

                    else ->
                        MaterialTheme.colorScheme.primary
                },

//                modifier = Modifier.size(42.dp)
                modifier = Modifier.size(52.dp)
            )
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}