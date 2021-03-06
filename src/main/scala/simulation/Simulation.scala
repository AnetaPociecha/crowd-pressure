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
import config.Config.{AgentRadius, Delay, Density, TimeoutSec}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{Duration, SECONDS}
import scala.concurrent.{Await, Future, blocking}

case class Simulation(webEngine: WebEngine) {

  var navigationFields: ArrayBuffer[NavigationField] = ArrayBuffer()

  var mapModel: MapModel = _
  val tmpDestinations: ArrayBuffer[(Int, Int)] = ArrayBuffer()

  var storedWidth: Int = _
  var storedHeight: Int = _

  def adjustSize(): Unit = {

    val jsMap: HTMLObjectElement = webEngine.executeScript("readMap()").asInstanceOf[HTMLObjectElement]
    val viewBoxAttr: String = jsMap.getContentDocument.getElementsByTagName("svg").item(0).getAttributes.getNamedItem("viewBox").getNodeValue
    val sizes: Array[String] = viewBoxAttr.split(" ")

    val width: Int = sizes(2).toDouble.toInt + 1
    val height: Int = sizes(3).toDouble.toInt + 1

    SVGBrowser.setSize(width, height)

    storedWidth = width
    storedHeight = height
  }

  def initMapModel(): Unit = {

    val map: simulation.Map = simulation.Map(webEngine, storedWidth, storedHeight)

    mapModel = MapModel(map)
    mapModel.initGraph()
  }

  def addDestination(x: Int, y: Int): Unit = {
    tmpDestinations += ((x,y))
  }

  def submitDestinations(): Unit = {
    mapModel.destinations.clear()
    mapModel.destinations ++= tmpDestinations
    tmpDestinations.clear()
  }


  def initNavigationFields(): Unit = {

    val navFields = mapModel.destinations.flatMap(d => {
      mapModel.destinations.filter(dd => dd != d).map(dd => {
        NavigationField(mapModel, dd._1, dd._2, d._1, d._2)
      })
    })

    for(nav <- navFields) {
      Await.ready(Future { blocking(nav.init().run())
      }, Duration(TimeoutSec, SECONDS))
    }

    navigationFields = navFields
  }

  /* ***************************************************************************** */

  @volatile var agents: ArrayBuffer[Agent] = ArrayBuffer()

  private def initAgent(): Unit = {

    val nav: NavigationField = navigationFields(Random.nextInt(navigationFields.length))

    agents += new Agent(Vector2D(nav.xStart, nav.yStart), nav)
  }

  var timer: AnimationTimer = _
  var isRunning: Boolean = false

  def perform(canvas: Canvas): Unit = {

      val gc: GraphicsContext = canvas.graphicsContext2D

      initAgent()

      gc.fill = Color.Red

      var lastTime = 0L

      timer = AnimationTimer { time =>

        if(Random.nextInt(Density) < 100) initAgent()

        gc.clearRect(0,0,storedWidth,storedHeight)

        if(lastTime != 0) {
          val interval = (time - lastTime) / 1e9

          agents.foreach(a => {
            gc.fillOval(a.position.x.toInt, a.position.y.toInt, AgentRadius*2, AgentRadius*2)
            a.step(interval)
          })

          agents = agents.filter(a => !a.destinationReached())
        }
        lastTime = time
        Thread sleep Delay
      }
      canvas.requestFocus()

    timer.start()
    isRunning = true
  }


  def clear(canvas: Canvas): Unit = {
    timer.stop()
    agents.clear()
    if(canvas != null) {
      val gc: GraphicsContext = canvas.graphicsContext2D
      gc.clearRect(0,0,storedWidth,storedHeight)
    }
    navigationFields = ArrayBuffer()
    isRunning = false
  }

}
