package simulation

import scala.collection.mutable

case class Navigation(x: Long, y: Long) {

  def get(): mutable.Map[(Long, Long), Vector2D] = {
    val mapModel = MapModel()
    mapModel.initGraph()

    val dijkstra = DikjstraModel(mapModel, x, y)
    val dg = dijkstra.createGraph()

    val direction = DesiredDirectionModel(dg, mapModel)
    val g = direction.createGraph()
    g
  }
}
