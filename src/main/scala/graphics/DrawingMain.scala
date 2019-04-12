package graphics

import scalafx.Includes._
import scalafx.application
import scalafx.application.JFXApp
import scalafx.geometry.Side
import scalafx.scene.Scene
import scalafx.scene.control.TabPane.TabClosingPolicy
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.scene.paint.Color
import scalafx.scene.image._
import scalafx.scene.shape.Rectangle
import java.io.{ File, FileInputStream }
import java.util.Properties

object DrawingMain extends JFXApp {

  stage = new application.JFXApp.PrimaryStage {
    title = "Symulacja tłumu"
    scene = new Scene(750,600) {
      val tabPane = new TabPane

      tabPane.setSide(Side.Right)
      tabPane.setRotateGraphic(false)
      tabPane.setTabClosingPolicy(TabClosingPolicy.Unavailable)

      val tab1 = new Tab
      val lab1 = new Label("TAURON Arena")
      tab1.setGraphic(lab1)

      val url = new File("C:/Users/user/Desktop/sp/arena.jpg").toURI().toString
      val img = new Image(url, 600, 600, false, true)

      val node = img.pixelReader match {
        case None => new Label("Cannot read pixels")
        case Some (pr) => new ImageView(img)
      }

      tab1.content = node

      val tab2 = new Tab
      val lab2 = new Label("Kraków Główny")
      tab2.setGraphic(lab2)

      tabPane.setTabMinHeight(150)
      tabPane.setTabMinWidth(30)

      tabPane.tabs = List(tab1, tab2)
      root = tabPane
    }
  }
}
