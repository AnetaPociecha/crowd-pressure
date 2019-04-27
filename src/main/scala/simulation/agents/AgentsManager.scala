package simulation.agents

import scenemodel.{SceneModel, SceneModelCreator}
import simulation.shortestpath.DesiredDirCalc

import scala.collection.mutable.ArrayBuffer
import scala.util.Random._
import simulation.Config._
import simulation.utils.Vector2D

class AgentsManager() {

  val scene: SceneModel = SceneModelCreator.create()

  val desiredDirCalcs: Array[DesiredDirCalc]
    = scene.destinations.map(g => DesiredDirCalc(g))

  @volatile var agents: ArrayBuffer[Agent] = ArrayBuffer[Agent]()


  def init(): Unit = {
    for (_ <- 0 until initAgentsNumber) {
      agents += randomAgent()
    }
  }

  def step(interval: Double): Unit = {
    for(_ <- 0 until fixMoveRepeat) {
      agents.foreach(a => a.step(interval/fixMoveRepeat))
      //agents = agents.reverse
    }
    clear()
    add()
  }

  def clear(): Unit = {
    val trash: ArrayBuffer[Agent] = agents.filter(a => a.isGoalReached)
    trash.foreach(a => agents -= a)
  }

  def randomAgent(): Agent = {
    new Agent(
      scene.start,
      goal,
      speed,
      agents
    )
  }

  def add(): Unit = {
    if(nextInt(100) < density)
    agents += randomAgent()
  }

  def speed: Double = {
    minAgentSpeed + nextInt(agentSpeedRange)
  }

  def goal: DesiredDirCalc = {
    desiredDirCalcs(nextInt(desiredDirCalcs.length))
  }

}
