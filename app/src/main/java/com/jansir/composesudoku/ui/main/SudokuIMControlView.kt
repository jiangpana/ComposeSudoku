package com.jansir.composesudoku.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jansir.composesudoku.ext.isPortrait
import com.jansir.composesudoku.ui.theme.AppTheme

@Composable
fun SudokuIMControlView(viewModel: SudokuViewModel) {
    val textSize = if (isPortrait()) 30.sp else 35.sp
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..10) {
            val value = if (i == 10) 0 else i
            Text(
                text = value.toString(),
                color = AppTheme.colors.sudokuview_text_color_uneditable,
                fontSize = textSize,
                modifier = Modifier
                    .clickable {
                        viewModel.dispatch(SudokuOperateAction.CHANGE_VALUE(value))
                    }
                    .padding(10.dp)
            )
        }
    }
}