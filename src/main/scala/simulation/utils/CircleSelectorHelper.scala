package simulation.utils

case class CircleSelectorHelper(center: Vector2D, sectorStart: Vector2D, sectorEnd: Vector2D, radius: Double) {

  def isInsideSector(point: Vector2D): Boolean = {

    val relPoint: Vector2D = Vector2D(point.x - center.x, point.y - center.y)

    val inside = !areClockwise(sectorStart, relPoint) && areClockwise(sectorEnd, relPoint) && isWithinRadius(relPoint)
    inside
  }

  def areClockwise(rightSelectorVector: Vector2D, relPoint: Vector2D): Boolean = {
    (-rightSelectorVector.x*relPoint.y + rightSelectorVector.y*relPoint.x) > 0
  }

  def isWithinRadius(relPoint: Vector2D): Boolean = {
    val radiusSquared: Double = radius * radius
    relPoint.x*relPoint.x + relPoint.y*relPoint.y <= radiusSquared
  }

}
