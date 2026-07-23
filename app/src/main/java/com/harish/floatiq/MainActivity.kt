
package com.harish.floatiq
//import com.google.android.gms.ads.MobileAds
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.harish.floatiq.ui.theme.FloatIQTheme
import com.harish.floatiq.ui.screens.HomeScreen
import com.harish.floatiq.ui.calculator.CalculatorScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.harish.floatiq.ui.health.HealthHubScreen
import androidx.compose.runtime.*
import com.harish.floatiq.core.ClipboardMonitor
import com.harish.floatiq.ui.health.BMIScreen
import com.harish.floatiq.ui.health.WaterIntakeScreen
import com.harish.floatiq.ui.health.CaloriesScreen
import com.harish.floatiq.ui.health.AgeCalculatorScreen
import com.harish.floatiq.ui.health.SleepCalculatorScreen
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import com.harish.floatiq.ui.history.HistoryScreen
import com.harish.floatiq.ui.converter.UnitConverterScreen
import com.harish.floatiq.ui.currency.CurrencyConverterScreen
import com.harish.floatiq.ui.theme.ThemeScreen
import com.harish.floatiq.ui.theme.ThemeManager
import com.harish.floatiq.ui.ocr.OCRScannerScreen
import com.harish.floatiq.ui.screens.FormulaExplorerScreen
import com.harish.floatiq.storage.ThemeStorage
//import com.harish.floatiq.ui.settings.LanguageSettingsScreen
import com.harish.floatiq.core.NavigationManager
import android.content.Intent
import android.view.KeyEvent
import com.harish.floatiq.ui.ocr.OCRCaptureManager

