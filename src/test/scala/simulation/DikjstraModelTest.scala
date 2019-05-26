package simulation

import org.scalatest.FunSuite

class DikjstraModelTest extends FunSuite {

  test("testCreateGraph") {
    val mapModel = MapModel()
    mapModel.initGraph()
    val model = DikjstraModel(mapModel, 15, 15)
    val g = model.createGraph()
    assertResult(0)(g(5,4))
    assertResult(1)(g(4,4))
  }

}
