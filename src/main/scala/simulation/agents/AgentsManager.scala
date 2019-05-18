package simulation.agents

import scalafx.scene.paint.Color._
import scalafx.scene.paint.Color
import scenemodel.{SceneModel, SceneModelCreator}
import simulation.shortestpath.DesiredDirCalc

import scala.collection.mutable.ArrayBuffer
import scala.util.Random._
import simulation.Config._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{Duration, SECONDS}
import scala.concurrent.{Await, Future, blocking}

class AgentsManager() {

  val scene: SceneModel = SceneModelCreator.instance()
  var desiredDirCalcs: Array[DesiredDirCalc] = Array[DesiredDirCalc]()
  val colors: Array[Color] = Array(Crimson, LawnGreen, RoyalBlue, Red, GreenYellow)

  @volatile var agents: ArrayBuffer[Agent] = ArrayBuffer[Agent]()

  def init(): Unit = {
    desiredDirCalcs = scene.destinations.map(g => DesiredDirCalc(g))

    for (_ <- 0 until initAgentsNumber) {
      agents += randomAgent()
    }
  }

  def step(interval: Double): Unit = {
    for(_ <- 0 until fixMoveRepeat) {
      agents.foreach(a => Await.ready(Future {
        blocking(a.step(interval/fixMoveRepeat).run())
      }, Duration(1, SECONDS)))
    }
    clear()
    add()
  }

  def clear(): Unit = {
    val trash: ArrayBuffer[Agent] = agents.filter(a => a.isGoalReached)
    trash.foreach(a => agents -= a)
  }

  def randomAgent(): Agent = {
    val rand: Int = nextInt(desiredDirCalcs.length)
    new Agent(
      scene.start,
      desiredDirCalcs(rand),
      minAgentSpeed + nextInt(agentSpeedRange),
      agents,
      colors(rand)
    )
  }

  def add(): Unit = {
    if(nextInt(100) < density)
    agents += randomAgent()
  }
}
