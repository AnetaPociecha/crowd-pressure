package simulation.shortestpathalgoritm

import simulation.Vector2D
import simulation.hexgrid.Neighborhood

import scala.collection.mutable

case class DesiredDirectionModel(dijkstraGraph: mutable.Map[(Long, Long), Long], mapModel: MapModel) {

  private val graph: mutable.Map[(Long, Long), Vector2D] = mutable.Map()
  val neighbourhood: Neighborhood = Neighborhood()

  def createGraph(): mutable.Map[(Long, Long), Vector2D] = {
    println("desired direction graph")
    dijkstraGraph.foreach(node => {
      graph += (node._1 -> direction(node._1._1, node._1._2))
    })
    graph
  }

  private def direction(row: Long, col: Long): Vector2D = {
    val dest: (Long, Long) = minCostNeighbour(row, col)

    val curCoors = mapModel.hexGrid.convertRowColToHexXY(row, col)
    val destCoors = mapModel.hexGrid.convertRowColToHexXY(dest._1, dest._2)

    val diff = Vector2D(destCoors.x, destCoors.y) - Vector2D(curCoors.x, curCoors.y)
    if(diff.magnitude == 0) diff else diff / diff.magnitude
  }

  private def minCostNeighbour(row: Long, col: Long): (Long, Long) = {
    val neighbours: List[(Long, Long)] = (row, col) :: neighbourhood.get(row, col).filter(n => {
      dijkstraGraph.contains(n)
    })

    val graphN = dijkstraGraph.filter(node => {
      neighbours.contains(node._1)
    })

    graphN.minBy(_._2)._1
  }

}
