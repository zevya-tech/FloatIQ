package com.harish.floatiq.ui.theme

import androidx.compose.runtime.mutableStateOf

object ThemeManager {

    val currentTheme =
        mutableStateOf(
            ThemeType.NEON_BLUE
        )
}