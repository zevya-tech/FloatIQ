package com.harish.floatiq.ui.components

import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CategoryChip(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(title)
        }
    )
}