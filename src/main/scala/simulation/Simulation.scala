package simulation

import graphics.SVGBrowser
import org.w3c.dom.html.HTMLObjectElement
import scalafx.animation.AnimationTimer
import scalafx.scene.canvas.{Canvas, GraphicsContext}
import scalafx.scene.paint.Color
import scalafx.scene.web.WebEngine
import simulation.shortestpathalgoritm.{MapModel, NavigationField}
import scala.collection.mutable.ArrayBuffer
import scala.util.Random
import config.Config.agentRadius

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

  def addDestination(x: Int, y: Int): Unit = {
    val rowCol = mapModel.hexGrid.convertXYToRowCol(x,y)
    if(!mapModel.isObstacle(rowCol.row, rowCol.col))
      tmpDestinations += ((x,y))
    println("destinations: " + tmpDestinations)
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

  /* ***************************************************************************** */

  @volatile var agents: ArrayBuffer[Agent] = ArrayBuffer()

  private def initAgent(): Unit = {

    val start: (Int, Int) = mapModel.destinations(Random.nextInt(mapModel.destinations.length))
    val nav: NavigationField = navigationFields(Random.nextInt(navigationFields.length))
    if(start._1 != nav.x || start._2 != nav.y)
      agents += new Agent(Vector2D(start._1,start._2),nav)
  }

  var timer: AnimationTimer = _

  def perform(canvas: Canvas): Unit = {

    if(timer == null) {
      val gc: GraphicsContext = canvas.graphicsContext2D

      initAgent()

      gc.fill = Color.Red

      var lastTime = 0L

      timer = AnimationTimer { time =>

        if(Random.nextInt(3) < 100) initAgent()

        gc.clearRect(0,0,storedWidth,storedHeight)

        if(lastTime != 0) {
          //        val interval = (time - lastTime) / 1e9

          agents.foreach(a => {
            gc.fillOval(a.position.x.toInt, a.position.y.toInt, agentRadius*2, agentRadius*2)
            a.step()
          })

          agents = agents.filter(a => !a.destinationReached())
        }
        lastTime = time
        Thread sleep 40
      }
      canvas.requestFocus()
    }

    timer.start()
  }

  def stop(): Unit = {
    if(timer != null) timer.stop()
  }

}
