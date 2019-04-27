package simulation.shortestpath.hexgrid

case class Neighborhood(graph: Array[Array[Boolean]] = Array()) {

  val neighbDirs0: Array[(Int, Int)] = Array((-1,0),(-1,1),(0,1),(1,0),(0,-1),(-1,-1))
  val neighbDirs1: Array[(Int, Int)] = Array((-1,0),(0,1),(1,1),(1,0),(1,-1),(0,-1))

  def rangeNeighbours(cur: (Int, Int)): Array[(Int, Int)] = {
    var dirs = neighbDirs0
    if(cur._2 % 2 == 1)
      dirs = neighbDirs1

    dirs
      .filter(dir => {
        val x = cur._1 + dir._1
        val y = cur._2 + dir._2
        x < graph(0).length && y < graph.length && x >= 0 && y >= 0 && graph(y)(x)
      })
      .map(dir => (cur._1 + dir._1, cur._2 + dir._2))
  }

  def neighbours(cur: (Int, Int)): Array[(Int, Int)] = {
    var dirs = neighbDirs0
    if(cur._2 % 2 == 1)
      dirs = neighbDirs1

    dirs
      .map(dir => (cur._1 + dir._1, cur._2 + dir._2))
  }
}
