package com.jansir.composesudoku.ui.sudoku

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
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.jansir.composesudoku.game.Cell
import com.jansir.composesudoku.ui.theme.AppTheme
import com.jansir.composesudoku.ui.widget.SampleAlertDialog
import kotlinx.coroutines.flow.collect

var cellWidth = 0f
var sudoku_view_padding = 20.dp
var sudoku_text_size = 20.dp


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SudokuGameView(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    println("SudokuView  执行")
    val data = viewModel.viewStates.cells!!
    val selectCell =  viewModel.viewStates.selectCell
    LaunchedEffect(Unit) {
        viewModel.viewEvents.collect {

        }
    }
    if (viewModel.viewStates.gameSuccess){
        SampleAlertDialog(
            title = "游戏成功",
            content = "重新开始游戏?",
            onConfirmClick = {
                viewModel.dispatch(SudokuOperateAction.RE_GAME)
            },
            onDismiss = {
                viewModel.dispatch(SudokuOperateAction.DISMISS_DIALOG)
            }
        )
    }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    var width = 0f
    var height = 0f
    Box(
        modifier = modifier
            .width(screenWidth)
            .height(screenWidth)
    ) {

        val textPaint = Paint().apply {
            color = AppTheme.colors.sudokuview_text_color
        }
        val textUnEditPaint = Paint().apply {
            color = AppTheme.colors.sudokuview_text_color_uneditable
        }

        Canvas(
            modifier = Modifier
                .padding(sudoku_view_padding)
                .fillMaxSize()
                .background(AppTheme.colors.sudoku_view_bg)
                .pointerInteropFilter {
                    return@pointerInteropFilter handleMotionEvent(it) { row, col ->
                        viewModel.dispatch(SudokuOperateAction.MOVE(row, col))
                    }
                }
        ) {
            println("Canvas Draw")
            width = size.width
            height = size.width
            cellWidth = (size.width) / 9.0f
            drawHighlightColRowSec(selectCell)
            drawLine(height, width)
            drawCellValue(data, textPaint, textUnEditPaint)
        }
    }

}

private fun DrawScope.drawHighlightColRowSec(selectCell: Cell) {
    selectCell.apply {
        // 绘制高亮列
        drawRect(
            Color(0xffD8EBFF),
            topLeft = Offset(selectCell.colIndex * cellWidth, 0f),
            size = Size(cellWidth, cellWidth * 9)
        )
        //绘制高亮行
        drawRect(
            Color(0xffD8EBFF),
            topLeft = Offset(0f, selectCell.rowIndex * cellWidth),
            size = Size(cellWidth * 9, cellWidth)
        )
        //绘制高亮九宫格
        drawRect(
            Color(0xffD8EBFF),
            topLeft = Offset(
                selectCell.sector!!.get(0).colIndex * cellWidth,
                selectCell.sector!!.get(0).rowIndex * cellWidth
            ),
            size = Size(cellWidth * 3, cellWidth * 3)
        )
        //绘制高亮单元格
        drawRect(
            Color(0xff88C2FF),
            topLeft = Offset(selectCell.colIndex * cellWidth, selectCell.rowIndex * cellWidth),
            size = Size(cellWidth, cellWidth)
        )
    }
}

private fun DrawScope.drawCellValue(
    datas: Array<Array<Cell>>,
    textPaint: Paint,
    textUnEditPaint: Paint
) {
    datas.forEachIndexed { rowIndex, ints ->
        ints.forEachIndexed { colIndex, cell ->
            drawIntoCanvas {
                //将 Jetpack Compose 环境的 Paint 对象转换为原生的 Paint 对象
                val paint = if (cell.isEditable) textPaint else textUnEditPaint
                val nativePaint = paint.asFrameworkPaint().apply {
                    isAntiAlias = true
                    isDither = true
                    typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                    textAlign = android.graphics.Paint.Align.CENTER
                    textSize = sudoku_text_size.toPx()
                    color = if (cell.isEditable) textPaint.color.toArgb() else textUnEditPaint.color.toArgb()
                }

                val fontMetrics = nativePaint.fontMetrics
                val top = fontMetrics.top
                val bottom = fontMetrics.bottom
                val offset = -top / 2 - bottom / 2
                val centerX = colIndex * cellWidth + cellWidth / 2f
                val centerY = rowIndex * cellWidth + cellWidth / 2f + offset
                //拿到原生的 Canvas 对象
                val nativeCanvas = it.nativeCanvas
                if (cell.value != 0) {
                    nativeCanvas.drawText(
                        cell.value.toString(),
                        centerX, centerY, nativePaint
                    )
                }

            }
        }

    }
}

private fun DrawScope.drawLine(
    height: Float,
    width: Float
) {
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
}


fun handleMotionEvent(it: MotionEvent, onTouch: (Int, Int) -> Unit): Boolean {
    val row = (it.y / cellWidth).toInt()
    val col = (it.x / cellWidth).toInt()
    onTouch(row, col)
    when (it.action) {
        MotionEvent.ACTION_DOWN -> {
            println("SudokuView ACTION_DOWN")
        }
        MotionEvent.ACTION_MOVE -> {
            println("SudokuView ACTION_MOVE")
        }
        MotionEvent.ACTION_UP -> {
            println("SudokuView ACTION_UP")
        }
        else -> return false
    }
    return true
}

