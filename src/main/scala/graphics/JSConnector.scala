package graphics

import java.util.StringTokenizer

import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import SVGBrowser.simulation
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

import scala.collection.immutable.Map.Map2
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class JSConnector {

  def pushString(msg: String): Unit = {
    System.out.println("JS>> " + msg)

    val arr: Array[String] = msg.split(" ")
    val x: Int = arr(1).toInt
    val y: Int = arr(2).toInt

    SVGBrowser.simulation.addDestination(x,y)

    SVGBrowser.addJSConnectorToJSWindow()
  }

  def getArray(msg: String): List[String] = {
    val listBuffer = new ListBuffer[String]()
    val tokenizer: StringTokenizer = new StringTokenizer(msg, "|")

    while(tokenizer.hasMoreTokens()){
      listBuffer += tokenizer.nextToken()
    }

    val list = listBuffer.toList
    list
  }

  def handleLayers(layers: String): Unit = {
    SVGBrowser.addJSConnectorToJSWindow()

    val list = getArray(layers)
    println("==== WARSTWY ====")
    list.foreach(println)
    println("=================")

    SVGBrowser.onLoadAction()
    SVGBrowser.addJSConnectorToJSWindow()
  }


  def printLayers(layers: String) : Unit = {
    println("====== LAYERS ======")
    println(layers)
    println("====================")

    val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)

    var mapData = mapper.readValue(layers, classOf[List[LayerNode]])

    for (i <- mapData.indices) {
//      println(mapData(i).getName())
    }

    SVGBrowser.onLoadAction()
    SVGBrowser.addJSConnectorToJSWindow()
    }
}