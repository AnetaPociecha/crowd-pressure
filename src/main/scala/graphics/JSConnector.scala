package graphics

import java.util.StringTokenizer

import scala.collection.mutable.ListBuffer

class JSConnector {

  def pushString(msg: String): Unit = {
    System.out.println("JS>> " + msg)
  }

  def getArray(msg: String): List[String] = {
    val listBuffer = new ListBuffer[String]()
    val tokenizer: StringTokenizer = new StringTokenizer(msg, "|");

    while(tokenizer.hasMoreTokens){
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