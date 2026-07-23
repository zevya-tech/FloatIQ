package com.harish.floatiq.ui.scientific

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScientificCalculatorButton(
    text: String,
    buttonColor: Color = Color(0xFF1E293B),
//    buttonSize: Dp = 72.dp,
    buttonSize: Dp = 56.dp,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
//            .padding(6.dp)
            .padding(3.dp)
//            .size(72.dp)
            .size(buttonSize)
            .background(
                buttonColor,
//                RoundedCornerShape(18.dp)
                RoundedCornerShape(14.dp)
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = text,
            color = Color.White,
//            fontSize = 22.sp,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}