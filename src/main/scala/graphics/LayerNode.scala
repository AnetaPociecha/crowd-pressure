package graphics

import com.fasterxml.jackson.annotation._
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.scala.deser.overrides.MutableList

import scala.collection.mutable


class LayerNode {

  @JsonProperty("name")
  var name: String = ""

  @JsonProperty("children")
  var children: MutableList[LayerNode] = mutable.MutableList()
  var properties = scala.collection.mutable.Map[String, JsonNode]()


  def this(name: String, children: MutableList[LayerNode]) {
    this()
    this.name = name
    this.children = children
  }

  @JsonSetter("name")
  def setName(name : String): Unit = {
    this.name = name
    println("setting name")
  }

  @JsonGetter("name")
  def getName(): String = {
    return this.name
  }

  @JsonSetter("children")
  def setChildren(children: MutableList[LayerNode]): Unit = {
    this.children = children
    println("setting children")
  }

  @JsonGetter("children")
  def getChildren(): MutableList[LayerNode] = {
    return this.children
  }

  @JsonAnyGetter
  def getProperties() : scala.collection.mutable.Map[String,JsonNode] = {
    return properties
  }

  @JsonAnySetter
  def setProperty(key: String, value: JsonNode) : Unit = {
    this.properties.put(key,value)
    println("setting exception")
  }

}
