package com.harish.floatiq.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.harish.floatiq.ui.ai.formula.FormulaInfo
import androidx.compose.ui.platform.LocalContext
import com.harish.floatiq.storage.FavoritesStorage
import androidx.compose.runtime.*
import com.harish.floatiq.ui.ai.LearningEngine

@Composable
fun FormulaCard(
    formula: FormulaInfo
) {


    val context =
        LocalContext.current
    LaunchedEffect(Unit) {

        LearningEngine
            .recordFormulaView(
                context,
                formula.name
            )
    }
    var isFavorite by remember {

        mutableStateOf(
            FavoritesStorage.isFavorite(
                context,
                formula.name
            )
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),

        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surface
        )
    ){
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

                Text(
                    text = formula.name,
                    style =
                        MaterialTheme.typography.titleLarge,
                    color =
                        MaterialTheme.colorScheme.onSurface
                )

                TextButton(

                    onClick = {

                        FavoritesStorage
                            .toggleFavorite(
                                context,
                                formula.name
                            )

                        isFavorite =
                            !isFavorite
                    }
                ) {

//                    Text(
//                        if (isFavorite)
//                            "⭐"
//                        else
//                            "☆"
//                    )
                    Text(
                        text =
                            if (isFavorite)
                                "⭐"
                            else
                                "☆",

                        color =
                            if (isFavorite)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.outline
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text =
                    "Category: ${formula.category}",
                color =
                    MaterialTheme.colorScheme.primary

            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text =
                    "Formula:\n${formula.formula}",
                color =
                    MaterialTheme.colorScheme.onSurface
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text =
                    "Explanation:\n${formula.explanation}",
                color =
                    MaterialTheme.colorScheme.onSurface
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text =
                    "Variables:\n${formula.variables}",
                color =
                    MaterialTheme.colorScheme.onSurface
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text =
                    "Example:\n${formula.example}",
                color =
                    MaterialTheme.colorScheme.onSurface
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text =
                    "Applications:\n${formula.applications}",
                color =
                    MaterialTheme.colorScheme.tertiary
            )
        }
    }
}