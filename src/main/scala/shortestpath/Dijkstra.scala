package shortestpath

import scala.collection.mutable

case class Dijkstra(graph: Array[Array[Boolean]]) {

  val width: Int = graph(0).length
  val height: Int = graph.length

  val visited: mutable.Map[(Int, Int), Boolean] = {
    var map: mutable.Map[(Int, Int), Boolean] = mutable.Map()
    for(i <- 0 until width; j <- 0 until height) {
      map += ((i,j) -> false)
    }
    map
  }

  val inf: Int = 10000

  val neighbDirs0: Array[(Int, Int)] = Array((-1,0),(-1,1),(0,1),(1,0),(0,-1),(-1,-1))
  val neighbDirs1: Array[(Int, Int)] = Array((-1,0),(0,1),(1,1),(1,0),(1,-1),(0,-1))

  def costArray(dest: (Int, Int)): mutable.Map[(Int, Int), Int] = {

    val ret: mutable.Map[(Int, Int), Int] = {
      var map: mutable.Map[(Int, Int), Int] = mutable.Map()
      for(i <- 0 until width; j <- 0 until height) {
        map += ((i,j) -> inf)
      }
      map
    }

    val neighbours: mutable.Queue[(Int, Int)] = mutable.Queue()

    ret(dest) = 0
    visited(dest) = true

    neighbours ++= nonVisitedNeighbours(dest)

    while(neighbours.nonEmpty) {

      val n = neighbours.dequeue()

      ret(n) = cost(n, ret)
      visited(n) = true

      neighbours ++= nonVisitedNeighbours(n)
    }

    ret
  }

  def cost(cur: (Int,Int), costs: mutable.Map[(Int, Int), Int]): Int = {
    val neighb = neighbours(cur)
    costs.filter(c => neighb.contains(c._1)).values.min + 1
  }

  def nonVisitedNeighbours(cur: (Int,Int)): mutable.Queue[(Int,Int)] = {

    var ret: mutable.Queue[(Int,Int)] = mutable.Queue()

    neighbours(cur).filter(n => !visited(n)).foreach(n => ret += n)

    ret
  }

  def neighbours(cur: (Int, Int)): Array[(Int, Int)] = {

    var dirs = neighbDirs0
    if(cur._1 % 2 == 1)
      dirs = neighbDirs1

    dirs
      .filter(dir => {
        val x = cur._1 + dir._1
        val y = cur._2 + dir._2
        x < width && y < height && x >= 0 && y >= 0 && graph(y)(x)
      })
      .map(dir => (cur._1 + dir._1, cur._2 + dir._2))
  }
}
