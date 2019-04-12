package simulation.logic

import simulation.Vector2D
import simulation.Config._
import scala.util.Random._

class DirectionCalculator(
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
      fixed += collisionVector * collisionVectorWeight
    }

    if(fixed.magnitude > agent.speed)
      fixed = fixed / fixed.magnitude * agent.speed

    if(fixed.magnitude < 1) {
      fixed = Vector2D(nextDouble(), nextDouble()) * (agent.speed / 5)
      println("trouble")
    }

    fixed
  }

  def getCollisionVector(neighbour: Agent): Vector2D = {
    agent.position - neighbour.position
  }

  def getDistanceFrom(neighbour: Agent): Double = {
    (neighbour.position - agent.position).magnitude
  }
}
