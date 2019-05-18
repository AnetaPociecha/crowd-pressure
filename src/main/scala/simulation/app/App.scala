package simulation.app

import java.io.File

import javafx.scene.image.Image
import scalafx.animation.AnimationTimer
import scalafx.application
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.canvas.{Canvas, GraphicsContext}
import scalafx.scene.control.Button
import scalafx.scene.layout.{BorderPane, HBox}
import scalafx.scene.paint.Color
import scenemodel.SceneModel
import simulation.Config.agentSize
import simulation.agents.AgentsManager
import simulation.Config._
import scenemodel.SceneModelCreator
import scalafx.geometry.Insets

object App extends JFXApp {

  val manager: AgentsManager = new AgentsManager
  manager.init()

  var isStarted: Boolean = false
  val sceneModel: SceneModel = SceneModelCreator.instance()

  stage = new application.JFXApp.PrimaryStage() {
    title = "Crowd simulation"

    scene = new Scene() {
      val border: BorderPane = new BorderPane

      val canvas: Canvas = new Canvas(canvasWidth,canvasHeight)
      val gc: GraphicsContext = canvas.graphicsContext2D
      border.center = canvas

      val controlPane: BorderPane = new BorderPane

      val startButton: Button = new Button("Start/Stop")
      val resetButton: Button = new Button("Reset")

      startButton.setPrefWidth(120)
      startButton.setPrefHeight(30)
      resetButton.setPrefWidth(120)
      resetButton.setPrefHeight(30)

      val buttonsBox: HBox = new HBox {
        spacing = 15
        padding = Insets(8, 30, 8, 10)
        children = Seq(startButton, resetButton)
      }

      controlPane.right = buttonsBox
      border.bottom = controlPane
      root = border

      var lastTime = 0L

      render(gc)
      val timer = AnimationTimer { time =>
        if(lastTime != 0) {
          val interval = (time - lastTime) / 1e9

          manager.step(interval * 1.8)
          render(gc)
        }

        lastTime = time
        Thread sleep 100
      }
      canvas.requestFocus()

      startButton.setOnAction( _ => {
        if(isStarted) {
          timer.stop()
          lastTime = 0L
        }
        else {
          timer.start()
        }
        isStarted = !isStarted
      })

      resetButton.setOnAction( _ => {
        manager.agents.clear()
      })
    }
  }

  val img: Image = new Image("file:logo.png")
  stage.icons.add(img)



  def render(gc: GraphicsContext): Unit = {
    gc.fill = Color.White
    gc.fillRect(0, 0, canvasWidth, canvasHeight)

    gc.fill = Color.LightGray

    sceneModel.walls.foreach(w => {
      gc.fillRect(w.start.x, w.start.y, Math.abs(w.stop.x - w.start.x), Math.abs(w.stop.y - w.start.y))
    })

    gc.fill = manager.colors(0)
    gc.fillRect(110,590,180,10)

    gc.fill = manager.colors(1)
    gc.fillRect(510,590,180,10)

    gc.fill = manager.colors(2)
    gc.fillRect(790,210,10,180)

    manager.agents.foreach(a => {
      gc.fill = a.color
      gc.fillOval(a.position.x - agentRadius, a.position.y - agentRadius, agentSize, agentSize)
    })
  }

  stage.setResizable(false)
}
