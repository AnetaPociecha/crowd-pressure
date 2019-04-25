package socialforcemodel.path

import socialforcemodel.utils.Vector2D

import scala.util.Random.nextInt

trait PathStrategy {
  def randomStart(): Vector2D
  def randomGoal(): Vector2D
  def randomSpeed(): Double = {
    20 + nextInt(5)
  }
}
