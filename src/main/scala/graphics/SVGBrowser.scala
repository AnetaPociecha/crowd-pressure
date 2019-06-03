package graphics

import java.io.File

import javafx.beans.value.ChangeListener
import javafx.scene.image.Image
import javafx.concurrent.Worker
import javafx.event.{ActionEvent, EventHandler}
import netscape.javascript.JSObject
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.canvas.Canvas
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.scene.web._
import scalafx.stage.FileChooser
import simulation.Simulation

object SVGBrowser extends JFXApp {

  val webView: WebView = new WebView()
  val webEngine: WebEngine = webView.getEngine()
  var simulation: Simulation = Simulation(webEngine)

  webEngine.load(getClass.getResource("map.html").toString)

  val uploadMapButton = new Button()
  uploadMapButton.setText("Upload Map")
  uploadMapButton.setOnAction(new EventHandler[ActionEvent] {
    override def handle(event: ActionEvent): Unit = {
      if (webEngine != null) {
        val fileChooser = new FileChooser()
        fileChooser.setTitle("Upload Map File")
        val extFilter: FileChooser.ExtensionFilter = new FileChooser.ExtensionFilter("SVG files (*.svg)", "*.svg")
        fileChooser.getExtensionFilters.add(extFilter)

        val file: File = fileChooser.showOpenDialog(stage)
        if (file != null) {
          val uri: String = file.toURI.toString
          webEngine.executeScript("clearMap()")
          webEngine.executeScript("setupMap(\""+uri+"\")")
          webEngine.executeScript("setOnLoadCallback(function() { app.handleLayers(getLayers().join(\"|\")) })")
        }
      }
    }
  })

  var root: BorderPane = new BorderPane()
  var stack: StackPane = new StackPane()

  stack.getChildren.add(webView)
  root.center = stack

  def onLoadAction(): Unit = {
    addJSConnectorToJSWindow()

    simulation.adjustSize()
    simulation.initMapModel()
    addJSConnectorToJSWindow()
    destinationsButton.disable = false
    startButton.disable = true
    stack.getChildren.remove(1)
    simulation.clear(canvas)
    addJSConnectorToJSWindow()

  }

  val destinationsButton = new Button()
  destinationsButton.setText("Submit Destinations")
  destinationsButton.disable = true

  destinationsButton.setOnAction(() => {
    if(!simulation.isRunning) {
      simulation.submitDestinations()
      simulation.initNavigationFields()
      addJSConnectorToJSWindow()
    }
    startButton.disable = false
    destinationsButton.disable = true
  })

  val startButton = new Button()
  startButton.setText("Start")
  startButton.disable = true
  startButton.setOnAction(() => {
    start()
    startButton.disable = true
  })

  uploadMapButton.setPrefWidth(120)
  uploadMapButton.setPrefHeight(25)
  destinationsButton.setPrefWidth(150)
  destinationsButton.setPrefHeight(25)
  startButton.setPrefWidth(120)
  startButton.setPrefHeight(25)

  val box: HBox = new HBox {
    spacing = 15
    padding = Insets(7, 25, 6, 25)
    children = Seq(uploadMapButton, destinationsButton, startButton)
  }
  root.bottom = box

  val rootScene = new Scene(root, 800, 600)

  stage = new PrimaryStage {
    title = "Crowd Simulation"
    scene = rootScene

    webEngine.getLoadWorker.stateProperty().addListener(new ChangeListener[Worker.State]() {
      def changed(observable: _root_.javafx.beans.value.ObservableValue[_ <: _root_.javafx.concurrent.Worker.State], oldValue: _root_.javafx.concurrent.Worker.State, newValue: _root_.javafx.concurrent.Worker.State): Unit = {
        if (newValue == Worker.State.SUCCEEDED) {
          addJSConnectorToJSWindow()
        }
      }
    })
  }

  val img: Image = new Image("file:logo.png")
  stage.icons.add(img)

  stage.setResizable(false)

  def setSize(width: Int, height: Int): Unit = {
    stage.setResizable(true)

    println("set window size")

    webView.setMinSize(width,height)
    webView.setMaxSize(width,height)

    root = new BorderPane()
    root.bottom = box
    root.center = stack

    val sc = new Scene(root)
    stage.scene = sc

    addJSConnectorToJSWindow()

    stage.setResizable(false)
  }

  def addJSConnectorToJSWindow(): Unit = {
    val window: JSObject = webEngine.executeScript("window").asInstanceOf[JSObject]
    window.setMember("app", new JSConnector())
  }

  var canvas: Canvas = _

  def start(): Unit = {
    if(!stack.children.contains(canvas)) {
      canvas = new Canvas(simulation.storedWidth, simulation.storedHeight)
      stack.getChildren.add(canvas)
    }
    if(!simulation.isRunning) simulation.perform(canvas)
  }

}