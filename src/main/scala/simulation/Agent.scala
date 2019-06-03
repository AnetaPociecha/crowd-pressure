package simulation

import simulation.shortestpathalgoritm.NavigationField
import config.Config.{CellSize, Speed}

class Agent(
             @volatile var position: Vector2D,
             val navigationField: NavigationField,
           ) {

  def step(interval : Double): Unit = {
    val dir: Vector2D = navigationField.direction(position.x.toLong, position.y.toLong)
    position += (dir * interval * Speed)
  }

  def destinationReached(): Boolean = {
    (position - Vector2D(navigationField.x, navigationField.y)).magnitude < CellSize * 2
  }
}
