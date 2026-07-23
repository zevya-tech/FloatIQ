package com.harish.floatiq.ui.converter

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.MaterialTheme
//import com.harish.floatiq.ui.ads.NativeAdCard
import com.harish.floatiq.ui.components.FeatureGuideDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.platform.LocalContext
import com.harish.floatiq.storage.EngagementTracker
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitConverterScreen(
    onBack: () -> Unit
) {

    BackHandler {
        onBack()
    }
    val context = LocalContext.current
    var input by remember {
        mutableStateOf("")
    }

    var result by remember {
        mutableStateOf("")
    }
    var showInputError by remember {
        mutableStateOf(false)
    }
    val categories = listOf(
        "Length",
        "Weight",
        "Temperature",
        "Area",
        "Volume",
        "Speed",
        "Time",
        "Data Storage"
    )

    var category by remember {
        mutableStateOf("Length")
    }

    var expandedCategory by remember {
        mutableStateOf(false)
    }

    val units = remember(category) {
        UnitConverterEngine.getUnits(category)
    }

    var fromUnit by remember {
        mutableStateOf(units.first())
    }

    var toUnit by remember {
        mutableStateOf(units.last())
    }
    LaunchedEffect(category) {

        val updatedUnits =
            UnitConverterEngine.getUnits(category)

        fromUnit = updatedUnits.first()

        toUnit = updatedUnits.last()

        result = ""
    }
    var expandedFrom by remember {
        mutableStateOf(false)
    }

    var expandedTo by remember {
        mutableStateOf(false)
    }
    var showConverterGuide by remember {
        mutableStateOf(false)
    }
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(
//                rememberScrollState()
//            )
//
//            .background(
//                Brush.verticalGradient(
//                    listOf(
//                        MaterialTheme.colorScheme.primary,
//                        MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
//                        MaterialTheme.colorScheme.background
//                    )
//                )
//            )
//            .padding(20.dp),
//
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
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
                .padding(20.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Unit Converter",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
                            RoundedCornerShape(16.dp)
                        )
                        .clickable {

                            showConverterGuide = true
                        },

                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = "Converter Guide",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            ExposedDropdownMenuBox(
                expanded = expandedCategory,
                onExpandedChange = {
                    expandedCategory = !expandedCategory
                }
            ) {

                OutlinedTextField(
                    value = category,
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text("Category")
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expandedCategory
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedCategory,
                    onDismissRequest = {
                        expandedCategory = false
                    }
                ) {

                    categories.forEach { item ->

                        DropdownMenuItem(
                            text = {
                                Text(item)
                            },
                            onClick = {

                                category = item
                                expandedCategory = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(28.dp),

//            colors = CardDefaults.cardColors(
//                containerColor = Color(0xFF1E293B)
//            )
                colors = CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme.surface
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),

                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = input,

                        onValueChange = {

                            input = it

                            if (it.isNotBlank()) {
                                showInputError = false
                            }
                        },
                        label = {
                            Text("Enter Value")
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
                            keyboardType = KeyboardType.Decimal
                        )
                    )
                    if (showInputError) {

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = "Input value required",
//                        color = Color.Red,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    ExposedDropdownMenuBox(
                        expanded = expandedFrom,
                        onExpandedChange = {
                            expandedFrom = !expandedFrom
                        }
                    ) {

                        OutlinedTextField(
                            value = fromUnit,
                            onValueChange = {},
                            readOnly = true,
                            label = {
                                Text("From")
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
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expandedFrom
                                )
                            },

                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = expandedFrom,
                            onDismissRequest = {
                                expandedFrom = false
                            }
                        ) {

                            units.forEach { unit ->

                                DropdownMenuItem(
                                    text = {
                                        Text(unit)
                                    },
                                    onClick = {

                                        fromUnit = unit
                                        expandedFrom = false
                                    }
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),

                        contentAlignment = Alignment.Center
                    ) {

                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor =
                                    MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier
                                .width(60.dp)
                                .height(60.dp),

                            shape = RoundedCornerShape(50.dp),

                            onClick = {

                                val temp = fromUnit
                                fromUnit = toUnit
                                toUnit = temp
                            }
                        ) {

                            Text("⇅")
                        }
                    }

                    ExposedDropdownMenuBox(
                        expanded = expandedTo,
                        onExpandedChange = {
                            expandedTo = !expandedTo
                        }
                    ) {

                        OutlinedTextField(
                            value = toUnit,
                            onValueChange = {},
                            readOnly = true,
                            label = {
                                Text("To")
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expandedTo
                                )
                            },

                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = expandedTo,
                            onDismissRequest = {
                                expandedTo = false
                            }
                        ) {

                            units.forEach { unit ->

                                DropdownMenuItem(
                                    text = {
                                        Text(unit)
                                    },
                                    onClick = {

                                        toUnit = unit
                                        expandedTo = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor =
                                MaterialTheme.colorScheme.primary
                        ),

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        onClick = {
                            if (input.isBlank()) {

                                showInputError = true
                                return@Button
                            }
                            val value =
                                input.toDoubleOrNull() ?: 0.0


                            val converted =
                                UnitConverterEngine.convert(
                                    category,
                                    value,
                                    fromUnit,
                                    toUnit
                                )


                            result =
                                "$value $fromUnit = ${
                                    UnitConverterEngine.formatResult(converted)
                                } $toUnit"
                            EngagementTracker
                                .onSuccessfulCalculation(
                                    context
                                )
                        }
                    ) {

                        Text("Convert")
                    }

                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (result.isNotEmpty()) {

                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth(),

                    shape = RoundedCornerShape(24.dp),

//                colors = CardDefaults.cardColors(
//                    containerColor = Color(0xFF0284C7)
//                )
                    colors = CardDefaults.cardColors(
                        containerColor =
                            MaterialTheme.colorScheme.primary
                    )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),

                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Result",
//                        color = Color.White,
                            color =
                                MaterialTheme.colorScheme.onPrimary,
                            fontSize = 16.sp
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = result,
//                        color = Color.White,
                            color =
                                MaterialTheme.colorScheme.onPrimary,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
//            Spacer(
//                modifier = Modifier.height(20.dp)
//            )
//
//            NativeAdCard()
        }
    }
    if (showConverterGuide) {

        FeatureGuideDialog(

            title = "📏 Unit Converter Guide",

            guideText =
                """
• Length Conversion
• Weight Conversion
• Temperature Conversion
• Area Conversion
• Volume Conversion
• Speed Conversion
• Time Conversion
• Data Storage Conversion

How to Use

1. Select a category
2. Enter a value
3. Choose From unit
4. Choose To unit
5. Tap Convert

Extra Features

⇅ Swap Button
• Instantly swaps From and To units

Examples

Length:
1 km → 1000 m

Weight:
1 kg → 1000 g

Temperature:
0°C → 32°F
""".trimIndent(),

            onDismiss = {

                showConverterGuide = false
            }
        )
    }
}