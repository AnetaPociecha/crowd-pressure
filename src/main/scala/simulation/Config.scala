package simulation

object Config {
  val sceneWidth: Int = 600
  val sceneHeight: Int = 600
  val pedestrianRadius: Int = 5
  val privateSpaceRadius: Int = 20
  val privateSpaceCenterShift: Double = privateSpaceRadius / 6
  val minAgentSpeed: Int = 15
  val speedRange: Int = 5
  val collisionVectorWeight: Double = 1.2
  val density: Int = 5 // probability of new agent appearance per interval in %
  val fixMoveRepeat: Int = 4
  val hexGridCellSize: Int = 10
}
