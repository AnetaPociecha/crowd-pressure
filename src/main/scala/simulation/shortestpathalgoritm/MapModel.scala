package simulation.shortestpathalgoritm

import config.Config.{CellSize, ObstacleMargin}
import simulation.hexgrid.{HexGrid, HexXY}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

case class MapModel(map: simulation.Map, margin: Int = 0, cellSize: Int = CellSize) {

  var destinations: ArrayBuffer[(Int, Int)] = ArrayBuffer()

  var graph: mutable.Map[(Long, Long), Boolean] =  mutable.Map()
  val hexGrid: HexGrid = HexGrid(cellSize)

  def initGraph(): Unit = {
    println("init map model")
    val g: mutable.Map[(Long, Long), Boolean] =  mutable.Map()
    val startingRowCol = hexGrid.convertXYToRowCol(-margin, -margin)
    var row: Long = startingRowCol.row
    var col: Long = startingRowCol.col

    while(isRowInRange(row, col)) {
      while (isColInRange(row,col)) {
        g += ((row, col) -> isObstacle(row,col))
        col += 1
      }
      col = startingRowCol.col
      row +=1
    }
    graph = g
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

  def isObstacle(row: Long, col: Long): Boolean = {
    val xy = hexGrid.convertRowColToHexXY(row, col)
//    val shift = cellSize / 3
    val shift = cellSize / 4 + ObstacleMargin
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
