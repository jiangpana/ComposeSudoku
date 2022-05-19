package com.jansir.composesudoku.ui.sudoku

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jansir.composesudoku.game.Cell
import com.jansir.composesudoku.game.QuesProvider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    var viewStates by mutableStateOf(SudokuViewState())

    private val _viewEvents = Channel<SudokuOperateAction>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()

    fun dispatch(action: SudokuOperateAction) {
        when (action) {
            is SudokuOperateAction.MOVE -> {
                try {
                    viewStates = viewStates.copy(selectCell = viewStates.cells!![action.row][action.col])
                } catch (e: Exception) {
                }
            }
            is SudokuOperateAction.CHANGE_VALUE -> {
                if (!viewStates.selectCell.isEditable) return
                if (viewStates.selectCell.value == action.value){
                    action.value = 0
                }
                val copyCell = viewStates.selectCell.copy(value = action.value)
                viewStates.cells!![viewStates.selectCell.rowIndex][viewStates.selectCell.colIndex] = copyCell
//                viewStates = viewStates.copy(selectCell = copyCell)
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
                val selectCell1 = cells[0][0]
                viewStates = viewStates.copy(cells = cells, selectCell = selectCell1)
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
            viewModelScope.launch {
                _viewEvents.send(SudokuOperateAction.SUC)
            }
            viewStates = viewStates.copy(gameSuccess = true)
        }
    }
}

data class SudokuViewState(
    val gameSuccess: Boolean = false,
    val cells: Array<Array<Cell>>? = QuesProvider.getQuesArray2d(),
    val selectCell: Cell = cells!![0][0],
) {

    override fun equals(other: Any?): Boolean {
        println("SudokuViewState equals 调用")
        //set 时候   return false 会刷新
        //set get   value 有改变会刷新
   /*     if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SudokuViewState

        if (gameSuccess != other.gameSuccess) return false
        if (!cells.contentDeepEquals(other.cells)) return false
        if (selectCell != other.selectCell) return false*/

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
}