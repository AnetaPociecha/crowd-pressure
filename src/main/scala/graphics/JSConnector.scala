package graphics

import java.util.StringTokenizer

import scala.collection.mutable.ListBuffer
import SVGBrowser.simulation
import _root_.simulation.Simulation

class JSConnector {

  def pushString(msg: String): Unit = {
    System.out.println("JS>> " + msg)
    val arr: Array[String] = msg.split(" ")
    val x: Int = arr(1).toInt
    val y: Int = arr(2).toInt
    simulation.tmpDestinations += ((x,y))
    println("destinations: " + simulation.tmpDestinations)
    SVGBrowser.addJSConnectorToJSWindow()
  }

  def getArray(msg: String): List[String] = {
    val listBuffer = new ListBuffer[String]()
    val tokenizer: StringTokenizer = new StringTokenizer(msg, "|");

    while(tokenizer.hasMoreTokens()){
      listBuffer += tokenizer.nextToken()
    }

    val list = listBuffer.toList
    list
  }

  def handleLayers(layers: String): Unit = {

    val list = getArray(layers)
    println("==== WARSTWY ====")
    list.foreach(println)
    println("=================")
  }
}