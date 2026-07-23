package com.harish.floatiq.ui.translation

import android.content.Context
import android.util.Log
object TranslationDownloadManager {

    private const val PREF_NAME =
        "translation_models"

    fun isInstalled(
        context: Context,
        language: TranslationLanguage
    ): Boolean {

        if (language == TranslationLanguage.ENGLISH) {
            return true
        }

        return context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .getBoolean(
                language.code,
                false
            )
    }

    fun markInstalled(
        context: Context,
        language: TranslationLanguage
    ) {

        context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .edit()
            .putBoolean(
                language.code,
                true
            )
            .apply()
    }
    fun markNotInstalled(
        context: Context,
        language: TranslationLanguage
    ) {

        context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .edit()
            .putBoolean(
                language.code,
                false
            )
            .apply()
        Log.d(
            "FLOATIQ_TRANSLATION",
            "Marked ${language.code} as not installed"
        )
    }
}