package simulation.shortestpath.hexgrid

import org.scalatest.FunSuite

class NeighborhoodTest extends FunSuite {

  val nh = Neighborhood(Array(
    Array(true, false),
    Array(true, true)
  ))

  test("testNeighbours") {
    val n = nh.neighbours(0,0)
    assertResult(n.length)(6)
  }

  test("testRangeNeighbours") {
    val n = nh.rangeNeighbours(0,0)
    assert(n.contains((0,1)))
    assert(n.contains((1,0)))
    assert(n.length == 2)
  }

  test("testRangeNonObstacleNeighbours") {
    val n = nh.rangeNonObstacleNeighbours(0,0)
    assert(!n.contains((1,0)))
    assert(n.contains((0,1)))
    assert(n.length == 1)
  }
}
