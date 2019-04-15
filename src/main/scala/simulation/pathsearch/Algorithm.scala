package simulation.pathsearch

import hexgrid.HexRowCol
import simulation.{Vector2D, XY}
import simulation.view.View

trait Algorithm {
  def desiredDirections(view: View, goal: HexRowCol): Map[HexRowCol, Vector2D]
}
