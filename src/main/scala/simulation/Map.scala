package simulation

import scalafx.scene.web.WebEngine

case class Map(webEngine: WebEngine, width: Int = 800, height: Int = 600) {

  def isObstacle(x: Int, y: Int): Boolean = {
    val layer: String = webEngine.executeScript(s"readLayer(${x.toString},${y.toString})").asInstanceOf[String]
    println(s"map reads layer for $x $y: "+layer)
    layer != "Allowed"
  }

  val destinations: Array[(Int, Int)] = Array((300,300), (350, 300))

}
