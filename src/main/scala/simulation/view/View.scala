package simulation.view

import simulation.XY

trait View {
  def isObstacle(xy: XY): Boolean
}
