package com.harish.floatiq.ui.translation

object AppStrings {

    fun languageSettings(
        language: TranslationLanguage
    ): String {

        return when (language) {

            TranslationLanguage.ENGLISH ->
                "Language Settings"

            TranslationLanguage.HINDI ->
                "भाषा सेटिंग्स"

            TranslationLanguage.TELUGU ->
                "భాషా సెట్టింగ్స్"
        }
    }

    fun languageDescription(
        language: TranslationLanguage
    ): String {

        return when (language) {

            TranslationLanguage.ENGLISH ->
                "Choose your preferred language for AI explanations and future translated content."

            TranslationLanguage.HINDI ->
                "AI स्पष्टीकरण और भविष्य की अनुवादित सामग्री के लिए अपनी पसंदीदा भाषा चुनें।"

            TranslationLanguage.TELUGU ->
                "AI వివరణలు మరియు భవిష్యత్తులో అనువదించిన కంటెంట్ కోసం మీ భాషను ఎంచుకోండి."
        }
    }

    fun currentLanguage(
        language: TranslationLanguage
    ): String {

        return when (language) {

            TranslationLanguage.ENGLISH ->
                "Current Language"

            TranslationLanguage.HINDI ->
                "वर्तमान भाषा"

            TranslationLanguage.TELUGU ->
                "ప్రస్తుత భాష"
        }
    }

    fun installed(
        language: TranslationLanguage
    ): String {

        return when (language) {

            TranslationLanguage.ENGLISH ->
                "Installed"

            TranslationLanguage.HINDI ->
                "इंस्टॉल"

            TranslationLanguage.TELUGU ->
                "ఇన్‌స్టాల్"
        }
    }

    fun download(
        language: TranslationLanguage
    ): String {

        return when (language) {

            TranslationLanguage.ENGLISH ->
                "Download"

            TranslationLanguage.HINDI ->
                "डाउनलोड"

            TranslationLanguage.TELUGU ->
                "డౌన్‌లోడ్"
        }
    }

    fun back(
        language: TranslationLanguage
    ): String {

        return when (language) {

            TranslationLanguage.ENGLISH ->
                "Back"

            TranslationLanguage.HINDI ->
                "वापस"

            TranslationLanguage.TELUGU ->
                "వెనుకకు"
        }
    }
    fun currentAILanguage(
        language: TranslationLanguage
    ): String {

        return when (language) {

            TranslationLanguage.ENGLISH ->
                "Current AI Language"

            TranslationLanguage.HINDI ->
                "वर्तमान AI भाषा"

            TranslationLanguage.TELUGU ->
                "ప్రస్తుత AI భాష"
        }
    }
    fun aiTranslationLanguage(
        language: TranslationLanguage
    ): String {

        return when (language) {

            TranslationLanguage.ENGLISH ->
                "AI Translation Language"

            TranslationLanguage.HINDI ->
                "AI अनुवाद भाषा"

            TranslationLanguage.TELUGU ->
                "AI అనువాద భాష"
        }
    }
    fun aiLanguageDescription(
        language: TranslationLanguage
    ): String {

        return when (language) {

            TranslationLanguage.ENGLISH ->
                "Choose language for OCR explanations, AI explanations and future AI features."

            TranslationLanguage.HINDI ->
                "OCR स्पष्टीकरण, AI स्पष्टीकरण और भविष्य की AI सुविधाओं के लिए भाषा चुनें।"

            TranslationLanguage.TELUGU ->
                "OCR వివరణలు, AI వివరణలు మరియు భవిష్యత్తు AI ఫీచర్ల కోసం భాషను ఎంచుకోండి."
        }
    }
    fun delete(
        language: TranslationLanguage
    ): String {

        return when(language) {

            TranslationLanguage.ENGLISH ->
                "Delete"

            TranslationLanguage.HINDI ->
                "हटाएँ"

            TranslationLanguage.TELUGU ->
                "తొలగించు"
        }
    }fun deleteLanguagePack(
        language: TranslationLanguage
    ): String {

        return when(language) {

            TranslationLanguage.ENGLISH ->
                "Delete Language Pack?"

            TranslationLanguage.HINDI ->
                "भाषा पैक हटाएँ?"

            TranslationLanguage.TELUGU ->
                "భాషా ప్యాక్ తొలగించాలా?"
        }
    }
    fun deleteLanguageDescription(
        language: TranslationLanguage
    ): String {

        return when(language) {

            TranslationLanguage.ENGLISH ->
                "This will remove the offline AI language model from your device."

            TranslationLanguage.HINDI ->
                "यह आपके डिवाइस से ऑफलाइन AI भाषा मॉडल को हटा देगा।"

            TranslationLanguage.TELUGU ->
                "ఇది మీ పరికరం నుండి ఆఫ్‌లైన్ AI భాషా మోడల్‌ను తొలగిస్తుంది."
        }
    }

}