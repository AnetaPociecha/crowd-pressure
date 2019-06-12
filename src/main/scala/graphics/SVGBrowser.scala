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
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.canvas.Canvas
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.scene.web._
import scalafx.stage.{FileChooser, Modality, Stage}
import simulation.Simulation

object SVGBrowser extends JFXApp {

  val webView: WebView = new WebView()
  val webEngine: WebEngine = webView.getEngine()
  var simulation: Simulation = Simulation(webEngine)

  var obstacle : String = ""
  var allowed : String = ""
  var targets : String = ""

//  var targetPosition = collection.mutable.Map[Int,Int]()
  var targetPosition: List[(Int,Int)] = List[(Int,Int)]()

  val connector = new JSConnector()

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
//          openLayerChooser()
          val uri: String = file.toURI.toString
          webEngine.executeScript("clearMap()")
          webEngine.executeScript("setupMap(\""+uri+"\")")
          webEngine.executeScript("setOnLoadCallback(function() {  app.handleLayers(getLayers().join(\"|\")) })") // app.printLayers(layers())
        }
      }
    }
  })

  var root: BorderPane = new BorderPane()
  var stack: StackPane = new StackPane()

  stack.getChildren.add(webView)
  root.center = stack

  def onLoadAction(): Unit = {
    openLayerChooser()
    addJSConnectorToJSWindow()

    simulation.adjustSize()
    simulation.initMapModel()
    print("ON Load action")
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

  def openLayerChooser() = {
    val newStage = new Stage()
    val comp = new VBox() {
      spacing = 10
      padding = Insets(30, 30, 30, 30)
      alignment = Pos.TopCenter
    }

    val stageScene = new Scene(comp)
    newStage.setAlwaysOnTop(true);
    newStage.initModality(Modality.WINDOW_MODAL)
    newStage.initOwner(rootScene.getWindow())
    newStage.setScene(stageScene)
    comp.getChildren().add(createDropdownLine("Allowed space", updateAllowed))
    comp.getChildren().add(createDropdownLine("Obstacles", updateObstacles))
    comp.getChildren().add(createDropdownLine("Targets", updateTargets))

    val submitButton = new Button("Submit") {
      margin = Insets(20, 0, 0, 0)
    }
    submitButton.setMinWidth(200)
    submitButton.onAction = new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent) {
        newStage.close()
        webEngine.executeScript("app.handleTargets(getTargets(\""+targets+"\").join(\"|\"))")
      }
    }

    comp.getChildren().add(submitButton)
    newStage.show()
  }
  def createDropdownLine(name : String, onChange : (String) => Unit) : HBox = {
    val comp = new HBox() {
      spacing = 10
      alignment = Pos.CenterRight
    }
    comp.getChildren().add(new Label(name))
    comp.getChildren().add(createDropdownTree("", onChange))

    return comp
  }

  def updateAllowed(name : String) : Unit = {
    this.allowed = name
  }

  def updateObstacles(name : String) : Unit = {
    this.obstacle = name
  }

  def updateTargets(name : String) : Unit = {
    this.targets = name
  }

//  def updateTargetPositions(targets : collection.mutable.Map[Int,Int]) : Unit = {
  def updateTargetPositions(targets : List[(Int,Int)]) : Unit = {
    this.targetPosition = targets
    for ((x,y) <- this.targetPosition) {
      simulation.addDestination(x,y)
//            println("x: "+ x + " y: " + y)
    }
  }

  def createDropdownTree(title: String, onChange : (String) => Unit) : MenuButton = {

    val menuItem = new MenuItem("hello")
    val content = new collection.mutable.MutableList[MenuItem]()

    val button = new MenuButton(title)

    for (layer <- connector.layers) {
      val item = new MenuItem(layer)
      item.setOnAction(() => {
        button.text = layer
        onChange(layer)
      })
      content += item
    }

    button.items = content
    button.setMinWidth(140)
    return button
  }

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

    webView.setMinSize(width,height + 10)
    webView.setMaxSize(width,height + 10)

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
    window.setMember("app", connector)
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