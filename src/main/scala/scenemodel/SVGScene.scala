package scenemodel

import graphics.SVGBrowser.webEngine
class SVGScene {

  def isObstacle(x: Int, y: Int): Boolean = {

    if(webEngine != null) {
      val layer: String = webEngine.executeScript("readLayer("+x+","+y+")").toString
      layer == Layer.Obstacle
    } else {
      false
    }
  }
}
