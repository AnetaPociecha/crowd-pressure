package graphics.hexagon

import hexgrid.HexGrid

object Demo {
  def main(args: Array[String]): Unit = {
    val hexGrid = HexGrid(100)
    val t = hexGrid.convertXYToRowCol(110,120)
    println(t)
  }

}
