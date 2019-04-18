package graphics

import netscape.javascript.JSObject
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.scene.paint.Color
import scalafx.scene.web._

object SVGBrowser extends JFXApp {

  val browser = new WebView {
    hgrow = Priority.Always
    vgrow = Priority.Always
  }

  val engine = browser.getEngine()
  engine.load("file:///Users/bartek/Studio%20projektowe/map.html")

  val window : JSObject = engine.executeScript("window").asInstanceOf[JSObject]
  window.setMember("app", new JSConnector())

  val txfUrl = new TextField {
    text = engine.location.value
    hgrow = Priority.Always
    vgrow = Priority.Never
  }
  txfUrl.onAction = handle {engine.load(txfUrl.text.get)}

  stage = new PrimaryStage {
    title = "SVG Browser"
    width = 800
    height = 600
    scene = new Scene {
      fill = Color.LightGray
      root = new BorderPane {
        hgrow = Priority.Always
        vgrow = Priority.Always
        top = txfUrl
        center = browser
      }
    }
  }

}