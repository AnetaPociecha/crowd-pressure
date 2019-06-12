package simulation

import scalafx.scene.web.WebEngine

case class Map(webEngine: WebEngine, width: Int = 800, height: Int = 600) {

  def isObstacle(x: Int, y: Int): Boolean = {
    val layer: String = webEngine.executeScript(s"readLayer(${x.toString},${y.toString})").asInstanceOf[String]
    layer != "Allowed" && layer != "Targets"
  }

}
