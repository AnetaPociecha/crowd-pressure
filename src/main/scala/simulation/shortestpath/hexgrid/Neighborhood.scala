package simulation.shortestpath.hexgrid

case class Neighborhood(graph: Array[Array[Boolean]] = Array()) {

  val neighbDirs0: Array[(Int, Int)] = Array((-1,0),(-1,1),(0,1),(1,0),(0,-1),(-1,-1))
  val neighbDirs1: Array[(Int, Int)] = Array((-1,0),(0,1),(1,1),(1,0),(1,-1),(0,-1))

  def neighbours(cur: (Int, Int)): Array[(Int, Int)] = {
    var dirs = neighbDirs0
    if(cur._2 % 2 == 1)
      dirs = neighbDirs1

    dirs
      .map(dir => (cur._1 + dir._1, cur._2 + dir._2))
  }

  def rangeNeighbours(cur: (Int, Int)): Array[(Int, Int)] = {
    neighbours(cur)
      .filter(dir => {
        dir._1 < graph(0).length && dir._2 < graph.length && dir._1 >= 0 &&  dir._2 >= 0
      })
  }

  def rangeNonObstacleNeighbours(cur: (Int, Int)): Array[(Int, Int)] = {
    rangeNeighbours(cur)
      .filter(dir => graph(dir._2)(dir._1))
  }
}
