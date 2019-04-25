package socialforcemodel.agents

import socialforcemodel.utils.Vector2D
import socialforcemodel.Config._

import scala.collection.mutable.ArrayBuffer

class Agent(
             @volatile var position: Vector2D,
             val goal: Vector2D,
             val desiredSpeed: Double,
             val agents: ArrayBuffer[Agent],
             val weight: Double = 70,
             @volatile var speed: Double = 0
           ) {

  val velocityCalc = VelocityCalc(this)

  def isGoalReached: Boolean = {
    (position - goal).magnitude < 20
  }

  def step(interval: Double): Unit = {
    val shift: Vector2D = velocityCalc.velocity(interval) * interval
    val newPosition = position + shift
    position = newPosition
  }

  def personalSpaceCenter(direction: Vector2D): Vector2D = {
    position + direction / direction.magnitude * personalSpaceCenterShift
  }
}
