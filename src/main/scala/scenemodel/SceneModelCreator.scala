package scenemodel

object SceneModelCreator {
  val sceneModel: SceneModel = MySceneModel()
  def instance(): SceneModel = {
    sceneModel
  }
}
