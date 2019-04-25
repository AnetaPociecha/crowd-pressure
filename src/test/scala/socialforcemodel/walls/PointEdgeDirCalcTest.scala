package socialforcemodel.walls

import org.scalatest.FunSuite
import socialforcemodel.utils.{PointWallDirCalc, Vector2D}

class PointEdgeDirCalcTest extends FunSuite {

  test("testDot") {

    val point = Vector2D(3,4)
    val calc = PointWallDirCalc(point)

    val res1 = calc.dot(Vector2D(1,1), Vector2D(3,4))

    assertResult(7)(res1)
  }

  test("testDirection") {
    val point = Vector2D(3,4)
    val calc = PointWallDirCalc(point)

    val res1 = calc.direction(Vector2D(1,1), Vector2D(7,1))
    val res2 = calc.direction(Vector2D(3,8), Vector2D(8,8))

    val p = Vector2D(453.3015904942156,290.7133777508621)
    val calc2 = PointWallDirCalc(p)
    val res3 = calc2.direction(Vector2D(450.0,550.0), Vector2D(450,600))

    assertResult(Vector2D(0, 3))(res1.get)
    assertResult(Vector2D(0, -4))(res2.get)
    assert(res3.isEmpty)
  }

  test("testDistance") {
    val point = Vector2D(3,4)
    val calc = PointWallDirCalc(point)

    val res1 = calc.distance(Vector2D(1,1), Vector2D(7,1))

    assertResult(3)(res1)
  }

}