class MainActivity : ComponentActivity() {
    override fun onKeyDown(
        keyCode: Int,
        event: KeyEvent?
    ): Boolean {

        if (
            keyCode == KeyEvent.KEYCODE_VOLUME_UP &&
            OCRCaptureManager.isScannerOpen
        ) {
            OCRCaptureManager.onCapture?.invoke()
            return true
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
//        MobileAds.initialize(this)
        ThemeManager.currentTheme.value =
            ThemeStorage.getTheme(this)
        val clipboardMonitor =
            ClipboardMonitor(this)

        clipboardMonitor.startMonitoring()
        enableEdgeToEdge()
        NavigationManager.currentScreen.value =
            intent.getStringExtra("open_screen")
                ?: "home"
        setContent {

            val selectedTheme by
            ThemeManager.currentTheme
            LaunchedEffect(selectedTheme) {

                android.util.Log.d(
                    "THEME_DEBUG",
                    "Current Theme = $selectedTheme"
                )
            }
            FloatIQTheme(
                selectedTheme = selectedTheme
            ) {



                val currentScreen by
                NavigationManager.currentScreen

                var isBackNavigation by remember {
                    mutableStateOf(false)
                }

                AnimatedContent(

                    targetState = currentScreen,


                    transitionSpec = {

                        if (isBackNavigation) {

                            slideInHorizontally(
                                animationSpec = tween(300),
                                initialOffsetX = { -it }
                            ) + fadeIn() togetherWith

                                    slideOutHorizontally(
                                        animationSpec = tween(300),
                                        targetOffsetX = { it }
                                    ) + fadeOut()

                        } else {

                            slideInHorizontally(
                                animationSpec = tween(300),
                                initialOffsetX = { it }
                            ) + fadeIn() togetherWith

                                    slideOutHorizontally(
                                        animationSpec = tween(300),
                                        targetOffsetX = { -it }
                                    ) + fadeOut()
                        }
                    },
                    label = "screen_animation"

                ) { screen ->

                    when (screen) {

                        "home" -> {

                            HomeScreen(

                                onOpenCalculator = {
                                    isBackNavigation = false
                                    NavigationManager.currentScreen.value ="calculator"
                                },

                                onOpenHealthHub = {
                                    isBackNavigation = false
                                    NavigationManager.currentScreen.value ="health"
                                },


                                onOpenUnitConverter = {
                                    isBackNavigation = false
                                    NavigationManager.currentScreen.value ="unit_converter"
                                },
                                onOpenCurrencyConverter = {
                                    isBackNavigation = false
                                    NavigationManager.currentScreen.value ="currency_converter"
                                },
                                        onOpenThemes = {
                                    isBackNavigation = false
                                            NavigationManager.currentScreen.value ="themes"
                                },

                                onOpenOCR = {
                                    isBackNavigation = false
                                    NavigationManager.currentScreen.value ="ocr"
                                },

//                                onOpenLanguageSettings = {
//                                    isBackNavigation = false
 //                               NavigationManager.currentScreen.value ="language_settings"
//                                },
                                        onOpenFormulaExplorer = {
                                    isBackNavigation = false
                                            NavigationManager.currentScreen.value ="formula_explorer"
                                },
                            )
                        }

                        "calculator" -> {

                            CalculatorScreen(

                                onBack = {
                                    isBackNavigation = true
                                    NavigationManager.currentScreen.value ="home"
                                },

                                onOpenHistory = {
                                    isBackNavigation = false
                                    NavigationManager.currentScreen.value ="history"
                                }
                            )
                        }
                        "history" -> {

                            HistoryScreen(

                                onBack = {
                                    isBackNavigation = true
                                    NavigationManager.currentScreen.value ="calculator"
                                }
                            )
                        }
                        "health" -> {

                            HealthHubScreen(

                                onOpenBMI = {
                                    isBackNavigation = false
                                    NavigationManager.currentScreen.value ="bmi"
                                },

                                onOpenWaterIntake = {
                                    isBackNavigation = false
                                    NavigationManager.currentScreen.value ="water"
                                },

                                onOpenCalories = {
                                    isBackNavigation = false
                                    NavigationManager.currentScreen.value ="calories"
                                },

                                onOpenAgeCalculator = {
                                    isBackNavigation = false
                                    NavigationManager.currentScreen.value ="age"
                                },

                                onOpenSleepCalculator = {
                                    isBackNavigation = false
                                    NavigationManager.currentScreen.value ="sleep"
                                },

                                onBack = {
                                    isBackNavigation = true
                                    NavigationManager.currentScreen.value ="home"
                                }
                            )
                        }

                        "bmi" -> {

                            BMIScreen(

                                onBack = {
                                    isBackNavigation = true
                                    NavigationManager.currentScreen.value ="health"
                                }
                            )
                        }

                        "water" -> {

                            WaterIntakeScreen(

                                onBack = {
                                    isBackNavigation = true
                                    NavigationManager.currentScreen.value ="health"
                                }
                            )
                        }

                        "calories" -> {

                            CaloriesScreen(

                                onBack = {
                                    isBackNavigation = true
                                    NavigationManager.currentScreen.value ="health"
                                }
                            )
                        }

                        "age" -> {

                            AgeCalculatorScreen(

                                onBack = {
                                    isBackNavigation = true
                                    NavigationManager.currentScreen.value ="health"
                                }
                            )
                        }

                        "sleep" -> {

                            SleepCalculatorScreen(

                                onBack = {
                                    isBackNavigation = true
                                    NavigationManager.currentScreen.value ="health"
                                }
                            )
                        }

                        "unit_converter" -> {

                            UnitConverterScreen(

                                onBack = {
                                    isBackNavigation = true
                                    NavigationManager.currentScreen.value ="home"
                                }
                            )
                        }
                        "currency_converter" -> {

                            CurrencyConverterScreen(

                                onBack = {
                                    isBackNavigation = true
                                    NavigationManager.currentScreen.value ="home"
                                }
                            )
                        }
                        "themes" -> {

                            ThemeScreen(

                                onBack = {
                                    isBackNavigation = true
                                    NavigationManager.currentScreen.value ="home"
                                }
                            )
                        }
//                        "language_settings" -> {
//
//                            LanguageSettingsScreen(
//
//                                onBack = {
//
//                                    isBackNavigation = true
//                        NavigationManager.currentScreen.value ="home"
//                                }
//                            )
//                        }
                        "formula_explorer" -> {

                            FormulaExplorerScreen(

                                onBack = {

                                    isBackNavigation = true
                                    NavigationManager.currentScreen.value ="home"
                                }
                            )
                        }
                        "ocr" -> {

                            OCRScannerScreen(

                                onBack = {

                                    isBackNavigation = true
                                    NavigationManager.currentScreen.value ="home"
                                }
                            )
                        }
                    }
                }
            }

        }


    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        setIntent(intent)

        intent.getStringExtra("open_screen")?.let {
            NavigationManager.currentScreen.value = it
        }
    }

}

@Composable
fun App() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "FloatIQ")
        }
    }
}