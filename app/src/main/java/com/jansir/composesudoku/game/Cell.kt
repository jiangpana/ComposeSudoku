package com.jansir.composesudoku.game

import kotlin.properties.Delegates

data class Cell(val rowIndex: Int = -1, val colIndex: Int = -1, val value: Int = -1) {
    var sector by Delegates.notNull<CellGroup>()
    var row
            by Delegates.notNull<CellGroup>()
    var col
            by Delegates.notNull<CellGroup>()


}
