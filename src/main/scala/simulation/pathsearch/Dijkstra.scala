package simulation.pathsearch
import hexgrid.{HexGrid, HexRowCol}
import simulation.Config.hexGridCellSize
import simulation.{Vector2D, XY}
import simulation.view.View

class Dijkstra extends Algorithm {

  val hexGrid: HexGrid = HexGrid(hexGridCellSize)

  override def desiredDirections(view: View, goal: HexRowCol): Map[HexRowCol, Vector2D] = {
    Map()
  }
}
