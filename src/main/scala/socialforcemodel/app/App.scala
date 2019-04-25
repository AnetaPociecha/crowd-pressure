package socialforcemodel.app

import scalafx.animation.AnimationTimer
import scalafx.application
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.canvas.{Canvas, GraphicsContext}
import scalafx.scene.layout.BorderPane
import scalafx.scene.paint.Color
import scenemodel.SceneModel
import socialforcemodel.Config.agentSize
import socialforcemodel.agents.AgentsManager
import socialforcemodel.path.SimplePathStrategy
import socialforcemodel.Config._
import scenemodel.SceneModelCreator

object  App extends JFXApp {

  val manager: AgentsManager = new AgentsManager(new SimplePathStrategy)
  manager.init()

  stage = new application.JFXApp.PrimaryStage() {
    title = "Social force model"

    scene = new Scene(800,600) {
      val border = new BorderPane
      val canvas = new Canvas(800,600)
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
        Thread sleep 200 // fix
      }

      timer.start()
      canvas.requestFocus()
    }
  }

  val sceneModel: SceneModel = SceneModelCreator.create()

  def render(gc: GraphicsContext): Unit = {
    gc.fill = Color.White
    gc.fillRect(0, 0, 800, 600)
    gc.fill = Color.Gray

    sceneModel.walls.foreach(w => {
      gc.fillRect(w.start.x, w.start.y,w.stop.x, w.stop.y)
    })

    gc.fill = Color.MediumBlue
    manager.agents.foreach(a => gc.fillOval(a.position.x - agentRadius, a.position.y - agentRadius, agentSize, agentSize))
  }
}
