package scenemodel

trait SceneModel {
  val walls: Set[Wall]

  def isObstacle(x: Int, y: Int): Boolean = {
    false
  }
}
