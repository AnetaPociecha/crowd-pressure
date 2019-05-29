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
          webEngine.executeScript("setOnLoadCallback(function() {  app.handleLayers(getLayers().join(\"|\")) })") // app.printLayers(layers())
          openLayerChooser()
        }
      }
    }
  })

  var root: BorderPane = new BorderPane()
  val stack: StackPane = new StackPane()

  stack.getChildren.add(webView)
  root.center = stack

  def onLoadAction(): Unit = {
    simulation.adjustSize()
    simulation.initMapModel()
    print("ON Load action")
    addJSConnectorToJSWindow()
  }

  val destinationsButton = new Button()
  destinationsButton.setText("Submit Destinations")
  destinationsButton.setOnAction(() => {
    simulation.submitDestinations()
    simulation.initNavigationFields()
    addJSConnectorToJSWindow()
  })

  val startButton = new Button()
  startButton.setText("Start")
  startButton.setOnAction(() => {
    start()
  })

//  val stopButton = new Button()
//  stopButton.setText("Stop")
//  stopButton.setOnAction(() => {
//    simulation.stop()
//  })

  uploadMapButton.setPrefWidth(120)
  uploadMapButton.setPrefHeight(25)
  destinationsButton.setPrefWidth(150)
  destinationsButton.setPrefHeight(25)
  startButton.setPrefWidth(120)
  startButton.setPrefHeight(25)
//  stopButton.setPrefWidth(120)
//  stopButton.setPrefHeight(25)

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
    comp.getChildren().add(createDropdownLine("Allowed space"))
    comp.getChildren().add(createDropdownLine("Obstacles"))
    comp.getChildren().add(createDropdownLine("Targets"))

    val submitButton = new Button("Submit") {
      margin = Insets(20, 0, 0, 0)
    }
    submitButton.setMinWidth(200)
    submitButton.onAction = new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent) {
        newStage.close()
      }
    }

    comp.getChildren().add(submitButton)
    newStage.show()
  }

  def createDropdownLine(name : String) : HBox = {
    val comp = new HBox() {
      spacing = 10
      alignment = Pos.CenterRight
    }
    comp.getChildren().add(new Label(name))
    comp.getChildren().add(createDropdownTree(""))

    return comp
  }

  def createDropdownTree(title: String) : MenuButton = {
    val rootItem = new TreeItem[String]("Root")
    rootItem.setExpanded(true)
    for (i<-1 to 25) {
      val item = new TreeItem[String]("Message " + i)

      rootItem.getChildren.add(item)
    }
    val tree = new TreeView[String](rootItem)
    val customMenuItem = new CustomMenuItem(tree)
    customMenuItem.setHideOnClick(false)
    val button = new MenuButton(title) {
      items = List(customMenuItem)
    }
    button.setMinWidth(140)
    return button
  }

  val box: HBox = new HBox {
    spacing = 15
    padding = Insets(7, 25, 6, 25)
    children = Seq(uploadMapButton, destinationsButton, startButton) // stopButton
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

  def start(): Unit = {
    val canvas: Canvas = new Canvas(simulation.storedWidth, simulation.storedHeight)
    stack.getChildren.add(canvas)
    simulation.perform(canvas)
  }

}