package simulation.shortestpathalgoritm

import simulation.Vector2D
import simulation.hexgrid.HexGrid

import scala.collection.mutable
import config.Config.CellSize

case class NavigationField(mapModel: MapModel, x: Long, y: Long) {

  private var directions: mutable.Map[(Long, Long), Vector2D] = mutable.Map()

  def init(): Unit = {
    println("init navigation field")

    val dijkstra = DikjstraModel(mapModel, x, y)
    val dg = dijkstra.createGraph()

    val direction = DesiredDirectionModel(dg, mapModel)
    val graph: mutable.Map[(Long, Long), Vector2D] = direction.createGraph()
    println(graph)
    directions = graph

    println("init navigation field completed")
  }

  def direction(x: Long, y: Long): Vector2D = {
    val rowCol = HexGrid(CellSize).convertXYToRowCol(x,y)
    directions(rowCol.row, rowCol.col)
  }
}
