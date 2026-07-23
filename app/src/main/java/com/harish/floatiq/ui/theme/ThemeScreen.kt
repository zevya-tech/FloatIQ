package com.harish.floatiq.ui.theme

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.platform.LocalContext
import com.harish.floatiq.storage.ThemeStorage
@Composable
fun ThemeScreen(
    onBack: () -> Unit
) {

    BackHandler {
        onBack()
    }
    val context = LocalContext.current
    val currentTheme =
        ThemeManager.currentTheme.value
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
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(20.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Spacer(
                modifier = Modifier.height(26.dp)
            )

            Text(
                text = "Themes",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(30.dp)
            )

            ThemeCard(
                title = "Neon Blue",
                color = Color(0xFF2563EB),
                isSelected = currentTheme == ThemeType.NEON_BLUE
            ) {
//                ThemeManager.currentTheme.value =
//                    ThemeType.NEON_BLUE


                ThemeManager.currentTheme.value =
                    ThemeType.NEON_BLUE

                ThemeStorage.saveTheme(
                    context,
                    ThemeType.NEON_BLUE
                )            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            ThemeCard(
                title = "Emerald Green",
                color = Color(0xFF059669),
                isSelected = currentTheme == ThemeType.EMERALD_GREEN
            ) {
//                ThemeManager.currentTheme.value =
//                    ThemeType.EMERALD_GREEN
                ThemeManager.currentTheme.value =
                    ThemeType.EMERALD_GREEN

                ThemeStorage.saveTheme(
                    context,
                    ThemeType.EMERALD_GREEN
                )

            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            ThemeCard(
                title = "Sunset Orange",
                color = Color(0xFFEA580C),
                isSelected = currentTheme == ThemeType.SUNSET_ORANGE
            ) {
//                ThemeManager.currentTheme.value =
//                    ThemeType.SUNSET_ORANGE

                ThemeManager.currentTheme.value =
                    ThemeType.SUNSET_ORANGE

                ThemeStorage.saveTheme(
                    context,
                    ThemeType.SUNSET_ORANGE
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            ThemeCard(
                title = "Midnight Purple",
                color = Color(0xFF7C3AED),
                isSelected = currentTheme == ThemeType.MIDNIGHT_PURPLE
            ) {
//                ThemeManager.currentTheme.value =
//                    ThemeType.MIDNIGHT_PURPLE
                ThemeManager.currentTheme.value =
                    ThemeType.MIDNIGHT_PURPLE

                ThemeStorage.saveTheme(
                    context,
                    ThemeType.MIDNIGHT_PURPLE
                )

            }
            Spacer(
                modifier = Modifier.height(16.dp)
            )


            ThemeCard(
                title = "Golden Red",
                color = Color(0xFFEC1C24),
                isSelected = currentTheme == ThemeType.RCB_CHAMPIONS
            ) {

//                ThemeManager.currentTheme.value =
//                    ThemeType.RCB_CHAMPIONS
                ThemeManager.currentTheme.value =
                    ThemeType.RCB_CHAMPIONS

                ThemeStorage.saveTheme(
                    context,
                    ThemeType.RCB_CHAMPIONS
                )
            }
            Spacer(
                modifier = Modifier.height(16.dp)
            )
//            ThemeCard(
//                title = "One Piece",
//                color = Color(0xFFD32F2F),
//                isSelected = currentTheme == ThemeType.ONE_PIECE
//            ) {
//
//                ThemeManager.currentTheme.value =
//                    ThemeType.ONE_PIECE
//            }
        }
    }
}

@Composable
private fun ThemeCard(
    title: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .border(
                width = if (isSelected) 4.dp else 0.dp,
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable {
                onClick()
            },


        shape = RoundedCornerShape(24.dp),

        colors = CardDefaults.cardColors(
            containerColor = color
        )
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),

            contentAlignment = Alignment.Center
        ) {

            Text(
                text = title,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}