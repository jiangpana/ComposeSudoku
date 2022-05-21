package com.jansir.composesudoku.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jansir.composesudoku.game.Cell
import com.jansir.composesudoku.game.QuesProvider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SudokuViewModel : ViewModel() {
    private val _timerState = MutableStateFlow(
        TimerData()
    )
    val timerState: StateFlow<TimerData> = _timerState
    var viewStates by mutableStateOf(SudokuViewState())

    private val _viewEvents = Channel<SudokuOperateAction>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()


    init {
        initTimer()
    }

    private fun initTimer() {
        viewModelScope.launch {
            while (true) {
                val next = timerState.value.millis.plus(1000)
                _timerState.emit(timerState.value.copy(millis = next))
                delay(1000L)
            }
        }
    }


    fun dispatch(action: SudokuOperateAction) {
        when (action) {
            is SudokuOperateAction.MOVE -> {
                try {
                    viewStates = viewStates.copy(selectCell = viewStates.cells!![action.row][action.col])
                } catch (e: Exception) {
                }
            }
            is SudokuOperateAction.CHANGE_VALUE -> {
                //text click 会触发 SudokuView Canvas Draw
                // 所以不用 viewStates = viewStates.copy(selectCell = copyCell) 再去触发一次了
                if (!viewStates.selectCell.isEditable) return
                if (viewStates.selectCell.value == action.value){
                    action.value = 0
                }
                val selectCell =viewStates.selectCell
                selectCell.value =action.value
                viewStates.cells!![selectCell.rowIndex][selectCell.colIndex] .value=  action.value
                if (selectCell.value!=0){
                    viewModelScope.launch {
                        _viewEvents.send(SudokuOperateAction.DO_ANIMATION)
                    }
                }
                checkSuccess()
            }
            is SudokuOperateAction.DISMISS_DIALOG -> {
                viewStates = viewStates.copy(gameSuccess = false)
            }
            is SudokuOperateAction.RE_GAME -> {
                val cells = viewStates.cells!!
                cells.forEach {
                    it.forEach {
                        if (it.isEditable) {
                            it.value = 0
                        }
                    }
                }
                viewStates.selectCell = viewStates.cells!![0][0]
                viewStates = viewStates.copy(cells = cells)
            }
        }

    }

    private fun checkSuccess() {
        var isSuc = true
        viewStates.cells!!.forEach {
            it.forEach {
                if (!it.isValid()) {
                    isSuc = false
                }
            }
        }
        if (isSuc) {
            viewStates = viewStates.copy(gameSuccess = isSuc)
            viewModelScope.launch {
                _viewEvents.send(SudokuOperateAction.SUC)
            }

        }
    }
}

data class SudokuViewState(
    var gameSuccess: Boolean = false,
    val cells: Array<Array<Cell>>? = QuesProvider.getQuesArray2d(),
    var selectCell: Cell = cells!![0][0],
) {

    override fun equals(other: Any?): Boolean {
        println("SudokuViewState equals 调用")
        //  viewStates = viewStates.copy(cells = cells)
        //  return false 会刷新
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SudokuViewState

        if (gameSuccess != other.gameSuccess) return false
        if (!cells.contentDeepEquals(other.cells)) return false
        if (selectCell != other.selectCell) return false

        return true
    }

    override fun hashCode(): Int {
        var result = gameSuccess.hashCode()
        result = 31 * result + cells.contentDeepHashCode()
        result = 31 * result + selectCell.hashCode()
        return result
    }
}

sealed class SudokuOperateAction() {
    class CHANGE_VALUE(var value: Int) : SudokuOperateAction()
    class MOVE(val row: Int, val col: Int) : SudokuOperateAction()
    object SUC : SudokuOperateAction()
    object DISMISS_DIALOG : SudokuOperateAction()
    object RE_GAME : SudokuOperateAction()
    object DO_ANIMATION : SudokuOperateAction()
}