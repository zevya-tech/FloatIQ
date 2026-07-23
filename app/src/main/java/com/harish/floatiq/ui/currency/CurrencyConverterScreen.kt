package com.harish.floatiq.ui.currency

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.MaterialTheme
//import com.harish.floatiq.ui.ads.NativeAdCard
import com.airbnb.lottie.compose.*
import com.harish.floatiq.R
import com.harish.floatiq.ui.components.FeatureGuideDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import android.widget.Toast
import android.util.Log
import com.harish.floatiq.storage.EngagementTracker
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyConverterScreen(
    onBack: () -> Unit
) {

    BackHandler {
        onBack()
    }

    var amount by remember {
        mutableStateOf("")
    }

    var result by remember {
        mutableStateOf("")
    }
    var exchangeRate by remember {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(false)
    }
    var internetError by remember {
        mutableStateOf(false)
    }
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.currency_loading
        )
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    val scope = rememberCoroutineScope()
    val context = androidx.compose.ui.platform.LocalContext.current
    var showError by remember {
        mutableStateOf(false)
    }

    val currencies =
        CurrencyData.currencies

    var fromCurrency by remember {
        mutableStateOf("USD")
    }

    var toCurrency by remember {
        mutableStateOf("INR")
    }

    var expandedFrom by remember {
        mutableStateOf(false)
    }

    var expandedTo by remember {
        mutableStateOf(false)
    }
    var showCurrencyGuide by remember {
        mutableStateOf(false)
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
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(20.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Currency Converter",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 30.sp,
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

                            showCurrencyGuide = true
                        },

                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = "Currency Guide",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(30.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(28.dp),

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

                        value = amount,
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

                            amount = it

                            if (it.isNotBlank()) {
                                showError = false
                            }
                        },

                        label = {
                            Text("Enter Amount")
                        },

                        keyboardOptions =
                            KeyboardOptions(
                                keyboardType =
                                    KeyboardType.Decimal
                            )
                    )

                    if (showError) {

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = "Amount required",
                            color = Color.Red
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )

                    ExposedDropdownMenuBox(
                        expanded = expandedFrom,
                        onExpandedChange = {
                            expandedFrom = !expandedFrom
                        }
                    ) {

                        OutlinedTextField(
                            value = fromCurrency,
                            onValueChange = {},
                            readOnly = true,
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),

                            label = {
                                Text("From Currency")
                            },

                            trailingIcon = {
                                ExposedDropdownMenuDefaults
                                    .TrailingIcon(
                                        expanded =
                                            expandedFrom
                                    )
                            }
                        )

                        ExposedDropdownMenu(
                            expanded = expandedFrom,
                            onDismissRequest = {
                                expandedFrom = false
                            }
                        ) {

                            currencies.forEach {

                                DropdownMenuItem(
                                    text = {
                                        Text(it)
                                    },

                                    onClick = {

                                        fromCurrency = it
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

                        contentAlignment =
                            Alignment.Center
                    ) {

                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor =
                                    MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier
                                .width(60.dp)
                                .height(60.dp),

                            shape =
                                RoundedCornerShape(50.dp),

                            onClick = {

                                val temp =
                                    fromCurrency

                                fromCurrency =
                                    toCurrency

                                toCurrency =
                                    temp
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
                            value = toCurrency,
                            onValueChange = {},
                            readOnly = true,

                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),

                            label = {
                                Text("To Currency")
                            },

                            trailingIcon = {
                                ExposedDropdownMenuDefaults
                                    .TrailingIcon(
                                        expanded =
                                            expandedTo
                                    )
                            }
                        )

                        ExposedDropdownMenu(
                            expanded = expandedTo,
                            onDismissRequest = {
                                expandedTo = false
                            }
                        ) {

                            currencies.forEach {

                                DropdownMenuItem(
                                    text = {
                                        Text(it)
                                    },

                                    onClick = {

                                        toCurrency = it
                                        expandedTo = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(30.dp)
                    )

                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor =
                                MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),

                        enabled =
                            amount.isNotBlank(),

                        onClick = {
                            if (amount.isBlank()) {

                                showError = true
                                return@Button
                            }
                            if (fromCurrency == toCurrency) {

                                Toast.makeText(
                                    context,
                                    "Source and target currencies cannot be the same",
                                    Toast.LENGTH_SHORT
                                ).show()

                                return@Button
                            }
                            val inputAmount = amount.toDoubleOrNull()

                            if (inputAmount == null) {

                                Toast.makeText(
                                    context,
                                    "Input format is not valid",
                                    Toast.LENGTH_SHORT
                                ).show()

                                return@Button
                            }
                            scope.launch {

                                try {
                                    internetError = false
                                    isLoading = true

                                    val response =
                                        RetrofitInstance.api.getRate(
                                            fromCurrency,
                                            toCurrency
                                        )

                                    if (
                                        response.isSuccessful
                                    ) {

                                        val rate =
                                            response.body()
                                                ?.rates
                                                ?.values
                                                ?.firstOrNull()
                                                ?: 0.0

                                        val inputAmount =
                                            amount.toDoubleOrNull()
                                                ?: 0.0

                                        val converted =
                                            inputAmount * rate

                                        exchangeRate =
                                            "1 $fromCurrency = ${
                                                CurrencyConverterEngine
                                                    .formatResult(rate)
                                            } $toCurrency"

                                        result =
                                            "${CurrencyConverterEngine.formatResult(inputAmount)} $fromCurrency = " +
                                                    "${
                                                        CurrencyConverterEngine.formatResult(
                                                            converted
                                                        )
                                                    } $toCurrency"
                                        EngagementTracker
                                            .onSuccessfulCalculation(
                                                context
                                            )
                                    } else {

                                        result =
                                            "API Error: ${response.code()}"
                                    }

                              }
                                catch (e: Exception) {



//                                    result = e.toString()

                                    internetError = true
                                }

                          finally {

                                    isLoading = false
                                }
                            }

                        }
                    ) {


                        Text(
                            if (isLoading)
                                "Fetching Rates..."
                            else
                                "Convert Currency"
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            if (isLoading) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
//                    modifier = Modifier.size(180.dp)
                        modifier = Modifier.size(220.dp)
                    )

                    Text(
                        text = "Fetching Live Exchange Rates...",
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )
                }
            }
            if (internetError) {

                Card(
                    modifier = Modifier.fillMaxWidth(),

                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFB91C1C)
                    )
                ) {

                    Text(
                        text =
                            "Internet connection is required for this Live Currency feature",

                        color = Color.White,

                        textAlign = TextAlign.Center,

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    )
                }
                Spacer(
                    modifier = Modifier.height(20.dp)
                )
            }

            if (exchangeRate.isNotEmpty()) {

                Card(
                    modifier = Modifier.fillMaxWidth(),

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                MaterialTheme.colorScheme.surface
                        )
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Text(
                            text = "Exchange Rate",
//                        color = Color.White,
                            color =
                                MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = exchangeRate,
                            color = Color.White
                        )
                    }
                }
            }
            if (result.isNotEmpty()) {

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                MaterialTheme.colorScheme.primary
                        )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),

                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Result",
                            color = MaterialTheme.colorScheme.onPrimary
                        )

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        Text(
                            text = result,
                            color =
                                MaterialTheme.colorScheme.onPrimary,
                            fontSize = 24.sp,
                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }
                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = "Currency rates are sourced from reference market data and may differ slightly from real-time exchange rates.",

                    fontSize = 12.sp,

                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),

                    textAlign = TextAlign.Center,

                    modifier = Modifier.fillMaxWidth()
                )
            }
//            Spacer(
//                modifier = Modifier.height(20.dp)
//            )
//
//            NativeAdCard()

        }
    }
    if (showCurrencyGuide) {

        FeatureGuideDialog(

            title = "💱 Currency Converter Guide",

            guideText =
                """
• Live Exchange Rates
• 150+ Currency Support
• Currency Swap Button
• Real-Time Conversion

How to Use

1. Enter amount
2. Select source currency
3. Select target currency
4. Tap Convert Currency
5. View live result

Swap Button

⇅ swaps source and target currencies instantly

Examples

1 USD → INR

1 EUR → GBP

1 AED → INR

Important

• Internet connection required
• Rates update from online market data
• Actual bank rates may differ slightly
""".trimIndent(),

            onDismiss = {

                showCurrencyGuide = false
            }
        )
    }
}