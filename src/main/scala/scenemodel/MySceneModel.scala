package scenemodel

import simulation.utils.Vector2D

import scala.util.Random.nextInt

case class MySceneModel(walls: Set[Wall] = Set(
  Wall // 1
  (
    Set(
      Edge(Vector2D(200,0), Vector2D(200, 200)),
      Edge(Vector2D(200, 200), Vector2D(0, 200))
    ),
    Vector2D(0,0),
    Vector2D(200, 200)
  ),

  Wall // 2
  (
    Set(
      Edge(Vector2D(600,0), Vector2D(600, 200)),
      Edge(Vector2D(600, 200), Vector2D(800, 200))
    ),
    Vector2D(600,0),
    Vector2D(1200, 200)
  ),

  Wall // 3
  (
    Set(
      Edge(Vector2D(0,400), Vector2D(100, 400)),
      Edge(Vector2D(100, 400), Vector2D(100, 600))
    ),
    Vector2D(0,400),
    Vector2D(100, 900)
  ),

  Wall // 4
  (
    Set(
      Edge(Vector2D(300, 400), Vector2D(300, 600)),
      Edge(Vector2D(300, 400), Vector2D(500, 400)),
      Edge(Vector2D(500, 400), Vector2D(500, 600))
    ),
    Vector2D(300, 400),
    Vector2D(500, 900)
  ),

  Wall // 5
  (
    Set(
      Edge(Vector2D(700,400), Vector2D(800, 400)),
      Edge(Vector2D(700, 400), Vector2D(700, 600))
    ),
    Vector2D(700, 400),
    Vector2D(1100, 900)
  )

)) extends SceneModel {

  override def isObstacle(x: Int, y: Int): Boolean = {

    walls.exists(w => {
      w.start.x <= x && w.start.y <= y && w.stop.x >= x && w.stop.y >= y
    })
  }

  override val destinations: Array[Vector2D] = Array(
    Vector2D(200, 700),
    Vector2D(600, 700),
    Vector2D(900, 300)
  )

  override def start: Vector2D = {
    if(nextInt(3) == 0) {
      val y = nextInt(100) + 250
      Vector2D(5, y)
    } else {
      val x = nextInt(300) + 250
      Vector2D(x, 5)
    }

  }

}
