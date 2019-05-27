package simulation

import graphics.SVGBrowser
import org.w3c.dom.html.HTMLObjectElement
import scalafx.scene.web.WebEngine
import simulation.shortestpathalgoritm.{MapModel, NavigationField}

import scala.collection.mutable.ArrayBuffer

case class Simulation(webEngine: WebEngine) {

  var navigationFields: ArrayBuffer[NavigationField] = ArrayBuffer()

  var mapModel: MapModel = _
  val tmpDestinations: ArrayBuffer[(Int, Int)] = ArrayBuffer()

  var storedWidth: Int = _
  var storedHeight: Int = _

  def adjustSize(): Unit = {
    println("adjust size")

    val jsMap: HTMLObjectElement = webEngine.executeScript("readMap()").asInstanceOf[HTMLObjectElement]
    val viewBoxAttr: String = jsMap.getContentDocument.getElementsByTagName("svg").item(0).getAttributes.getNamedItem("viewBox").getNodeValue
    val sizes: Array[String] = viewBoxAttr.split(" ")

    println("view Box: " + viewBoxAttr)

    val width: Int = sizes(2).toDouble.toInt + 1
    val height: Int = sizes(3).toDouble.toInt + 1

    println(s"map size: $width $height")

    SVGBrowser.setSize(width, height)

    storedWidth = width
    storedHeight = height

    println("adjust size completed")
  }

  def initMapModel(): Unit = {
    println("init map")

    val map: simulation.Map = simulation.Map(webEngine, storedWidth, storedHeight)

    mapModel = MapModel(map)
    mapModel.initGraph()

    println("init map completed")
  }

  def submitDestinations(): Unit = {
    mapModel.destinations ++= tmpDestinations
    tmpDestinations.clear()
    println("sumbit destinations completed " + mapModel.destinations)
  }

  def initNavigationFields(): Unit = {
    println("init navigation fields")

    val navFields = mapModel.destinations.map(d => {
      NavigationField(mapModel,d._1,d._2)
    })
    navFields.foreach(nav => nav.init())
    navigationFields = navFields

    println("init navigation fields completed")
  }
}
