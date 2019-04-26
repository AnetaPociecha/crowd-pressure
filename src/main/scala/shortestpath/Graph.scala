package shortestpath

object Graph {

  def main(args: Array[String]): Unit = {
    val dijkstra = Dijkstra(Array(
      Array.fill(3)(true),
      Array.fill(3)(true),
      Array.fill(3)(true)
    ))

    val res = dijkstra.costArray((2,2))
    println(res)

  }
}

