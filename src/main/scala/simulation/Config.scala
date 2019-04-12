package simulation

object Config {
  val sceneWidth: Int = 600
  val sceneHeight: Int = 600
  val pedestrianRadius: Int = 5
  val privateSpaceRadius: Int = 20
  val minAgentSpeed: Int = 10
  val speedRange: Int = 5
  val collisionVectorWeight: Int = 4
  val density: Int = 5 // probability of new agent appearance per interval in %
  val fixMoveRepeat: Int = 6
}
