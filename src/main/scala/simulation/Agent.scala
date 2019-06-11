package simulation

import simulation.shortestpathalgoritm.NavigationField
import config.Config.{CellSize, Speed}
import graphics.SVGBrowser

class Agent(
             @volatile var position: Vector2D,
             val navigationField: NavigationField,
           ) {

  def step(interval : Double): Unit = {
    val dir: Vector2D = navigationField.direction(position.x.toLong, position.y.toLong)
    position += (dir * interval * Speed)
  }

  def destinationReached(): Boolean = {
    ((position - Vector2D(navigationField.xStop, navigationField.yStop)).magnitude < CellSize * 2 + 6)
//      || position.x <= 0 || position.y <=0 || position.x >= SVGBrowser.stage.width.toDouble || position.x >= SVGBrowser.stage.height.toDouble - SVGBrowser.box.height.toDouble)
  }
}
