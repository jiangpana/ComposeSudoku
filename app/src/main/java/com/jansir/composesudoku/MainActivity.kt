package com.jansir.composesudoku

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jansir.composesudoku.ui.sudoku.MainViewModel
import com.jansir.composesudoku.ui.sudoku.SudokuGameView
import com.jansir.composesudoku.ui.sudoku.SudokuOperateAction
import com.jansir.composesudoku.ui.theme.AppTheme
import com.jansir.composesudoku.ui.theme.ComposeSudokuTheme
import com.jansir.composesudoku.ui.theme.appThemeState


class MainActivity : ComponentActivity() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = applicationContext

        setContent {
            ComposeSudokuTheme {
                // A surface container using the 'background' color from the theme
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val viewModel = remember {
                        MainViewModel()
                    }
                    Text(
                        text = "Sudoku",
                        color = AppTheme.colors.sudokuview_text_color_uneditable,
                        fontSize = 50.sp,
                        modifier = Modifier.padding(10.dp)
                    )

                    SudokuGameView(viewModel = viewModel)

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        for (i in 1..9) {
                            Text(
                                text = i.toString(),
                                color = AppTheme.colors.sudokuview_text_color_uneditable,
                                fontSize = 35.sp,
                                modifier = Modifier
                                    .clickable {
                                        viewModel.dispatch(SudokuOperateAction.CHANGE_VALUE(i))
                                    }
                                    .padding(10.dp)
                            )
                        }
                        Text(
                            text = "0",
                            color = AppTheme.colors.sudokuview_text_color_uneditable,
                            fontSize = 35.sp,
                            modifier = Modifier
                                .clickable {
                                    viewModel.dispatch(SudokuOperateAction.CHANGE_VALUE(0))
                                }
                                .padding(10.dp)
                        )

                    }

                    Button(onClick = {
                        if (appThemeState.value == AppTheme.Theme.Dark) {
                            appThemeState.value = AppTheme.Theme.Blue
                        } else {
                            appThemeState.value = AppTheme.Theme.Dark
                        }
                    }) {
                        Text(text = "切换主题")
                    }
                }
            }
        }
    }
}
