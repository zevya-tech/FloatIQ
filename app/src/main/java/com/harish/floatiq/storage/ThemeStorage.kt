package com.harish.floatiq.storage

import android.content.Context
import com.harish.floatiq.ui.theme.ThemeType

object ThemeStorage {

    private const val PREFS_NAME = "floatiq_theme"
    private const val KEY_THEME = "selected_theme"

    fun saveTheme(
        context: Context,
        theme: ThemeType
    ) {
        context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
            .edit()
            .putString(
                KEY_THEME,
                theme.name
            )
            .apply()
    }

    fun getTheme(
        context: Context
    ): ThemeType {

        val savedTheme =
            context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
                .getString(
                    KEY_THEME,
                    ThemeType.NEON_BLUE.name
                )

        return try {
            ThemeType.valueOf(savedTheme!!)
        } catch (e: Exception) {
            ThemeType.NEON_BLUE
        }
    }
}