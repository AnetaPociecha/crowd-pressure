package socialforcemodel.utils

case class Vector2D(x: Double, y: Double) {
  def +(v: Vector2D) = Vector2D(x+v.x, y+v.y)
  def -(v: Vector2D) = Vector2D(x-v.x, y-v.y)
  def *(c: Double) = Vector2D(x*c, y*c)
  def /(c: Double) = Vector2D(x/c, y/c)
  def magnitude: Double = Math.sqrt(x*x+y*y)
}
