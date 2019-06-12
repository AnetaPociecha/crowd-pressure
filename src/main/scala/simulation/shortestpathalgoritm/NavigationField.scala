package simulation.shortestpathalgoritm

import simulation.Vector2D
import simulation.hexgrid.HexGrid

import scala.collection.mutable
import config.Config.CellSize

case class NavigationField(mapModel: MapModel, xStop: Long, yStop: Long, xStart: Long, yStart: Long) {

  private var directions: mutable.Map[(Long, Long), Vector2D] = mutable.Map()

  def init(): Runnable = { () =>

    val dijkstra = DikjstraModel(mapModel, xStop, yStop, xStart, yStart)
    val dg = dijkstra.createGraph()

    val direction = DesiredDirectionModel(dg, mapModel)
    val graph: mutable.Map[(Long, Long), Vector2D] = direction.createGraph()
    directions = graph
  }

  val hexGrid: HexGrid = HexGrid(CellSize)

  def direction(x: Long, y: Long): Vector2D = {
    val rowCol = hexGrid.convertXYToRowCol(x,y)
    directions(rowCol.row, rowCol.col)
  }
}
