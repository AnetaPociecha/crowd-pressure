package simulation.hexgrid

case class Neighborhood() {

  val neighbRow0: List[(Int, Int)] = List((0,-1),(1,-1),(1,0),(0,1),(-1,0),(-1,-1))
  val neighbRow1: List[(Int, Int)] = List((0,-1),(1,0),(1,1),(0,1),(-1,1),(-1,0))

  def get(row: Long, col: Long): List[(Long, Long)] = {
    var dirs = neighbRow0
    if(row % 2 == 1)
      dirs = neighbRow1

    dirs
      .map(dir => (row + dir._1, col + dir._2))
  }
}
