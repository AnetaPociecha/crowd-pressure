package simulation.app

import javafx.event.EventHandler
import scalafx.animation.AnimationTimer
import scalafx.application
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.canvas.{Canvas, GraphicsContext}
import scalafx.scene.layout.BorderPane
import scalafx.scene.paint.Color
import scalafx.stage.WindowEvent
import scenemodel.SceneModel
import simulation.Config.agentSize
import simulation.agents.AgentsManager
import simulation.Config._
import scenemodel.SceneModelCreator

object App extends JFXApp {

  val manager: AgentsManager = new AgentsManager
  manager.init()

  stage = new application.JFXApp.PrimaryStage() {
    title = "Tuptusie"

    scene = new Scene(canvasWidth, canvasHeight) {
      val border = new BorderPane
      val canvas = new Canvas(canvasWidth,canvasHeight)
      val gc: GraphicsContext = canvas.graphicsContext2D

      border.center = canvas
      root = border

      var lastTime = 0L

      val timer = AnimationTimer { time =>
        if(lastTime != 0) {
          val interval = (time - lastTime) / 1e9

          manager.step(interval)
          render(gc)
        }

        lastTime = time
        Thread sleep 200
      }

      timer.start()
      canvas.requestFocus()
    }
  }

  val sceneModel: SceneModel = SceneModelCreator.create()

  def render(gc: GraphicsContext): Unit = {
    gc.fill = Color.White
    gc.fillRect(0, 0, canvasWidth, canvasHeight)
    gc.fill = Color.Gray

    sceneModel.walls.foreach(w => {
      gc.fillRect(w.start.x, w.start.y,w.stop.x, w.stop.y)
    })

    gc.fill = Color.MediumBlue
    manager.agents.foreach(a => gc.fillOval(a.position.x - agentRadius, a.position.y - agentRadius, agentSize, agentSize))
  }
}
