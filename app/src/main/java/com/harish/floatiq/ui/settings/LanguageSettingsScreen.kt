package com.harish.floatiq.ui.settings
//import com.harish.floatiq.ui.translation.AppStrings
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.platform.LocalContext
//import com.harish.floatiq.ui.translation.TranslationDownloadManager
//import com.harish.floatiq.storage.LanguageStorage
//import com.harish.floatiq.ui.translation.TranslationLanguage
//import androidx.compose.foundation.background
//import androidx.compose.ui.graphics.Color
//import androidx.compose.runtime.LaunchedEffect
//import com.harish.floatiq.ui.translation.TranslationManager
//import kotlinx.coroutines.launch
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import com.harish.floatiq.storage.AITranslationLanguageStorage
//import com.harish.floatiq.ui.theme.ThemeManager
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//
//
//@Composable
//fun LanguageSettingsScreen(
//    onBack: () -> Unit
//) {
//
//    val context = LocalContext.current
//    val theme = ThemeManager.currentTheme
//    var selectedLanguage by remember {
//
//        mutableStateOf(
//            LanguageStorage.getLanguage(context)
//        )
//    }
//    var showDeleteDialog by remember {
//
//        mutableStateOf(false)
//    }
//
//    var languageToDelete by remember {
//
//        mutableStateOf<TranslationLanguage?>(
//            null
//        )
//    }
//    var showDownloadDialog by remember {
//
//        mutableStateOf(false)
//    }
//
//    var languageToDownload by remember {
//
//        mutableStateOf<TranslationLanguage?>(
//            null
//        )
//    }
//
//    var isDownloading by remember {
//
//        mutableStateOf(false)
//    }
//    var refreshTrigger by remember {
//
//        mutableStateOf(0)
//    }
//    var selectedAILanguage by remember {
//
//        mutableStateOf(
//            AITranslationLanguageStorage.getLanguage(
//                context
//            )
//        )
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(
//                rememberScrollState()
//            )
//            .padding(20.dp)
//    ) {
//
//
//        Text(
//            text =
//                AppStrings.languageSettings(
//                    selectedLanguage
//                ),
//            fontSize = 26.sp
//        )
//
//        Spacer(
//            modifier = Modifier.height(20.dp)
//        )
//
//        Text(
//            text = "App Language",
//            style = MaterialTheme.typography.titleMedium
//        )
//        Spacer(
//            modifier = Modifier.height(8.dp)
//        )
//
//        Text(
//            text =
//                AppStrings.languageDescription(
//                    selectedLanguage
//                )
//        )
//        Spacer(
//            modifier = Modifier.height(12.dp)
//        )
//
//
//        Card(
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(16.dp)
//
//        ) {
//
//            Column(
//                modifier = Modifier.padding(16.dp)
//            ) {
//
//                Text(
//                    text =
//                        AppStrings.currentAILanguage(
//                            selectedLanguage
//                        ),
//                    style =
//                        MaterialTheme.typography.labelLarge
//                )
//
//                Spacer(
//                    modifier = Modifier.height(4.dp)
//                )
//
//                Text(
//                    text =
//                        selectedAILanguage.displayName,
//                    style =
//                        MaterialTheme.typography.headlineSmall
//                )
//            }
//        }
//        Spacer(
//            modifier = Modifier.height(24.dp)
//        )
//        refreshTrigger
//
//        TranslationLanguage.entries.forEach { language ->
//
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 6.dp)
//                    .clickable {
//
//                        selectedLanguage = language
//
//                        LanguageStorage.saveLanguage(
//                            context,
//                            language
//                        )
//                    },
//                shape = RoundedCornerShape(16.dp)
//            ) {
//
//                Row(
//                    modifier = Modifier.padding(16.dp)
//                ) {
//
//                    RadioButton(
//                        selected =
//                            selectedLanguage == language,
//
//                        onClick = {
//
//                            selectedLanguage = language
//
//                            LanguageStorage.saveLanguage(
//                                context,
//                                language
//                            )
//                        }
//                    )
//
//                    Spacer(
//                        modifier = Modifier.width(12.dp)
//                    )
//
//                    Text(
//                        text = language.displayName
//                    )
//
//                }
//            }
//        }
//        HorizontalDivider()
//
//        Spacer(
//            modifier = Modifier.height(32.dp)
//        )
//
//
//        Spacer(
//            modifier = Modifier.height(8.dp)
//        )
//
//
//        Text(
//            text =
//                "${AppStrings.currentAILanguage(selectedLanguage)}: ${selectedAILanguage.displayName}",
//            style =
//                MaterialTheme.typography.bodyLarge
//        )
//
//        Spacer(
//            modifier = Modifier.height(12.dp)
//        )
//
//
//        Text(
//            text =
//                AppStrings.aiLanguageDescription(
//                    selectedLanguage
//                )
//        )
//        Spacer(
//            modifier = Modifier.height(8.dp)
//        )
//
//        Text(
//            text =
//                "Downloaded Models: ${
//                    TranslationLanguage.entries.count {
//                        TranslationDownloadManager
//                            .isInstalled(
//                                context,
//                                it
//                            )
//                    }
//                }",
//            style =
//                MaterialTheme.typography.bodySmall
//        )
//        Spacer(
//            modifier = Modifier.height(16.dp)
//        )
//        fun selectAILanguage(language: TranslationLanguage) {
//
//            if (
//                language == TranslationLanguage.ENGLISH ||
//                TranslationDownloadManager.isInstalled(
//                    context,
//                    language
//                )
//            ) {
//
//                selectedAILanguage = language
//
//                AITranslationLanguageStorage.saveLanguage(
//                    context,
//                    language
//                )
//            }
//            else {
//
//                languageToDownload = language
//                showDownloadDialog = true
//            }
//        }
//        TranslationLanguage.entries.forEach { language ->
//
//
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 6.dp)
//                    .clickable {
//                        selectAILanguage(language)
//                    },
//
//                shape = RoundedCornerShape(16.dp),
//
//                elevation =
//                    CardDefaults.cardElevation(
//                        defaultElevation =
//                            if (
//                                selectedAILanguage ==
//                                language
//                            ) {
//                                8.dp
//                            } else {
//                                2.dp
//                            }
//                    )
//            ){
//
//                Row(
//                    modifier = Modifier.padding(16.dp)
//                ) {
//
//                    RadioButton(
//                        selected =
//                            selectedAILanguage == language,
//
//
//                        onClick = {
//
//                            selectAILanguage(language)
//                        }
//                    )
//
//                    Spacer(
//                        modifier = Modifier.width(12.dp)
//                    )
//
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement =
//                            Arrangement.SpaceBetween
//                    ) {
//
//                        Text(
//                            text = language.displayName
//                        )
//
//
//                        if (language == TranslationLanguage.ENGLISH) {
//
//                            Text(
//                                text =
//                                    AppStrings.installed(
//                                        selectedLanguage
//                                    )
//                            )
//
//                        }
//                        else if (
//                            TranslationDownloadManager
//                                .isInstalled(
//                                    context,
//                                    language
//                                )
//                        ) {
//
//
//                            AssistChip(
//
//                                onClick = {
//
//                                    languageToDelete =
//                                        language
//
//                                    showDeleteDialog =
//                                        true
//                                },
//
//                                label = {
//
//                                    Text(
//                                        AppStrings.delete(
//                                            selectedLanguage
//                                        )
//                                    )
//                                }
//                            )
//
//                        }
//                        else {
//
//
//
//                            AssistChip(
//
//                                onClick = {
//
//                                    languageToDownload =
//                                        language
//
//                                    showDownloadDialog =
//                                        true
//                                },
//
//                                label = {
//
//                                    Text(
//                                        AppStrings.download(
//                                            selectedLanguage
//                                        )
//                                    )
//                                }
//                            )
//                        }
//                    }
//                }
//            }
//        }
//        if (showDownloadDialog) {
//
//            AlertDialog(
//
//                onDismissRequest = {
//
//                    if (!isDownloading) {
//
//                        showDownloadDialog = false
//                    }
//                },
//
//                title = {
//
//                    Text(
//                        "Download Language Pack"
//                    )
//                },
//
//                text = {
//
//                    Column {
//
//                        Text(
//                            "Do you want to download ${languageToDownload?.displayName}?"
//                        )
//
//                        if (isDownloading) {
//
//                            Spacer(
//                                modifier = Modifier.height(16.dp)
//                            )
//
//                            CircularProgressIndicator()
//
//                            Spacer(
//                                modifier = Modifier.height(8.dp)
//                            )
//
//                            Text(
//                                "Downloading..."
//                            )
//                        }
//                    }
//                },
//
//                confirmButton = {
//
//                    if (!isDownloading) {
//
//                        TextButton(
//
//                            onClick = {
//
//                                languageToDownload?.let { language ->
//
//                                    isDownloading = true
//
//                                    CoroutineScope(
//                                        Dispatchers.Main
//                                    ).launch {
//
//                                        try {
//
//
//                                            TranslationManager.translate(
//                                                context = context,
//                                                text = "test",
//                                                targetLanguage = language
//                                            )
//
//                                            selectedAILanguage = language
//
//                                            AITranslationLanguageStorage.saveLanguage(
//                                                context,
//                                                language
//                                            )
//
//                                            refreshTrigger++
//
//                                        } finally {
//
//                                            isDownloading =
//                                                false
//
//                                            showDownloadDialog =
//                                                false
//                                        }
//                                    }
//                                }
//                            }
//                        ) {
//
//                            Text(
//                                "Continue Download"
//                            )
//                        }
//                    }
//                },
//
//                dismissButton = {
//
//                    if (!isDownloading) {
//
//                        TextButton(
//
//                            onClick = {
//
//                                showDownloadDialog =
//                                    false
//                            }
//                        ) {
//
//                            Text(
//                                "Close"
//                            )
//                        }
//                    }
//                }
//            )
//        }
//        if (showDeleteDialog) {
//
//            AlertDialog(
//
//                onDismissRequest = {
//
//                    showDeleteDialog = false
//                },
//
//                title = {
//
//                    Text(
//                        AppStrings.deleteLanguagePack(
//                            selectedLanguage
//                        )
//                    )
//                },
//
//                text = {
//
//                    Text(
//                        AppStrings.deleteLanguageDescription(
//                            selectedLanguage
//                        )
//                    )
//                },
//
//                confirmButton = {
//
//                    TextButton(
//
//                        onClick = {
//
//                            showDeleteDialog = false
//
//                            languageToDelete?.let { language ->
//
//                                CoroutineScope(
//                                    Dispatchers.Main
//                                ).launch {
//
//
//                                    TranslationManager
//                                        .deleteModel(
//                                            context,
//                                            language
//                                        )
//
//                                    if (selectedAILanguage == language) {
//
//                                        selectedAILanguage =
//                                            TranslationLanguage.ENGLISH
//
//                                        AITranslationLanguageStorage
//                                            .saveLanguage(
//                                                context,
//                                                TranslationLanguage.ENGLISH
//                                            )
//                                    }
//
//                                    refreshTrigger++
//                                }
//                            }
//                        }
//                    ) {
//
//                        Text(
//                            AppStrings.delete(
//                                selectedLanguage
//                            )
//                        )
//                    }
//                },
//
//                dismissButton = {
//
//                    TextButton(
//
//                        onClick = {
//
//                            showDeleteDialog = false
//                        }
//                    ) {
//
//                        Text(
//                            AppStrings.back(
//                                selectedLanguage
//                            )
//                        )
//                    }
//                }
//            )
//        }
////        Button(
////            onClick = onBack,
////                    modifier = Modifier.fillMaxWidth()
////        ) {
//        Button(
//
//            onClick = onBack,
//
//            modifier =
//                Modifier
//                    .fillMaxWidth()
//                    .height(56.dp),
//
//            shape =
//                RoundedCornerShape(16.dp)
//        ){
//            Text(
//                AppStrings.back(
//                    selectedLanguage
//                )
//            )
//        }
//        Spacer(
//            modifier = Modifier.height(40.dp)
//        )
//    }
//}