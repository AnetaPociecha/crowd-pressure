package simulation

import simulation.shortestpathalgoritm.NavigationField
import simulation.Config.cellSize

class Agent(@volatile var position: Vector2D, val navigationField: NavigationField) {

  def step(): Unit = {
    val dir: Vector2D = navigationField.direction(position.x.toLong, position.y.toLong)
    // to do stuff
    position += (dir * 2)
  }

  def destinationReached(): Boolean = {
    (position - Vector2D(navigationField.x, navigationField.y)).magnitude < cellSize * 2
  }
}
