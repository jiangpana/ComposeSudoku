package com.jansir.composesudoku.game

 class CellGroup(){
     private val mCells = arrayOfNulls<Cell>(9)

     private var mPos = 0

     fun addCell(cell: Cell) {
         if (mPos>8)return
         mCells[mPos++] = cell
     }
     fun addCells(cells: Array<Cell>) {
        for (i in 0..9){
            mCells[i] = cells[i]
        }

     }
     operator fun get(index:Int):Cell{
       return  mCells[index]!!
     }
}
