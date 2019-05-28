package graphics

import java.io.File

import javafx.beans.value.ChangeListener
import javafx.concurrent.Worker
import javafx.event.{ActionEvent, EventHandler}
import netscape.javascript.JSObject
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.canvas.{Canvas, GraphicsContext}
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.scene.paint.Color
import scalafx.scene.web._
import scalafx.stage.FileChooser
import simulation.Simulation

object SVGBrowser extends JFXApp {

  val webView: WebView = new WebView()
  val webEngine: WebEngine = webView.getEngine()
  var simulation: Simulation = Simulation(webEngine)

  webEngine.load(getClass.getResource("map.html").toString)

  val button = new Button()
  button.setText("Load map")
  button.setOnAction(new EventHandler[ActionEvent] {
    override def handle(event: ActionEvent): Unit = {
      if (webEngine != null) {
        val fileChooser = new FileChooser()
        fileChooser.setTitle("Load map file")
        val file: File = fileChooser.showOpenDialog(stage)
        if (file != null) {
          val uri: String = file.toURI.toString
          webEngine.executeScript("setupMap(\""+uri+"\")")
          webEngine.executeScript("setOnLoadCallback(function() { app.handleLayers(getLayers().join(\"|\")) })")
        }
      }
    }
  })

  var root: BorderPane = new BorderPane()
  val stack: StackPane = new StackPane()

  val box: VBox = new VBox()

  stack.getChildren.add(webView)
  root.center = stack
  //

  val button1 = new Button()
  button1.setText("Adjust map size")
  button1.setOnAction(() => {
    simulation.adjustSize()
    addJSConnectorToJSWindow()
  })

  val button2 = new Button()
  button2.setText("Init map model")
  button2.setOnAction(() => {
    simulation.initMapModel()
    addJSConnectorToJSWindow()
  })

  val button3 = new Button()
  button3.setText("Submit destinations")
  button3.setOnAction(() => {
    simulation.submitDestinations()
    addJSConnectorToJSWindow()
  })

  val button4 = new Button()
  button4.setText("Init navigation fields")
  button4.setOnAction(() => {
    simulation.initNavigationFields()
    addJSConnectorToJSWindow()
  })

  val button5 = new Button()
  button5.setText("Start")
  button5.setOnAction(() => {
    start()
  })
  //

  box.getChildren.add(button)
  box.getChildren.add(button1) //
  box.getChildren.add(button2) //
  box.getChildren.add(button3) //
  box.getChildren.add(button4) //
  box.getChildren.add(button5) //

  root.bottom = box

  val rootScene = new Scene(root, 800, 600)

  stage = new PrimaryStage {
    title = "SVG Browser"
    scene = rootScene

    webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener[Worker.State]() {
      def changed(observable: _root_.javafx.beans.value.ObservableValue[_ <: _root_.javafx.concurrent.Worker.State], oldValue: _root_.javafx.concurrent.Worker.State, newValue: _root_.javafx.concurrent.Worker.State): Unit = {
        if (newValue == Worker.State.SUCCEEDED) {
          addJSConnectorToJSWindow()
        }
      }
    })
  }

  def setSize(width: Int, height: Int): Unit = {

    println("set window size")

    webView.setMinSize(width,height)
    webView.setMaxSize(width,height)

    root = new BorderPane()
    root.bottom = box
    root.center = stack

    val sc = new Scene(root)
    stage.scene = sc

    addJSConnectorToJSWindow()
  }

  def addJSConnectorToJSWindow(): Unit = {
    val window: JSObject = webEngine.executeScript("window").asInstanceOf[JSObject]
    window.setMember("app", new JSConnector())
  }



  /* ************************************************************ */

  def start(): Unit = {
    val canvas: Canvas = new Canvas(simulation.storedWidth, simulation.storedHeight)

    stack.getChildren.add(canvas)

    simulation.perform(canvas)
  }

}