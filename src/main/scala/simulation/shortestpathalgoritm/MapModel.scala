package simulation.shortestpathalgoritm

import simulation.Config.cellSize
import simulation.hexgrid.{HexGrid, HexXY}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

case class MapModel(map: simulation.Map, margin: Int = 0, cellSize: Int = cellSize) {

  val destinations: ArrayBuffer[(Int, Int)] = ArrayBuffer()

  val graph: mutable.Map[(Long, Long), Boolean] =  mutable.Map()
  val hexGrid: HexGrid = HexGrid(cellSize)

  def initGraph(): Unit = {
    println("init map model")
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
    println("map model: "+graph)
  }

  private def isColInRange(row: Long, col: Long): Boolean = {
    val xy: HexXY = hexGrid.convertRowColToHexXY(row, col)
    xy.x >= margin && xy.x < map.width - margin
  }

  private def isRowInRange(row: Long, col: Long): Boolean = {
    val xy: HexXY = hexGrid.convertRowColToHexXY(row, col)
    xy.y >= margin && xy.y < map.height - margin
  }

  private  def isObstacle(row: Long, col: Long): Boolean = {
    val xy = hexGrid.convertRowColToHexXY(row, col)
//    val shift = cellSize / 4
//    (
      map.isObstacle(xy.x.toInt, xy.y.toInt)
//      || map.isObstacle((xy.x + shift).toInt, xy.y.toInt)
//      || map.isObstacle((xy.x - shift).toInt, xy.y.toInt)
//      || map.isObstacle(xy.x.toInt, (xy.y + shift).toInt)
//      || map.isObstacle(xy.x.toInt, (xy.y - shift).toInt)
//
//      || map.isObstacle((xy.x + shift).toInt, (xy.y + shift).toInt)
//      || map.isObstacle((xy.x + shift).toInt, (xy.y - shift).toInt)
//      || map.isObstacle((xy.x - shift).toInt, (xy.y + shift).toInt)
//      || map.isObstacle((xy.x - shift).toInt, (xy.y - shift).toInt)
//      )
  }

}
