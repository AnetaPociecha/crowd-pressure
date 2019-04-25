package socialforcemodel.utils

case class PointWallDirCalc(p: Vector2D) {

  def direction(a: Vector2D, b: Vector2D): Option[Vector2D] = {
    val ba = b - a
    val d = ba / ba.magnitude
    val x = a + d  * dot(p-a,d)
    val v = p - x

    val smallX = if (a.x < b.x) a.x else b.x
    val smallY = if (a.y < b.y) a.y else b.y

    val bigX = if(a.x >= b.x) a.x else b.x
    val bigY = if(a.y >= b.y) a.y else b.y

    val tol = 8

    if( x.x < smallX - tol || x.x > bigX + tol || x.y < smallY - tol || x.y > bigY + tol)
      Option.empty
    else
      Option(v)
  }

  def dot(v1: Vector2D, v2: Vector2D): Double = {
    v1.x * v2.x + v1.y * v2.y
  }


  def distance(start: Vector2D, stop: Vector2D): Double = {
    val A = p.x - start.x

    val B = p.y - start.y
    val C = stop.x - start.x

    val D = stop.y - start.y
    val E = -D

    val F = C
    val dot = A * E + B * F
    val len_sq = E * E + F * F
    Math.abs(dot) / Math.sqrt(len_sq)
  }

}
