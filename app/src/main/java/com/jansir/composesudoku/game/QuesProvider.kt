package com.jansir.composesudoku.game

import com.jansir.composesudoku.data.Constant

object QuesProvider {

    fun getQuesArray2d(ques: String = Constant.ques): Array<Array<Cell>> {
        val ans = Constant.answ
        var index = 0
        val sectors =
            arrayOfNulls<CellGroup>(9)
        val rows =
            arrayOfNulls<CellGroup>(9)
        val cols =
            arrayOfNulls<CellGroup>(9)
        for (i in 0 until 9) {
            sectors[i] = CellGroup()
            rows[i] = CellGroup()
            cols[i] = CellGroup()
        }
        val array2d = Array(9) { rowIndex ->
            Array(9) { colIndex ->
                val cellVal = ques[index].toString().toInt()
                val ansVal = ans[index++].toString().toInt()
                Cell(
                    rowIndex = rowIndex,
                    colIndex = colIndex,
                    value = cellVal,
                    ansValue = ansVal,
                    isEditable = cellVal == 0
                ).apply {
                    row = rows[rowIndex]!!
                    col = cols[colIndex]!!
                    sector = sectors[((colIndex / 3) * 3) + (rowIndex / 3)]!!
                    row?.addCell(this)
                    col?.addCell(this)
                    sector?.addCell(this)
                }
            }
        }
        return array2d
    }
}