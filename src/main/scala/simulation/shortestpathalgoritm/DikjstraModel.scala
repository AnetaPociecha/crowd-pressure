package simulation.shortestpathalgoritm

import simulation.hexgrid.Neighborhood

import scala.collection.mutable


case class DikjstraModel(mapModel: MapModel, x: Long, y: Long) {

  val inf: Long = 1000

  private val graph: mutable.Map[(Long, Long), Long] = {
    val g: mutable.Map[(Long, Long), Long] = mutable.Map()
    mapModel.graph.foreach(node => {
      g += (node._1 -> inf)
    })
    g
  }

  private val visited: mutable.Map[(Long, Long), Boolean] = {
    val g: mutable.Map[(Long, Long), Boolean] = mutable.Map()
    mapModel.graph.foreach(node => {
      g += (node._1 -> mapModel.graph(node._1))
    })
    g
  }

  private val neighborhood = Neighborhood()

  def createGraph(): mutable.Map[(Long, Long), Long] = {

    println("Dijkstra graph")

    val rowCol = mapModel.hexGrid.convertXYToRowCol(x,y)
    @volatile var curNodes: mutable.MutableList[(Long, Long)] = mutable.MutableList((rowCol.row, rowCol.col))
    @volatile var nextNodes: mutable.MutableList[(Long, Long)] = mutable.MutableList()
    @volatile var cost = 0

    while( visited.values.exists(_ == false) && curNodes.nonEmpty) {
      println("process nodes: "+curNodes)

      for(node <- curNodes) {


        visit(node, cost)
        nextNodes ++= neighbours(node._1, node._2)

      }

      curNodes = nextNodes
      nextNodes = mutable.MutableList()
      cost += 1
    }

    graph
  }

  private def visit(node: (Long, Long), cost: Int): Unit = {
    visited(node) = true
    if(graph(node) > cost) graph(node) = cost
  }

  private def neighbours(row: Long, col: Long): List[(Long, Long)] = {
    val neighbours: List[(Long, Long)] = neighborhood.get(row,col).filter(node => {
      mapModel.graph.contains(node) && !mapModel.graph(node) && !visited(node)
    })
    neighbours
  }
}