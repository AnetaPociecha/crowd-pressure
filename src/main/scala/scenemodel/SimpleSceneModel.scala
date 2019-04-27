package scenemodel

import simulation.utils.Vector2D

import scala.util.Random.nextInt

case class SimpleSceneModel(walls: Set[Wall] = Set(
  Wall
  (
    Set(
      Edge(Vector2D(400,0), Vector2D(400, 400)),
      Edge(Vector2D(400, 400), Vector2D(800, 400))
    ),
    Vector2D(400,0),
    Vector2D(800, 400)
  )
)) extends SceneModel {

  override def isObstacle(x: Int, y: Int): Boolean = {
    x >= 400 && y <= 400
  }

  override val destinations: Array[Vector2D] = Array(
    Vector2D(400, 654),
    Vector2D(650, 654),
    Vector2D(880, 476),
    Vector2D(850, 543),
  )

  override def start: Vector2D = {
    val x = nextInt(320) + 20
    Vector2D(x, nextInt(5))
  }

}
