package com.harish.floatiq.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.harish.floatiq.ui.ai.formula.FormulaSearchEngine
import com.harish.floatiq.ui.components.FormulaCard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.lazy.LazyRow
import com.harish.floatiq.ui.ai.formula.FormulaCategory
import com.harish.floatiq.ui.components.CategoryChip
import androidx.compose.foundation.background
import com.harish.floatiq.storage.FavoritesStorage
import com.harish.floatiq.ui.ai.LearningEngine
import com.harish.floatiq.ui.components.LearningStatsCard
import androidx.compose.foundation.clickable
//import com.harish.floatiq.ui.ads.NativeAdCard
import com.harish.floatiq.ui.components.FeatureGuideDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.ui.Alignment
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.graphics.Brush

@Composable
fun FormulaExplorerScreen(
    onBack: () -> Unit
) {

    BackHandler {
        onBack()
    }

    var query by remember {
        mutableStateOf("")
    }
    var selectedCategory by remember {
        mutableStateOf<FormulaCategory?>(null)
    }
    var showFavorites by remember {
        mutableStateOf(false)
    }
    var expandLearning by remember {

        mutableStateOf(false)
    }
    var showFormulaGuide by remember {
        mutableStateOf(false)
    }
    val context =
        LocalContext.current
    val favoriteNames =
        FavoritesStorage.getFavorites(
            context
        )

    var refreshTrigger by remember {
        mutableStateOf(0)
    }




    val topLearningTopics =
        remember(refreshTrigger) {

            LearningEngine
                .getTopLearningTopics(
                    context
                )
        }
    val allResults =
        FormulaSearchEngine.search(
            query,
            selectedCategory
        )
    val results =
        if (showFavorites) {

            allResults.filter {

                favoriteNames.contains(
                    it.name
                )
            }

        } else {

            allResults
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
            modifier =
                Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .padding(16.dp)
        ) {



            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Formula Explorer",
                        tint = Color(0xFF0EA5E9),
                        modifier = Modifier.size(40.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(12.dp)
                    )

                    Text(
                        text = "Formula Explorer",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
                            RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            showFormulaGuide = true
                        },

                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = "Formula Guide",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "Search formulas, variables, explanations and examples instantly.",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            OutlinedTextField(
                value = query,
                onValueChange = {
                    query = it
                },

                modifier = Modifier.fillMaxWidth(),

                label = {
                    Text("Search Formula")
                },

                shape = RoundedCornerShape(18.dp),

                colors = OutlinedTextFieldDefaults.colors(

                    focusedBorderColor =
                        MaterialTheme.colorScheme.primary,

                    unfocusedBorderColor =
                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.25f),

                    focusedLabelColor =
                        MaterialTheme.colorScheme.primary,

                    unfocusedLabelColor =
                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),

                    focusedTextColor =
                        MaterialTheme.colorScheme.onBackground,

                    unfocusedTextColor =
                        MaterialTheme.colorScheme.onBackground,

                    focusedContainerColor =
                        Color.White.copy(alpha = 0.10f),

                    unfocusedContainerColor =
                        Color.White.copy(alpha = 0.06f)
                )
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )
            LazyColumn(
                modifier =
                    Modifier.weight(1f)
            )
            {
                item {
                    Text(
                        text = "Categories",

                        style =
                            MaterialTheme.typography.titleMedium,

                        color =
                            MaterialTheme.colorScheme.primary
                    )

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )
                    LazyRow {
                        item {

                            CategoryChip(

                                title = "⭐ Favorites",

                                selected = showFavorites,

                                onClick = {

                                    showFavorites = true
                                    selectedCategory = null
                                }
                            )

                            Spacer(
                                modifier =
                                    Modifier.width(8.dp)
                            )
                        }
                        item {

                            CategoryChip(
                                title = "All",
                                selected =
                                    selectedCategory == null,
                                onClick = {
                                    showFavorites = false
                                    selectedCategory = null
                                }
                            )

                            Spacer(
                                modifier =
                                    Modifier.width(8.dp)
                            )
                        }

                        items(
                            FormulaCategory.entries
                        ) { category ->

                            CategoryChip(

                                title = category.name,

                                selected =
                                    selectedCategory == category,

                                onClick = {
                                    showFavorites = false
                                    selectedCategory =
                                        category
                                }
                            )

                            Spacer(
                                modifier =
                                    Modifier.width(8.dp)
                            )
                        }
                    }
                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    if (
                        topLearningTopics.isNotEmpty()
                    ) {

                        Card(

                            modifier =
                                Modifier.fillMaxWidth(),

                            colors =
                                CardDefaults.cardColors(

                                    containerColor =
                                        MaterialTheme
                                            .colorScheme
                                            .surface
                                )
                        ) {

                            Column(

                                modifier =
                                    Modifier.padding(16.dp)
                            ) {


                                Row(

                                    modifier =
                                        Modifier.fillMaxWidth(),

                                    horizontalArrangement =
                                        Arrangement.SpaceBetween
                                ) {

                                    TextButton(

                                        onClick = {

                                            expandLearning =
                                                !expandLearning
                                        }
                                    ) {

                                        Text(

                                            text =


                                                if (expandLearning)
                                                    "▼ Continue Learning (${topLearningTopics.size} topics)"
                                                else
                                                    "▶ Continue Learning (${topLearningTopics.size} topics)",

                                            color =
                                                MaterialTheme
                                                    .colorScheme
                                                    .primary
                                        )
                                    }

                                    if (expandLearning) {

                                        TextButton(

                                            onClick = {

                                                LearningEngine
                                                    .clearLearningMemory(
                                                        context
                                                    )

                                                refreshTrigger++
                                            }
                                        ) {

                                            Text(

                                                text = "Clear",

                                                color =
                                                    MaterialTheme
                                                        .colorScheme
                                                        .error
                                            )
                                        }
                                    }
                                }
                                if (expandLearning) {

                                    Spacer(
                                        modifier =
                                            Modifier.height(8.dp)
                                    )

                                    topLearningTopics.forEach {

                                        LearningStatsCard(it)

                                        Spacer(
                                            modifier =
                                                Modifier.height(8.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(
                            modifier =
                                Modifier.height(16.dp)
                        )
                    }
//                    NativeAdCard()
//
//                    Spacer(
//                        modifier = Modifier.height(16.dp)
//                    )
                    Text(
                        text = "Formulas",

                        style =
                            MaterialTheme.typography.titleMedium,

                        color =
                            MaterialTheme.colorScheme.primary
                    )

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )
                    if (results.isEmpty()) {

                        Text(

                            text =
                                if (showFavorites)
                                    "No favorites added yet"
                                else
                                    "No formulas found",

                            color =
                                MaterialTheme.colorScheme
                                    .onBackground
                                    .copy(alpha = 0.7f)
                        )
                    }
                }

                items(results) {

                    FormulaCard(it)

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )
                }
            }
        }
    }
    if (showFormulaGuide) {

        FeatureGuideDialog(

            title = "🧠 Formula Explorer Guide",

            guideText =
                """
What is this tool?

Formula Explorer helps you quickly find and learn formulas used in Mathematics, Physics, Chemistry, Statistics and Engineering.

Features

🔍 Smart Formula Search

Search formulas by:

• Formula name
• Keyword
• Variable
• Subject

Examples:

BMI
Pythagorean
Ohm
Force
Velocity
Area
Probability

────────────────────

📚 Categories

Browse formulas by subject.

Available categories include:

• Mathematics
• Geometry
• Physics
• Chemistry
• Statistics
• Engineering

────────────────────

⭐ Favorites

Save important formulas for quick access.

Favorites help you:

• Build a personal formula collection
• Quickly revisit commonly used formulas
• Access saved formulas from the Favorites filter

────────────────────

🧠 Learning Memory

Formula Explorer tracks topics you study frequently.

Learning Memory shows:

• Most viewed topics
• Learning progress
• Frequently studied categories
• Continue learning suggestions

────────────────────

📖 Formula Information

Each formula may include:

• Formula expression
• Variable definitions
• Explanation
• Practical examples
• Real-world usage

────────────────────

How to Use

1. Search for a formula using the search bar.
2. Browse formulas from categories.
3. Review formula explanations and variables.
4. Save useful formulas to Favorites.
5. Track your learning progress over time.

────────────────────

Tips

• Search using keywords or formula names.
• Use Categories to discover related formulas.
• Save frequently used formulas to Favorites.
• Check Learning Memory to continue studying previous topics.
• Use Formula Explorer together with OCR Scanner for faster learning.
"""
                    .trimIndent(),

            onDismiss = {
                showFormulaGuide = false
            }
        )
    }
}