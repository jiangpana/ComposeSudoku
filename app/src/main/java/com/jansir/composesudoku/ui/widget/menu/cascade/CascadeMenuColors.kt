package com.jansir.composesudoku.ui.widget.menu.cascade

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class CascadeMenuColors(
    val backgroundColor: Color,
    val contentColor: Color,
)

@Composable
fun cascadeMenuColors(
    backgroundColor: Color = Color.White,
    contentColor: Color = Color.Black
): CascadeMenuColors {
    return CascadeMenuColors(backgroundColor, contentColor)
}