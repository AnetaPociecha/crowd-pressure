package simulation.shortestpath

import simulation.shortestpath.hexgrid.Neighborhood

import scala.collection.mutable

case class Dijkstra(graph: Array[Array[Boolean]]) {

  val width: Int = graph(0).length
  val height: Int = graph.length

  val inf: Int = 10000

  val neighbourhood: Neighborhood = Neighborhood(graph)

  val visited: mutable.Map[(Int, Int), Boolean] = {
    var map: mutable.Map[(Int, Int), Boolean] = mutable.Map()
    for(i <- 0 until width; j <- 0 until height) {
      map += ((i,j) -> false)
    }
    map
  }

  def costsMap(dest: (Int, Int)): mutable.Map[(Int, Int), Int] = {

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
    //ret.filter(e => e._2 != inf)
    ret
  }

  def cost(cur: (Int,Int), costs: mutable.Map[(Int, Int), Int]): Int = {
    val neighb = neighbourhood.rangeNeighbours(cur)
    costs.filter(c => neighb.contains(c._1)).values.min + 1
  }

  def nonVisitedNeighbours(cur: (Int,Int)): mutable.Queue[(Int,Int)] = {
    var ret: mutable.Queue[(Int,Int)] = mutable.Queue()
    neighbourhood.rangeNeighbours(cur).filter(n => !visited(n)).foreach(n => ret += n)
    ret
  }
}
