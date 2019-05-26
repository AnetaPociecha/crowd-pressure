package simulation

import org.scalatest.FunSuite

class MapModelTest extends FunSuite {

  test("testInitGraph") {
    val mapModel = MapModel(cellSize = 10)

    mapModel.initGraph()

    assert(mapModel.graph.get(0,0).get)
  }

}
