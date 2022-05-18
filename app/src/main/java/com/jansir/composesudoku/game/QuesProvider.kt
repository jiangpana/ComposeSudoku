package com.jansir.composesudoku.game

import com.jansir.composesudoku.data.Constant

object QuesProvider {

    fun getQuesArray2d(ques: String = Constant.ques): Array<Array<Cell>> {
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
                Cell(rowIndex, colIndex, ques[index++].toString().toInt()).apply {
                    row = rows[rowIndex]!!
                    col = cols[colIndex]!!
                    sector = sectors[((colIndex / 3) * 3) + (rowIndex / 3)]!!
                    row.addCell(this)
                    col.addCell(this)
                    sector.addCell(this)
                }
            }
        }
/*        array2d.forEach {
            it.forEach { cell ->
                cell.row.addCells(array2d[cell.rowIndex])
                for (i in 0..9) {
                    cell.col.addCell(array2d[cell.colIndex][i])
                }

            }
        }*/
        return array2d
    }
}