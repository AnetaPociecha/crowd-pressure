package scenemodel

import socialforcemodel.utils.Vector2D

case class ExitSceneModel(walls: Set[Wall] = Set(
  Wall
  (
    Set(
      Edge(Vector2D(0,550), Vector2D(350, 550)),
      Edge(Vector2D(350, 550), Vector2D(350, 600))
    ),
    Vector2D(0,550),
    Vector2D(350, 600)
  ),
  Wall
  (
    Set(
      Edge(Vector2D(450,550), Vector2D(800,550)),
      Edge(Vector2D(450, 550), Vector2D(450, 600))
    ),
    Vector2D(450,550),
    Vector2D(800, 600)
  )
)) extends SceneModel {}
