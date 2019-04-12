package simulation.logic

import scalafx.animation.AnimationTimer
import scalafx.application
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.canvas.Canvas
import scalafx.scene.layout.BorderPane
import scalafx.scene.paint.Color
import scalafx.scene.canvas.GraphicsContext

import simulation.Config._

object SocialForceModelAnimation extends JFXApp {
  private val manager = new AgentsManager(0,sceneWidth,0,sceneWidth)
  manager.init()

  stage = new application.JFXApp.PrimaryStage() {
    title = "Social force model"

    scene = new Scene(sceneWidth, sceneHeight) {
      val border = new BorderPane
      val canvas = new Canvas(sceneWidth,sceneHeight)
      val gc: GraphicsContext = canvas.graphicsContext2D

      border.center = canvas
      root = border  

      var lastTime = 0L

      val timer = AnimationTimer { time =>
        if(lastTime != 0) {
          val interval = (time - lastTime) / 1e9

          gc.fill = Color.White
          gc.fillRect(0,0, canvas.width(), canvas.height())
          gc.fill = Color.MediumBlue

          manager.step(gc,interval)
        }

        lastTime = time
        Thread sleep 100 // fix
      }

      timer.start()
      canvas.requestFocus()
    }
  }
}
