package simulation.view
import simulation.XY

class SampleView extends View {
  override def isObstacle(xy: XY): Boolean = {
    false
  }
}
