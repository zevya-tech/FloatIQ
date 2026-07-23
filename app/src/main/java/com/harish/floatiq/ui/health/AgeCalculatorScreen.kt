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
import java.time.LocalDate
import java.time.Period
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.material3.MaterialTheme
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import com.harish.floatiq.ui.components.FeatureGuideDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material3.Icon
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Public
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.platform.LocalContext
import com.harish.floatiq.storage.EngagementTracker
@Composable
fun AgeCalculatorScreen(
    onBack: () -> Unit
) {

    BackHandler {
        onBack()
    }
    val context = LocalContext.current
    var day by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }

    var ageResult by remember {
        mutableStateOf("")
    }

    var totalMonths by remember {
        mutableStateOf("")
    }

    var totalDays by remember {
        mutableStateOf("")
    }
    var birthdayCountdown by remember {
        mutableStateOf("")
    }

    var zodiacSign by remember {
        mutableStateOf("")
    }

    var weekdayBorn by remember {
        mutableStateOf("")
    }

    var mercuryAge by remember {
        mutableStateOf("")
    }

    var venusAge by remember {
        mutableStateOf("")
    }

    var marsAge by remember {
        mutableStateOf("")
    }

    var jupiterAge by remember {
        mutableStateOf("")
    }
    var showAgeGuide by remember {
        mutableStateOf(false)
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


//            Text(
//                text = "🎂 Age Calculator",
//                color = MaterialTheme.colorScheme.onBackground,
//                fontSize = 32.sp,
//                fontWeight = FontWeight.Bold
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.Cake,
                        contentDescription = "Age Calculator",
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(40.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(12.dp)
                    )

                    Text(
                        text = "Age Calculator",
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
                            showAgeGuide = true
                        },

                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = "Age Guide",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            OutlinedTextField(
                value = day,
                onValueChange = { day = it },
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
                label = { Text("Day") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = month,
                onValueChange = { month = it },
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
                label = { Text("Month") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = year,
                onValueChange = { year = it },
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
                label = { Text("Year") },
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
                        day.isBlank() ||
                        month.isBlank() ||
                        year.isBlank()
                    ) {

                        scope.launch {

                            snackbarHostState.showSnackbar(
                                "Day, Month and Year are required"
                            )
                        }

                        ageResult = ""
                        totalMonths = ""
                        totalDays = ""
                        birthdayCountdown = ""
                        zodiacSign = ""
                        weekdayBorn = ""

                        mercuryAge = ""
                        venusAge = ""
                        marsAge = ""
                        jupiterAge = ""

                        return@Button
                    }
                    try {

                        val birthDate =
                            LocalDate.of(
                                year.toInt(),
                                month.toInt(),
                                day.toInt()
                            )
                        weekdayBorn =
                            birthDate.dayOfWeek.name
                        val today =
                            LocalDate.now()
                        var nextBirthday =
                            birthDate.withYear(today.year)

                        if (nextBirthday.isBefore(today)) {

                            nextBirthday =
                                nextBirthday.plusYears(1)
                        }

                        birthdayCountdown =
                            java.time.temporal.ChronoUnit.DAYS
                                .between(
                                    today,
                                    nextBirthday
                                )
                                .toString() + " days"
                        zodiacSign =
                            when {

                                (month.toInt() == 3 && day.toInt() >= 21) ||
                                        (month.toInt() == 4 && day.toInt() <= 19)
                                    -> "Aries ♈"

                                (month.toInt() == 4 && day.toInt() >= 20) ||
                                        (month.toInt() == 5 && day.toInt() <= 20)
                                    -> "Taurus ♉"

                                (month.toInt() == 5 && day.toInt() >= 21) ||
                                        (month.toInt() == 6 && day.toInt() <= 20)
                                    -> "Gemini ♊"

                                (month.toInt() == 6 && day.toInt() >= 21) ||
                                        (month.toInt() == 7 && day.toInt() <= 22)
                                    -> "Cancer ♋"

                                (month.toInt() == 7 && day.toInt() >= 23) ||
                                        (month.toInt() == 8 && day.toInt() <= 22)
                                    -> "Leo ♌"

                                (month.toInt() == 8 && day.toInt() >= 23) ||
                                        (month.toInt() == 9 && day.toInt() <= 22)
                                    -> "Virgo ♍"

                                (month.toInt() == 9 && day.toInt() >= 23) ||
                                        (month.toInt() == 10 && day.toInt() <= 22)
                                    -> "Libra ♎"

                                (month.toInt() == 10 && day.toInt() >= 23) ||
                                        (month.toInt() == 11 && day.toInt() <= 21)
                                    -> "Scorpio ♏"

                                (month.toInt() == 11 && day.toInt() >= 22) ||
                                        (month.toInt() == 12 && day.toInt() <= 21)
                                    -> "Sagittarius ♐"

                                (month.toInt() == 12 && day.toInt() >= 22) ||
                                        (month.toInt() == 1 && day.toInt() <= 19)
                                    -> "Capricorn ♑"

                                (month.toInt() == 1 && day.toInt() >= 20) ||
                                        (month.toInt() == 2 && day.toInt() <= 18)
                                    -> "Aquarius ♒"

                                else -> "Pisces ♓"
                            }
                        val period =
                            Period.between(
                                birthDate,
                                today
                            )

                        ageResult =
                            "${period.years} Years, ${period.months} Months, ${period.days} Days"

                        totalMonths =
                            ((period.years * 12) + period.months).toString()

                        totalDays =
                            java.time.temporal.ChronoUnit.DAYS
                                .between(
                                    birthDate,
                                    today
                                )
                                .toString()
                        keyboardController?.hide()
                        val earthYears =
                            period.years.toDouble()

                        mercuryAge =
                            String.format("%.1f", earthYears / 0.24)

                        venusAge =
                            String.format("%.1f", earthYears / 0.62)

                        marsAge =
                            String.format("%.1f", earthYears / 1.88)

                        jupiterAge =
                            String.format("%.1f", earthYears / 11.86)
                        EngagementTracker
                            .onSuccessfulCalculation(
                                context
                            )
                    } catch (e: Exception) {
                        scope.launch {

                            snackbarHostState.showSnackbar(
                                "Please enter a valid date"
                            )
                        }
                        ageResult = "Invalid Date"
                        totalMonths = ""
                        totalDays = ""
                        birthdayCountdown = ""
                        zodiacSign = ""
                        weekdayBorn = ""

                        mercuryAge = ""
                        venusAge = ""
                        marsAge = ""
                        jupiterAge = ""
                        keyboardController?.hide()

                    }
                }
            ) {
                Text("Calculate Age")
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (ageResult.isNotEmpty()) {

                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor =
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)

                    ) {
                        Column {

                            AgeInfoCard(
                                icon = Icons.Default.Event,
                                title = "Age",
                                value = ageResult,
                                iconColor = Color(0xFF10B981)
                            )

                            AgeInfoCard(
                                icon = Icons.Default.CalendarMonth,
                                title = "Total Months",
                                value = totalMonths,
                                iconColor = Color(0xFF3B82F6)
                            )

                            AgeInfoCard(
                                icon = Icons.Default.Today,
                                title = "Total Days",
                                value = totalDays,
                                iconColor = Color(0xFFF59E0B)
                            )

                            AgeInfoCard(
                                icon = Icons.Default.Celebration,
                                title = "Next Birthday",
                                value = birthdayCountdown,
                                iconColor = Color(0xFFEC4899)
                            )

                            AgeInfoCard(
                                icon = Icons.Default.AutoAwesome,
                                title = "Zodiac Sign",
                                value = zodiacSign,
                                iconColor = Color(0xFF8B5CF6)
                            )

                            AgeInfoCard(
                                icon = Icons.Default.Today,
                                title = "Born On",
                                value = weekdayBorn,
                                iconColor = Color(0xFF06B6D4)
                            )

                            AgeInfoCard(
                                icon = Icons.Default.Public,
                                title = "Mercury Age",
                                value = "$mercuryAge Years",
                                iconColor = Color(0xFFEF4444)
                            )

                            AgeInfoCard(
                                icon = Icons.Default.Public,
                                title = "Venus Age",
                                value = "$venusAge Years",
                                iconColor = Color(0xFFF97316)
                            )

                            AgeInfoCard(
                                icon = Icons.Default.Public,
                                title = "Mars Age",
                                value = "$marsAge Years",
                                iconColor = Color(0xFFDC2626)
                            )

                            AgeInfoCard(
                                icon = Icons.Default.Public,
                                title = "Jupiter Age",
                                value = "$jupiterAge Years",
                                iconColor = Color(0xFF14B8A6)
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
    if (showAgeGuide) {

        FeatureGuideDialog(

            title = "🎂 Age Calculator Guide",

            guideText =
                """
What is this tool?

This calculator provides detailed age information based on your date of birth.

Required Inputs

• Day
• Month
• Year

Available Results

🎂 Exact Age
Years, months and days.

📆 Total Months
Total age expressed in months.

📅 Total Days
Total age expressed in days.

🎉 Next Birthday
Days remaining until your next birthday.

♈ Zodiac Sign
Your zodiac sign based on birth date.

📅 Day Born
Shows the weekday you were born.

🪐 Planetary Age
Age converted to:

• Mercury
• Venus
• Mars
• Jupiter

How to Use

1. Enter day, month and year.
2. Tap Calculate Age.
3. Review all age statistics.

Interesting Fact

Because planets orbit the Sun at different speeds, your age changes depending on the planet.

Example:

If you are 24 years old on Earth:

• Mercury ≈ 100 years
• Venus ≈ 39 years
• Mars ≈ 13 years
• Jupiter ≈ 2 years

Important

Results depend on the accuracy of the entered birth date.
"""
                    .trimIndent(),

            onDismiss = {
                showAgeGuide = false
            }
        )
    }
}
@Composable
fun AgeInfoCard(
    icon: ImageVector,
    title: String,
    value: String,
    iconColor: Color
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),

        shape = RoundedCornerShape(18.dp),

        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
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
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = value,
                    color =
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                )
            }
        }
    }
}