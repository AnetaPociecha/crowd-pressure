package scenemodel

import socialforcemodel.utils.Vector2D

case class AltSceneModel(walls: Set[Wall] = Set(
  Wall
  (
    Set(
      Edge(Vector2D(400,0), Vector2D(400, 500)),
      Edge(Vector2D(400, 500), Vector2D(800, 500))
    ),
    Vector2D(400,0),
    Vector2D(800, 500)
  )
)) extends SceneModel {}
