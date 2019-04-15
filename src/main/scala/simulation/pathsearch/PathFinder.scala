package simulation.pathsearch

import hexgrid.{HexGrid, HexRowCol}
import simulation.{Vector2D, XY}
import simulation.view.View
import simulation.Config._

class PathFinder(val view: View,
                 val goal: XY,
                 val algorithm: Algorithm = new Dijkstra()
                 ) {

  val hexGrid: HexGrid = HexGrid(hexGridCellSize)
  val rowColGoal: HexRowCol = hexGrid.convertXYToRowCol(goal.x, goal.y)

  val desiredDirections: Map[HexRowCol, Vector2D] = algorithm.desiredDirections(view, rowColGoal)

  def isGoalReached(xy: XY): Boolean = {
    hexGrid.convertXYToRowCol(xy.x, xy.y) == rowColGoal
  }

  def nextDirection(xy: XY): Vector2D = {
    val rowCol: HexRowCol = hexGrid.convertXYToRowCol(xy.x, xy.y)
    desiredDirections(rowCol)
  }
}
