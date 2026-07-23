
package com.harish.floatiq.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.harish.floatiq.data.HistoryItem
import androidx.compose.material3.MaterialTheme
@Composable
fun HistoryCard(
    item: HistoryItem
) {

    Column(

        modifier = Modifier

            .fillMaxWidth()

//            .background(
//                Color(0x221E293B),
//                RoundedCornerShape(20.dp)
//            )
            .background(
                MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                RoundedCornerShape(20.dp)
            )

            .padding(16.dp)
    ) {

        Text(

            text = item.expression,

//            color = Color.White,
            color =
                MaterialTheme.colorScheme.onSurface,

            fontSize = 20.sp
        )

        Spacer(
            Modifier.height(8.dp)
        )

        Text(

            text = "= ${item.result}",

//            color = Color(0xFF00C853),
            color =
                MaterialTheme.colorScheme.tertiary,

            fontSize = 24.sp
        )
    }
}