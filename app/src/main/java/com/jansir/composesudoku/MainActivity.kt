package com.jansir.composesudoku

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jansir.composesudoku.ui.sudoku.SudokuGameView
import com.jansir.composesudoku.ui.sudoku.SudokuGameViewModel
import com.jansir.composesudoku.ui.sudoku.SudokuOperateAction
import com.jansir.composesudoku.ui.theme.AppTheme
import com.jansir.composesudoku.ui.theme.ComposeSudokuTheme
import com.jansir.composesudoku.ui.theme.appThemeState
import java.util.*


class MainActivity : ComponentActivity() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    var time =0L

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
                        SudokuGameViewModel()
                    }
                    Text(
                        text = "Sudoku",
                        color = AppTheme.colors.sudokuview_text_color_uneditable,
                        fontSize = 50.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                    var timeStr by remember{
                        mutableStateOf("00:00:00")
                    }
                    val timer = remember{
                        Timer()
                    }
                    LaunchedEffect(key1 = "", block = {
                        println("LaunchedEffect")
                        timer.schedule(object : TimerTask() {
                            override fun run() {
                                println("thread name = ${Thread.currentThread().name}")
                                time+=1000
                                timeStr = time.millis2time()

                            }
                        },0,1000)
                    })
                    Text(text = timeStr)
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

fun Long.millis2time():String{
    val totalSeconds: Long = this / 1000
    val seconds = totalSeconds % 60
    val minutes = totalSeconds / 60 % 60
    val hours = totalSeconds / 3600
    return Formatter().format("%02d:%02d:%02d", hours, minutes, seconds).toString()

}