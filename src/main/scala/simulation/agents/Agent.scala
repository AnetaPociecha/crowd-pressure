package simulation.agents

import simulation.shortestpath.DesiredDirCalc
import simulation.utils.Vector2D
import simulation.Config._

import scala.collection.mutable.ArrayBuffer

class Agent(
             @volatile var position: Vector2D,
             val desiredDirCalc: DesiredDirCalc,
             val desiredSpeed: Double,
             val agents: ArrayBuffer[Agent],
             val weight: Double = 70,
             @volatile var speed: Double = 0
           ) {

  val velocityCalc = VelocityCalc(this)

  def isGoalReached: Boolean = {
    val tol = 5
    ((position - desiredDirCalc.goal).magnitude < 5 ||
    position.x < 0 || position.y < 0 || position.x > canvasWidth + tol ||
    position.y > canvasHeight + tol)
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
