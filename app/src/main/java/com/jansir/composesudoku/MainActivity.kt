package com.jansir.composesudoku

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
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


@Composable
@Preview
fun Test() {
    var enabled by remember {
        mutableStateOf(false)
    }
    val size = remember { Animatable(0f) }
    LaunchedEffect("") {
        size.animateTo(1000f, tween(
            durationMillis = 5000,
            delayMillis = 5000
        )){
        }
    }


    Canvas(Modifier.fillMaxSize()) {
        drawRect(Color.Yellow, Offset(0f, 0f), Size(size.value, size.value))
    }
/*    Box(
        Modifier
            .fillMaxSize()
            .graphicsLayer(alpha = alpha)
            .background(Color.Red)
            .clickable {
                enabled =!enabled
            }
    )
    */

}