package socialforcemodel.path

import socialforcemodel.utils.Vector2D

import scala.util.Random._

class SimplePathStrategy extends PathStrategy {

  override def randomStart(): Vector2D = {
    val x = nextInt(350)
    Vector2D(x,nextInt(20) - 25)
  }

  override def randomGoal(): Vector2D = {
    Vector2D(600,1000)
  }
}
