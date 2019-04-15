package simulation.socialforcemodel

import simulation.{Agent, Vector2D}
import simulation.Config._
import scalafx.scene.canvas.GraphicsContext

import scala.util.Random._

class AgentsManager(val x1: Int = 0, val x2: Int = sceneWidth, val y1: Int = 0, val y2: Int = sceneHeight) {

  val initialAgentsNumber = 10
  var agents: List[Agent] = List[Agent]()

  def init = () => {
    for(_ <- 0 until initialAgentsNumber) yield agents ::= createRandomAgent()
  }

  def createRandomAgent = () => {
    val start = createRandomStart()
    val goal = createRandomGoal(start)
    val speed = createRandomSpeed()
    new Agent(start, speed, goal, agents)
  }

  def createRandomStart = () => {
    val rand = nextInt(4)
    rand match {
      case 0  => Vector2D(x1, getRandomY())
      case 1  => Vector2D(x2, getRandomY())
      case 2  => Vector2D(getRandomX(), y1)
      case 3  => Vector2D(getRandomX(), y2)
      case _ => Vector2D(0, 0)
    }
  }

  def createRandomGoal = (start: Vector2D) => {
    val rand = nextInt(3)
    if(start.x == x1) {
      rand match {
        case 0  => Vector2D(getRandomX(), y1)
        case 1  => Vector2D(x2, getRandomY())
        case 2  => Vector2D(getRandomX(), y2)
        case _ => Vector2D(0, 0)
      }
    } else if(start.x == x2) {
      rand match {
        case 0  => Vector2D(getRandomX(), y1)
        case 1  => Vector2D(x1, getRandomY())
        case 2  => Vector2D(getRandomX(), y2)
        case _ => Vector2D(0, 0)
      }
    } else if(start.y == y1) {
      rand match {
        case 0  => Vector2D(x2, getRandomY())
        case 1  => Vector2D(x1, getRandomY())
        case 2  => Vector2D(getRandomX(), y2)
        case _ => Vector2D(0, 0)
      }
    } else if(start.y == y2) {
      rand match {
        case 0  => Vector2D(x2, getRandomY())
        case 1  => Vector2D(x1, getRandomY())
        case 2  => Vector2D(getRandomX(), y1)
        case _ => Vector2D(0, 0)
      }
    } else Vector2D(0, 0)
  }


  def getRandomY = () => {
    nextInt(y1 + y2) + y1
  }

  def getRandomX = () => {
    nextInt(x1 + x2) + x1
  }

  def createRandomSpeed = () => {
    minAgentSpeed + nextInt(speedRange)
  }

  def addAgent = () => {
    if(nextInt(100) < density) {
      val a = createRandomAgent()
      agents ::= a
    }
  }

  def step(gc: GraphicsContext, interval: Double): Unit = {

    agents = agents.filter(a => !a.isGoalReached)

    for(_ <- 0 until fixMoveRepeat) {
      agents.foreach(a => {
        a.move(interval/fixMoveRepeat)
      })
      agents = agents.reverse
    }

    agents.foreach(a => {
      gc.fillOval(
        a.position.x-pedestrianRadius,
        a.position.y-pedestrianRadius,
        pedestrianRadius*2,
        pedestrianRadius*2
      )
    })
    addAgent()
  }

}
