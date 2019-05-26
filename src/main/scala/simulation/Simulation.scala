package simulation

import scalafx.scene.web.WebEngine
import simulation.shortestpathalgoritm.{MapModel, NavigationField}

case class Simulation(webEngine: WebEngine) {

  var navigationFields: Array[NavigationField] = Array()

  def init(): Unit = {
    val map: simulation.Map = simulation.Map(webEngine)

    println("init simulation")

    val mapModel: MapModel = MapModel(map)
    mapModel.initGraph()

    val navFields = map.destinations.map(d => {
      NavigationField(mapModel,d._1,d._2)
    })
    navFields.foreach(nav => nav.init())
    navigationFields = navFields
    println("init simulation completed")
  }
}
