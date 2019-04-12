package simulation.logic

import org.scalatest.{BeforeAndAfterEach, FunSuite}
import simulation.Vector2D

class DirectionCalculatorTest extends FunSuite with BeforeAndAfterEach {

  var list: List[Agent] = List()
  val neighbour = new Agent(Vector2D(8,1), 20, Vector2D(4,4), list)
  val agent = new Agent(Vector2D(1,1), 20, Vector2D(4,4), list)

  val directionCalculator = new DirectionCalculator(agent, list)

  override def beforeEach(): Unit = {
  }

  test("testGetCollisionVector") {
    val vect = directionCalculator.getCollisionVector(neighbour)

    val neighbour2 = new Agent(Vector2D(3,3), 20, Vector2D(4,4), list)
    val vect2 = directionCalculator.getCollisionVector(neighbour2)

    assertResult(Vector2D(-7,0))(vect)
    assertResult(Vector2D(-2,-2))(vect2)
  }

  test("testGetDistanceFrom") {
    val dist = directionCalculator.getDistanceFrom(neighbour)
    assertResult(7)(dist)

  }

  test("testGetFixedDirection") {
    val dir = directionCalculator.getFixedVelocity(Vector2D(0,30))
    val v = Vector2D(-7,30)
    assertResult(v)(dir)
  }
}
