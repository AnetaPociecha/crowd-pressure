package socialforcemodel.agents

import socialforcemodel.path.PathStrategy

import scala.collection.mutable.ArrayBuffer
import scala.util.Random._
import socialforcemodel.Config._

class AgentsManager(pathStrategy: PathStrategy) {

  @volatile var agents: ArrayBuffer[Agent] = ArrayBuffer()

  def init(): Unit = {
    for (_ <- 0 until initAgentsNumber) {
      agents += randomAgent()
    }
  }

  def step(interval: Double): Unit = {
    for(_ <- 0 until fixMoveRepeat) {
      agents.foreach(a => a.step(interval/fixMoveRepeat))
      agents = agents.reverse
    }
    clear()
    // add()
  }

  def clear(): Unit = {
    val trash: ArrayBuffer[Agent] = agents.filter(a => a.isGoalReached)
    trash.foreach(a => agents -= a)
  }

  def randomAgent(): Agent = {
    new Agent(
      pathStrategy.randomStart(),
      pathStrategy.randomGoal(),
      pathStrategy.randomSpeed(),
      agents
    )
  }

  def add(): Unit = {
    if(nextInt(100) < 50)
    agents += randomAgent()
  }

}
