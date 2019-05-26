package simulation

import org.scalatest.FunSuite

class DesiredDirectionModelTest extends FunSuite {

  test("testCreateGraph") {

    val mapModel = MapModel()
    mapModel.initGraph()

    val dijkstra = DikjstraModel(mapModel, 15, 15)
    val dg = dijkstra.createGraph()

    val direction = DesiredDirectionModel(dg, mapModel)
    val g = direction.createGraph()

    assertResult(Vector2D(0,0))(g(5,4))
  }

}
