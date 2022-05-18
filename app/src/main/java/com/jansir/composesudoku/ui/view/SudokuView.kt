package com.jansir.composesudoku.ui.view

import android.graphics.Typeface
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jansir.composesudoku.game.QuesProvider


@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun SudokuView() {
    val datas = remember {
        QuesProvider.getQuesArray2d()
    }
    var selectCell by remember {
        mutableStateOf(datas[0][0])
    }
    val padding =20.dp
    var cellWidth =0f
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    Box(
        modifier = Modifier
            .width(screenWidth)
            .height(screenWidth)
    ) {
        Canvas(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White)
                .pointerInteropFilter {
                    val x = it.x
                    val y = it.y
                    val lx = x - padding.value
                    val ly = y - padding.value
                    val row = (ly / cellWidth).toInt()
                    val col = (lx / cellWidth).toInt()
                    try {
                        selectCell =  datas[row][col]
                    } catch (e: Exception) {
                    }
                    when (it.action) {
                        MotionEvent.ACTION_DOWN -> {
                            println("SudokuView ACTION_DOWN")
                        }
                        MotionEvent.ACTION_MOVE -> {
                            println("SudokuView ACTION_MOVE")
                            if (it.x > 200.dp.value || it.y > 200.dp.value) {
                                return@pointerInteropFilter false
                            }
                        }
                        MotionEvent.ACTION_UP -> {
                            println("SudokuView ACTION_UP")
                        }
                        else -> return@pointerInteropFilter false
                    }
                    true
                }
        ) {
            val width = size.width
            val height = size.width
            cellWidth = (size.width) / 9.0f

            //draw select cell
            selectCell.apply {
                // 绘制高亮列
                drawRect(Color(0xffD8EBFF), topLeft = Offset(selectCell.colIndex*cellWidth,0f) ,size=Size(cellWidth,cellWidth*9)
                )
                //绘制高亮行
                drawRect(Color(0xffD8EBFF), topLeft = Offset(0f,selectCell.rowIndex*cellWidth) ,size=Size(cellWidth*9,cellWidth)
                )
                //绘制高亮九宫格
                drawRect(Color(0xffD8EBFF), topLeft = Offset(selectCell.sector.get(0).colIndex*cellWidth,selectCell.sector.get(0).rowIndex*cellWidth) ,size=Size(cellWidth*3,cellWidth*3)
                )
                //绘制高亮单元格
                drawRect(Color(0xff88C2FF), topLeft = Offset(selectCell.colIndex*cellWidth,selectCell.rowIndex*cellWidth) ,size=Size(cellWidth,cellWidth)
                )
            }

            //画9x9垂直线
            for (c in 0..9) {
                val start = Offset(c * cellWidth, 0f)
                val end = Offset(c * cellWidth, height)
                drawLine(Color.Black, start, end, strokeWidth = 2.dp.value)
            }

            //画9x9水平线
            for (c in 0..9) {
                val start = Offset(0f, c * cellWidth)
                val end = Offset(width, c * cellWidth)
                drawLine(Color.Black, start, end, strokeWidth = 2.dp.value)
            }
            //画3x3 垂直粗线
            for (c in 0..3) {
                val start = Offset(c * cellWidth * 3, 0f)
                val end = Offset(c * cellWidth * 3, height)
                drawLine(Color.Black, start, end, strokeWidth = 8.dp.value, cap = StrokeCap.Square)
            }

            //画3x3 水平粗线
            for (c in 0..3) {
                val start = Offset(0f, c * cellWidth * 3)
                val end = Offset(width, c * cellWidth * 3)
                drawLine(Color.Black, start, end, strokeWidth = 8.dp.value, cap = StrokeCap.Square)
            }
            //draw value
            datas.forEachIndexed { rowIndex, ints ->
                ints.forEachIndexed { colIndex, cell ->
                    drawIntoCanvas {
                        //将 Jetpack Compose 环境的 Paint 对象转换为原生的 Paint 对象
                        val textPaint = Paint().asFrameworkPaint().apply {
                            isAntiAlias = true
                            isDither = true
                            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                            textAlign = android.graphics.Paint.Align.CENTER
                        }
                        textPaint.color = android.graphics.Color.BLACK
                        textPaint.textSize = 50.dp.value
                        val fontMetrics = textPaint.fontMetrics
                        val top = fontMetrics.top
                        val bottom = fontMetrics.bottom
                        val offset = -top / 2 - bottom / 2
                        val centerX = colIndex * cellWidth + cellWidth / 2f
                        val centerY = rowIndex * cellWidth + cellWidth / 2f + offset
                        //拿到原生的 Canvas 对象
                        val nativeCanvas = it.nativeCanvas
                        if (cell.value!=0){
                            nativeCanvas.drawText(
                                cell.value.toString(),
                                centerX, centerY, textPaint
                            )
                        }

                    }
                }

            }

        }
    }


}

