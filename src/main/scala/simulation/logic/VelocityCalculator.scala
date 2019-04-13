package simulation.logic

import simulation.Vector2D
import simulation.Config._
import scala.util.Random._

class VelocityCalculator(
                         agent: Agent,
                         agentsList: List[Agent]
                         ) {

  def getFixedVelocity(direction: Vector2D): Vector2D = {
    val neighbours: List[Agent] = agentsList
      .filter( a => a != agent
        && getDistanceFrom(a) < privateSpaceRadius)

    var fixed: Vector2D = direction

    for(n <- neighbours) {
      val collisionVector: Vector2D = getCollisionVector(n)
      fixed += collisionVector * agent.speed
    }

    if(fixed.magnitude > agent.speed)
      fixed = fixed / fixed.magnitude * agent.speed

    fixed
  }

  def getCollisionVector(neighbour: Agent): Vector2D = {
    val diff = agent.position - neighbour.position
    diff / diff.magnitude
  }

  def getDistanceFrom(neighbour: Agent): Double = {
    (neighbour.position - agent.position).magnitude
  }
}
