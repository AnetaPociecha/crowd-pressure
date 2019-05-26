package simulation

import simulation.hexgrid.{HexGrid, HexXY}

import scala.collection.mutable
import simulation.Config._

case class MapModel(map: Map = Map(), margin: Int = 0, cellSize: Int = cellSize) {

  val graph: mutable.Map[(Long, Long), Boolean] =  mutable.Map()
  val hexGrid: HexGrid = HexGrid(cellSize)

  def initGraph(): Unit = {
    val startingRowCol = hexGrid.convertXYToRowCol(-margin, -margin)
    var row: Long = startingRowCol.row
    var col: Long = startingRowCol.col

    while(isRowInRange(row, col)) {
      while (isColInRange(row,col)) {
        graph += ((row, col) -> isObstacle(row,col))
        col += 1
      }
      col = startingRowCol.col
      row +=1
    }
  }

  private def isColInRange(row: Long, col: Long): Boolean = {
    val xy: HexXY = hexGrid.convertRowColToHexXY(row, col)
    xy.x >= -margin && xy.x <= map.width + margin
  }

  private def isRowInRange(row: Long, col: Long): Boolean = {
    val xy: HexXY = hexGrid.convertRowColToHexXY(row, col)
    xy.y >= -margin && xy.y <= map.height + margin
  }

  private  def isObstacle(row: Long, col: Long): Boolean = {
    val xy = hexGrid.convertRowColToHexXY(row, col)
    val shift = cellSize / 4
    (
      map.isObstacle(xy.x.toInt, xy.y.toInt)
      || map.isObstacle((xy.x + shift).toInt, xy.y.toInt)
      || map.isObstacle((xy.x - shift).toInt, xy.y.toInt)
      || map.isObstacle(xy.x.toInt, (xy.y + shift).toInt)
      || map.isObstacle(xy.x.toInt, (xy.y - shift).toInt)

      || map.isObstacle((xy.x + shift).toInt, (xy.y + shift).toInt)
      || map.isObstacle((xy.x + shift).toInt, (xy.y - shift).toInt)
      || map.isObstacle((xy.x - shift).toInt, (xy.y + shift).toInt)
      || map.isObstacle((xy.x - shift).toInt, (xy.y - shift).toInt)
      )
  }

}
