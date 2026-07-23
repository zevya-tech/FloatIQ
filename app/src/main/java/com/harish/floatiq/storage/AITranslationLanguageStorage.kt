package com.harish.floatiq.storage

import android.content.Context
import com.harish.floatiq.ui.translation.TranslationLanguage

object AITranslationLanguageStorage {

    private const val PREF_NAME =
        "floatiq_ai_language"

    private const val KEY_LANGUAGE =
        "selected_ai_language"

    fun saveLanguage(
        context: Context,
        language: TranslationLanguage
    ) {

        context
            .getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
            .edit()
            .putString(
                KEY_LANGUAGE,
                language.code
            )
            .apply()
    }

    fun getLanguage(
        context: Context
    ): TranslationLanguage {

        val code =
            context
                .getSharedPreferences(
                    PREF_NAME,
                    Context.MODE_PRIVATE
                )
                .getString(
                    KEY_LANGUAGE,
                    "en"
                )

        return TranslationLanguage.entries.firstOrNull {

            it.code == code

        } ?: TranslationLanguage.ENGLISH
    }
}