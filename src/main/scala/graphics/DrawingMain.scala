package graphics

import scalafx.Includes._
import scalafx.application
import scalafx.application.JFXApp
import scalafx.geometry.Side
import scalafx.scene.Scene
import scalafx.scene.control.TabPane.TabClosingPolicy
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.scene.paint.Color
import scalafx.scene.image._
import scalafx.scene.shape.Rectangle
import java.io.{File, FileInputStream}
import java.util.Properties

import scalafx.scene.web.{WebEngine, WebView}

object DrawingMain extends JFXApp {

  stage = new application.JFXApp.PrimaryStage {
    title = "Symulacja tłumu"
    scene = new Scene(750,600) {
      val tabPane = new TabPane

      tabPane.setSide(Side.Right)
      tabPane.setRotateGraphic(false)
      tabPane.setTabClosingPolicy(TabClosingPolicy.Unavailable)

      val arenaTab = new Tab
      val arenaLabel = new Label("TAURON Arena")
      arenaTab.setGraphic(arenaLabel)

      val arenaView = new WebView()
      val arenaEngine: WebEngine = arenaView.engine
      arenaEngine.load("file:///C:/Users/user/Desktop/sp/arena.jpg")

      arenaTab.content = arenaView


      val stationTab = new Tab
      val stationLabel = new Label("Kraków Główny")
      stationTab.setGraphic(stationLabel)

      val stationView = new WebView()
      val stationEngine: WebEngine = stationView.engine
      stationEngine.load("file:///C:/Users/user/Desktop/sp/dworzec.jpg")

      stationTab.content = stationView

      tabPane.setTabMinHeight(150)
      tabPane.setTabMinWidth(30)

      tabPane.tabs = List(arenaTab, stationTab)
      root = tabPane
    }
  }
}
