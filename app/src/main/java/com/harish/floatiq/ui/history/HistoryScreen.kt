package com.harish.floatiq.ui.history

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
//import com.harish.floatiq.storage.HistoryStorage
import androidx.compose.foundation.clickable
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.ui.Alignment

import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Brush
import com.harish.floatiq.ui.components.FeatureGuideDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.collectAsState
import com.harish.floatiq.data.HistoryDatabase
import com.harish.floatiq.data.HistoryRepository
import kotlinx.coroutines.launch
@Composable
fun HistoryScreen(

    onBack: () -> Unit

) {
    BackHandler {

        onBack()
    }

    val context =
        LocalContext.current


//    var history by remember {
//
//        mutableStateOf(
//            HistoryStorage.getHistory(context)
//        )
//    }
    val repository = remember {
        HistoryRepository(
            HistoryDatabase.getDatabase(context).historyDao()
        )
    }

    val history by repository
        .allHistory
        .collectAsState(initial = emptyList())

    val scope = rememberCoroutineScope()
    var showHistoryGuide by remember {
        mutableStateOf(false)
    }
    Column(

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

//            .statusBarsPadding()
            .windowInsetsPadding(WindowInsets.safeDrawing)

            .padding(
                horizontal = 20.dp,
                vertical = 12.dp
            )
    )
    {



        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {

            Column {

                Text(
                    text = "History",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 30.sp
                )

                Spacer(
                    Modifier.height(4.dp)
                )

                Text(
                    text = "Your recent calculations",
                    color =
                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontSize = 14.sp
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
                        showHistoryGuide = true
                    },

                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.HelpOutline,
                    contentDescription = "History Guide",
                    tint = Color.White
                )
            }
        }
        Spacer(
            Modifier.height(24.dp)
        )
        Text(

            text = "🗑 Clear All History",


            color = MaterialTheme.colorScheme.error,

            fontSize = 16.sp,
            modifier = Modifier
                .padding(vertical = 12.dp)
                .clickable {

//                    HistoryStorage.clearHistory(
//                        context
//                    )
//
//                    history = mutableListOf()
                    scope.launch {
                        repository.clearHistory()
                    }
                }
        )
        if (history.isEmpty()) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Text(

                    text = "No History Yet",
                    color =
                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }

        }
        LazyColumn {


            items(
                history,
                key = { it.timestamp }
            ) { item ->


                val dismissState =
                    rememberSwipeToDismissBoxState(

                        confirmValueChange = {

                            it == SwipeToDismissBoxValue.EndToStart
                        }
                    )

                if (
                    dismissState.currentValue ==
                    SwipeToDismissBoxValue.EndToStart &&
                    dismissState.targetValue ==
                    SwipeToDismissBoxValue.EndToStart
                )
               {

//                    HistoryStorage.deleteHistoryItem(
//                        context,
//                        item
//                    )
//
//                    history =
//                        HistoryStorage.getHistory(
//                            context
//                        )
                   scope.launch {
                       repository.deleteHistory(item)
                   }
                }


                SwipeToDismissBox(

                    state = dismissState,
                    enableDismissFromStartToEnd = false,

                    enableDismissFromEndToStart = true,

                    backgroundContent = {

                        Box(

                            modifier = Modifier.fillMaxSize(),

                            contentAlignment = Alignment.CenterEnd

                        ) {

                            if (
                                dismissState.dismissDirection ==
                                SwipeToDismissBoxValue.EndToStart
                            ) {

                                Row(

                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            MaterialTheme.colorScheme.error
                                        ),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically

                                ) {

                                    Icon(

                                        imageVector = Icons.Default.Delete,

                                        contentDescription = "Delete",

                                        tint = Color.White,

                                        modifier = Modifier
                                            .padding(end = 24.dp)
                                    )
                                }
                            }
                        }
                    },

                    content = {

                        HistoryCard(item)
                    }
                )

                Spacer(
                    Modifier.height(12.dp)
                )
            }
        }
    }
    if (showHistoryGuide) {

        FeatureGuideDialog(

            title = "🕘 History Guide",

            guideText =
                """
• Automatically stores calculations
• View previous results
• Swipe left to delete entries
• Clear all saved history
• History remains after app restart

How to Use

1. Perform calculations
2. Open History
3. Review previous calculations
4. Swipe left to delete individual items
5. Use Clear All History to remove everything
""".trimIndent(),

            onDismiss = {
                showHistoryGuide = false
            }
        )
    }
}