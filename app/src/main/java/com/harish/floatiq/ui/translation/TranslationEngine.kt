package com.harish.floatiq.ui.translation
import android.content.Context
interface TranslationEngine {

    suspend fun translate(
        context: Context,
        text: String,
        targetLanguage: TranslationLanguage
    ): String
}