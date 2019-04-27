package simulation.shortestpath

import scenemodel.{SceneModel, SceneModelCreator}
import simulation.shortestpath.hexgrid.{HexGrid, Neighborhood, HexRowCol}
import simulation.utils.Vector2D
import simulation.Config._

import scala.collection.mutable

case class Navigation(cellSize: Int) {

  val scene: SceneModel = SceneModelCreator.create()
  val grid: HexGrid = HexGrid(cellSize)

  def createGraph(): Array[Array[Boolean]] = {

    val X: Int = (canvasWidth * 2 / Math.sqrt(3) / cellSize).toInt + 2
    val Y: Int = (canvasHeight / cellSize / 1.5). toInt + 2

    val graph: Array[Array[Boolean]] = Array.fill(Y)(Array.fill(X)(true))

    for(i <- 0 until X; j <- 0 until Y) {
      val coords = grid.convertRowColToHexXY(HexRowCol(j,i))
      val obs = scene.isObstacle(coords.x.toInt, coords.y.toInt)
      if(obs) graph(j)(i) = false
    }
    graph
  }

  def desiredDirections(goal: (Int, Int)): mutable.Map[(Int, Int), Vector2D] = {

    val graph = createGraph()
    val dijkstra = Dijkstra(graph)

    val costsMap = dijkstra.costsMap(goal)

    directions(costsMap, graph)
  }

  def directions(costsMap: mutable.Map[(Int, Int), Int], graph: Array[Array[Boolean]]): mutable.Map[(Int, Int), Vector2D] = {

    val dirMap: mutable.Map[(Int, Int), Vector2D] = mutable.Map[(Int, Int), Vector2D]()
    val neighbourhood = Neighborhood(graph)

    for (cell <- costsMap) {
      val neighbours = neighbourhood.neighbours(cell._1).filter(n => costsMap.contains(n))

      var minN = neighbours(0)
      for (n <- neighbours) {
        if(costsMap(n) < costsMap(minN)) minN = n
      }

      val cur = Vector2D(cell._1._1, cell._1._2)
      val next = Vector2D(minN._1, minN._2)

      dirMap(cell._1) = (next - cur) / (next - cur).magnitude
    }
    dirMap
  }

}
