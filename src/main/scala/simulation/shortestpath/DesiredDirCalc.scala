package simulation.shortestpath

import simulation.shortestpath.hexgrid.HexGrid
import simulation.utils.Vector2D

import scala.collection.mutable
import simulation.Config._

case class DesiredDirCalc(goal: Vector2D, cellSize: Int = hexGridCellSize) {

  val nav = Navigation(cellSize)
  val grid: HexGrid = HexGrid(cellSize)

  val desiredDirectionMap: mutable.Map[(Int, Int), Vector2D] = {
    val rowCol = grid.convertXYToRowCol(goal.x, goal.y)
    nav.desiredDirections((rowCol.col.toInt,rowCol.row.toInt))
  }


  def desiredDirection(position: Vector2D): Vector2D = {
    val rowCol = grid.convertXYToRowCol(position.x, position.y)
    desiredDirectionMap((rowCol.col.toInt, rowCol.row.toInt))
  }

}
