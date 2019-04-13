package simulation.logic

import simulation.Vector2D
import simulation.Config._
import scala.util.Random._

class VelocityCalculator(
                         agent: Agent,
                         agentsList: List[Agent]
                         ) {

  def getFixedVelocity(direction: Vector2D, privateSpaceCenter: Vector2D): Vector2D = {
    val neighbours: List[Agent] = agentsList
      .filter( a => a != agent
        && inPrivateSpace(a, privateSpaceCenter))

    var fixed: Vector2D = direction

    for(n <- neighbours) {
      val collisionVector: Vector2D = getCollisionVector(n)
      fixed += collisionVector * agent.speed * collisionVectorWeight
    }

    if(fixed.magnitude > agent.speed)
      fixed = fixed / fixed.magnitude * agent.speed

    fixed
  }

  def getCollisionVector(neighbour: Agent): Vector2D = {
    val diff = agent.position - neighbour.position
    diff / diff.magnitude
  }

  def inPrivateSpace(neighbour: Agent, privateSpaceCenter: Vector2D): Boolean = {
    (neighbour.position - privateSpaceCenter).magnitude < privateSpaceRadius
  }
}
