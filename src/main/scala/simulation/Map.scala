package simulation

case class Map(width: Int = 20, height: Int = 20) {
  def isObstacle(x: Int, y: Int): Boolean = { // TO DO
    x < 10 && y < 10
  }
}
