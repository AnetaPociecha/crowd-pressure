package simulation

import org.scalatest.FunSuite

class NavigationTest extends FunSuite {

  test("testGet") {
    val nav = Navigation(15,15).get()

    assertResult(Vector2D(0,0))(nav(5,4))
  }

}
