package com.jansir.composesudoku.game

import kotlin.properties.Delegates

data class Cell(
    val rowIndex: Int = -1,
    val colIndex: Int = -1,
    var value: Int = -1,
    val ansValue :Int=-1,
    val isEditable: Boolean = true,
    var sector: CellGroup? = null,
    var row: CellGroup? = null,
    var col: CellGroup? = null,
){
    fun isValid() =value ==ansValue
}