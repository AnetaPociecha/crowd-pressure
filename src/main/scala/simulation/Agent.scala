package simulation

import simulation.shortestpathalgoritm.NavigationField
import config.Config.{cellSize, speed}

class Agent(
             @volatile var position: Vector2D,
             val navigationField: NavigationField,
           ) {

  def step(): Unit = {
    val dir: Vector2D = navigationField.direction(position.x.toLong, position.y.toLong)
    position += (dir * speed)
  }

  def destinationReached(): Boolean = {
    (position - Vector2D(navigationField.x, navigationField.y)).magnitude < cellSize * 2
  }
}
