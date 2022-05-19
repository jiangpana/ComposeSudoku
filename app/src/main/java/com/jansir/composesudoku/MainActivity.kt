package com.jansir.composesudoku

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jansir.composesudoku.ui.sudoku.MainViewModel
import com.jansir.composesudoku.ui.sudoku.SudokuOperateAction
import com.jansir.composesudoku.ui.sudoku.SudokuGameView
import com.jansir.composesudoku.ui.theme.AppTheme
import com.jansir.composesudoku.ui.theme.ComposeSudokuTheme
import com.jansir.composesudoku.ui.theme.appThemeState
import com.jansir.composesudoku.ui.widget.StaggeredGrid

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
                    modifier = Modifier.fillMaxSize(),
                ) {

                    val viewModel = MainViewModel()
                    SudokuGameView(viewModel = viewModel)
                    StaggeredGrid() {
                        for (i in 1..9) {
                            Button(onClick = {
                                viewModel.dispatch(SudokuOperateAction.CHANGE(i))
                            }) {
                                Text(text = "$i")
                            }
                        }
                        Button(onClick = {
                            viewModel.dispatch(SudokuOperateAction.CHANGE(0))
                        }) {
                            Text(text = "清除")
                        }
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
