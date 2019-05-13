package graphics

import javafx.beans.value.ChangeListener
import javafx.concurrent.Worker
import netscape.javascript.JSObject
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.scene.web._
import java.io.File

import scalafx.scene.canvas.{Canvas, GraphicsContext}
import scalafx.scene.paint.Color


object SVGBrowser extends JFXApp {

  val webView: WebView = new WebView()
  val webEngine: WebEngine = webView.getEngine()
  webEngine.load(getClass.getResource("map.html").toString)

  val customMapButton: Button = new Button()
  customMapButton.setText("Upload custom map")

  val startButton: Button = new Button()
  startButton.setText("Start/Stop")

  val resetButton: Button = new Button()
  resetButton.setText("Reset")

  val simulationControlPane = new BorderPane()
  simulationControlPane.left = startButton
  simulationControlPane.right = resetButton

  val userPane = new BorderPane()
  userPane.left = customMapButton
  userPane.right = simulationControlPane

  val canvas: Canvas = new Canvas(800,500) // TO DO set right size
  val gc: GraphicsContext = canvas.graphicsContext2D

  gc.fill = Color.Red
  gc.fillRect(10,10,10,10)
  gc.fillRect(60,100,10,10)

  val simulationPane: StackPane = new StackPane()
  simulationPane.getChildren.add(webView)
  simulationPane.getChildren.add(canvas)

  val root: VBox = new VBox()

  root.getChildren.add(simulationPane)
  root.getChildren.add(userPane)


  val rootScene = new Scene(root, 800, 600)

  stage = new PrimaryStage {
    title = "SVG Browser"
    width = 800
    height = 630 // TO DO size
    scene = rootScene

    webEngine.getLoadWorker.stateProperty().addListener(new ChangeListener[Worker.State]() {
      def changed(observable: _root_.javafx.beans.value.ObservableValue[_ <: _root_.javafx.concurrent.Worker.State], oldValue: _root_.javafx.concurrent.Worker.State, newValue: _root_.javafx.concurrent.Worker.State): Unit = {
        if (newValue == Worker.State.SUCCEEDED) {
          val window: JSObject = webEngine.executeScript("window").asInstanceOf[JSObject]
          window.setMember("app", new JSConnector())
          val path: String = System.getProperty("user.dir").replace("\\","/")+"/map.svg"
          loadMap(new File(path))
        }
      }
    })
  }

  customMapButton.setOnAction(() => {
    if(webEngine != null) {
      val file: File = Popup.chooseFile(stage)
      loadMap(file)
    }
  })

  def loadMap(file: File): Unit = {
    if (webEngine != null && file != null && file.exists()) {
      val uri: String = file.toURI.toString
      webEngine.executeScript("setupMap(\""+uri+"\")")
      webEngine.executeScript("setOnLoadCallback(function() { app.handleLayers(getLayers().join(\"|\")) })")
    }
  }

}