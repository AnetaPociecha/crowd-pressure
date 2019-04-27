package scenemodel

import simulation.utils.Vector2D

trait SceneModel {
  val walls: Set[Wall]
  val destinations: Array[Vector2D] = Array()
  def isObstacle(x: Int, y: Int): Boolean = {
    false
  }
  def start: Vector2D = {Vector2D(0,0)}
}
