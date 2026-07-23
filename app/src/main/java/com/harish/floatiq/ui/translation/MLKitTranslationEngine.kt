package com.harish.floatiq.ui.translation

//import com.google.mlkit.nl.translate.TranslateLanguage
//import com.google.mlkit.nl.translate.Translation
//import com.google.mlkit.nl.translate.TranslatorOptions
//import kotlinx.coroutines.tasks.await
//import android.content.Context
//import com.google.mlkit.nl.translate.TranslateRemoteModel
//import com.google.mlkit.common.model.RemoteModelManager
//class MLKitTranslationEngine : TranslationEngine {
//
//    override suspend fun translate(
//        context: Context,
//        text: String,
//        targetLanguage: TranslationLanguage
//    ): String {
//
//        if (targetLanguage == TranslationLanguage.ENGLISH) {
//            return text
//        }
//
//        val targetCode = when (targetLanguage) {
//
//            TranslationLanguage.TELUGU ->
//                TranslateLanguage.TELUGU
//
//            TranslationLanguage.HINDI ->
//                TranslateLanguage.HINDI
//
//            else ->
//                TranslateLanguage.ENGLISH
//        }
//
//        val options = TranslatorOptions.Builder()
//            .setSourceLanguage(
//                TranslateLanguage.ENGLISH
//            )
//            .setTargetLanguage(
//                targetCode
//            )
//            .build()
//
//        val translator =
//            Translation.getClient(options)
//
////        translator.downloadModelIfNeeded()
////            .await()
//
//        translator.downloadModelIfNeeded()
//            .await()
//
//        TranslationDownloadManager.markInstalled(
//            context,
//            targetLanguage
//        )
//        return translator.translate(text)
//            .await()
//    }
//    suspend fun deleteModel(
//        context: Context,
//        language: TranslationLanguage
//    ) {
//
//        if (language == TranslationLanguage.ENGLISH) {
//            return
//        }
//
//        val languageCode = when (language) {
//
//            TranslationLanguage.HINDI ->
//                TranslateLanguage.HINDI
//
//            TranslationLanguage.TELUGU ->
//                TranslateLanguage.TELUGU
//
//            else ->
//                return
//        }
//
//        val model =
//            TranslateRemoteModel.Builder(
//                languageCode
//            ).build()
//
//        RemoteModelManager
//            .getInstance()
//            .deleteDownloadedModel(model)
//            .await()
//
//        TranslationDownloadManager
//            .markNotInstalled(
//                context,
//                language
//            )
//    }
//}