package socialforcemodel.agents

import socialforcemodel.Config.{agentRadius, personalSpaceRadius, selfStopAngle}
import scenemodel.{Edge, SceneModel, SceneModelCreator}
import socialforcemodel.utils.{CircleSelectorHelper, PointWallDirCalc, Vector2D}
import socialforcemodel.Config._

case class VelocityCalc(agent: Agent) {

  def velocity(interval: Double): Vector2D = {
    val des = desired(interval)
    var fixed = avoidAgentsCollision(des)
    fixed = avoidWallCollision(fixed)

    if(fixed.magnitude > agent.desiredSpeed)
      fixed = fixed / fixed.magnitude * agent.desiredSpeed

    agent.speed = fixed.magnitude

    fixed
  }

  def avoidWallCollision(velocity: Vector2D): Vector2D = {

    var fixed = velocity

    val A = 2000
    val B = 1
    val k = 120000

    val sceneModel: SceneModel = SceneModelCreator.create()

    var edges: Set[Edge] = Set()
    sceneModel.walls.foreach(w => edges ++= w.edges)

    val calcHelper = PointWallDirCalc(agent.position)
    edges
      .filter(w => {
        val dir = calcHelper.direction(w.start, w.stop)
        dir.isDefined && dir.get.magnitude < wallForceRadius
      })
      .foreach(w => {
        val dir = calcHelper.direction(w.start, w.stop).get
        val dist = dir.magnitude
        val d = (agentRadius + 2) - dist

        val f_ijn1 =  A * Math.exp(d / B)
        val f_ijn2 = if(d > 0) k * d else 0

        val force =  dir * (f_ijn1 + f_ijn2)

        fixed += force
      })

    fixed
  }

  def desired(interval: Double): Vector2D = {
    val diff = agent.goal - agent.position
    val dir = diff / diff.magnitude

    val velocity = dir * agent.weight * (agent.desiredSpeed - agent.speed) / interval

    if(isFontCollisionDanger(velocity))
      Vector2D(0,0)
    else
      velocity
  }

  def avoidAgentsCollision(velocity: Vector2D): Vector2D = {

    var fixed = velocity

    val A = 2000
    val B = 1
    val k = 120000

    agent.agents.filter(a => a != agent && inPersonalSpace(a, velocity)).foreach(a => {

      val diff: Vector2D = agent.position - a.position
      val dist: Double = diff.magnitude
      val dir: Vector2D = diff / dist

      val d = (agentRadius + 2) * 2 - dist

      val f_ijn1 =  A * Math.exp(d / B)
      val f_ijn2 = if(d > 0) k * d else 0

      val force =  dir * (f_ijn1 + f_ijn2)

      fixed += force
    })

    fixed
  }

  def isFontCollisionDanger(velocity: Vector2D): Boolean = {
    val rightArm = rotate(velocity, - selfStopAngle)
    val leftArm = rotate(velocity, selfStopAngle)

    val circle: CircleSelectorHelper = CircleSelectorHelper(
      agent.position, rightArm, leftArm, personalSpaceRadius * 0.6)

    agent.agents
      .filter(a => a != agent && inPersonalSpace(a, velocity))
      .exists(a => circle isInsideSector a.position)
  }

  def inPersonalSpace(a: Agent, velocity: Vector2D): Boolean = {
    (agent.personalSpaceCenter(velocity) - a.position).magnitude < personalSpaceRadius
  }

  def rotate(v: Vector2D, angle: Double): Vector2D = {
    val radians = Math.toRadians(angle)
    val x = v.x * Math.cos(radians) - v.y * Math.sin(radians)
    val y = v.x * Math.sin(radians) + v.y * Math.cos(radians)
    Vector2D(x,y)
  }
}
