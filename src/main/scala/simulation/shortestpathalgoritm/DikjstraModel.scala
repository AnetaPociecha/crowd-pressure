package simulation.shortestpathalgoritm

import simulation.hexgrid.Neighborhood
import config.Config.PathWidthInCells

import scala.collection.mutable


case class DikjstraModel(mapModel: MapModel, xStop: Long, yStop: Long, xStart: Long, yStart: Long) {

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

    val rowCol = mapModel.hexGrid.convertXYToRowCol(xStop, yStop)
    @volatile var curNodes: mutable.MutableList[(Long, Long)] = mutable.MutableList((rowCol.row, rowCol.col))
    @volatile var nextNodes: mutable.MutableList[(Long, Long)] = mutable.MutableList()
    @volatile var cost = 0

    while( visited.values.exists(_ == false) && curNodes.nonEmpty ) {

      for(node <- curNodes) {
        nextNodes ++= neighbours(node._1, node._2)
      }

      var estiCostMap: mutable.TreeMap[(Long, Long), Double] = mutable.TreeMap()

      nextNodes.foreach( node =>
        estiCostMap += ( node -> estiCost(node) )
      )

      var seq = estiCostMap.toSeq.sortBy(_._2)
      seq = seq.take(PathWidthInCells)

      seq.foreach(s => visit(s._1, cost))


      curNodes = mutable.MutableList()
      seq.foreach(s => curNodes += s._1)

      nextNodes = mutable.MutableList()

      cost += 1
    }

    graph
  }

  private def estiCost(node: (Long, Long)): Double = {
    val xy = mapModel.hexGrid.convertRowColToHexXY(node._1, node._2)
    Math.sqrt(Math.pow(xy.x - xStart, 2) + Math.pow(xy.y - yStart, 2))
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
