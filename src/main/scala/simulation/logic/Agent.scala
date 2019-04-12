package simulation.logic

import simulation.Vector2D

class Agent(var position: Vector2D,
            var speed: Double = 30,
            var goal: Vector2D = Vector2D(500,500),
            val agents: List[Agent]) {

  val directionCalculator: DirectionCalculator
    = new DirectionCalculator(this, agents)

  def getDistanceToGoal: Vector2D =  goal - position

  def move (interval: Double): Unit = {
    val desiredVelocity = getDesiredDirection * speed
    val fixedVelocity = directionCalculator.getFixedVelocity(desiredVelocity)

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

}
