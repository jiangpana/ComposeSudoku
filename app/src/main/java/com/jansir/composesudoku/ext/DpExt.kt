package com.jansir.composesudoku.ext

import androidx.compose.ui.unit.Dp
import com.jansir.composesudoku.MainActivity.Companion.context


fun Dp.toPx(): Float {
    val scale = context.getResources().getDisplayMetrics().density;
    return  (value * scale + 0.5f);
}