package simulation

import simulation.Config._
import simulation.socialforcemodel.VelocityCalculator

class Agent(var position: Vector2D,
            var speed: Double = 30,
            var goal: Vector2D = Vector2D(500,500),
            val agents: List[Agent]) {

  val directionCalculator: VelocityCalculator
    = new VelocityCalculator(this, agents)

  def getDistanceToGoal: Vector2D =  goal - position

  def move (interval: Double): Unit = {
    val direction = getDesiredDirection
    val center = privateSpaceCenter(direction)
    val desiredVelocity = direction * speed

    val fixedVelocity = directionCalculator.getFixedVelocity(desiredVelocity, center)

    val increase = fixedVelocity * interval
    position = position + increase
  }

  def getDesiredDirection: Vector2D = {
    val distance = getDistanceToGoal
    val normal = distance / distance.magnitude
    normal
  }

  def isGoalReached: Boolean =
    getDistanceToGoal.magnitude < 30

  def privateSpaceCenter(direction: Vector2D): Vector2D = {
    position + direction * privateSpaceCenterShift
  }

}
