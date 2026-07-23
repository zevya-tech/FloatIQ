package com.harish.floatiq.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.harish.floatiq.data.LearningMemory

@Composable
fun LearningStatsCard(
    memory: LearningMemory
) {

    Card(

        modifier =
            Modifier.fillMaxWidth(),

        colors =
            CardDefaults.cardColors(

                containerColor =
                    MaterialTheme.colorScheme.surface
            )
    ) {

        Column(

            modifier =
                Modifier.padding(16.dp)
        ) {

            Text(

                text =
                    memory.topic,

                style =
                    MaterialTheme.typography.titleMedium,

                color =
                    MaterialTheme.colorScheme.primary
            )

            Spacer(
                modifier =
                    Modifier.height(4.dp)
            )

            Text(
                text =
                    "Viewed ${memory.viewCount} times",

                color =
                    MaterialTheme.colorScheme.onSurface
            )
        }
    }
}