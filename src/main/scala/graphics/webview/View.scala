package graphics.webview

import scalafx.application
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.canvas.Canvas
import scalafx.scene.paint.Color
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.control.Button
import scalafx.scene.layout.BorderPane
import scalafx.scene.web.{WebEngine, WebView}


object View extends JFXApp{
  stage = new application.JFXApp.PrimaryStage() {
    title = "WebView"
    scene = new Scene(600, 600) {

      val borderPane: BorderPane = new BorderPane

      val view = new WebView()
      val engine: WebEngine = view.engine

      borderPane.center = view

      val arenaButton: Button = new Button("Arena")
      arenaButton.onAction = _ =>
        engine.load("file:///C:/Users/user/Desktop/sp/arena.jpg")


      val stationButton: Button = new Button("Station")
      stationButton.onAction = _ =>
        engine.load("file:///C:/Users/user/Desktop/sp/dworzec.jpg")

      val jsButton = new Button("js script")
      jsButton.onAction = _ =>
        engine.executeScript("window.location = \"https://www.youtube.com/\";")


      val innerPane: BorderPane = new BorderPane
      innerPane.left = arenaButton
      innerPane.right = stationButton
      innerPane.center = jsButton

      borderPane.bottom = innerPane

      root = borderPane

    }
  }
}
