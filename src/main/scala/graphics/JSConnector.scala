package graphics

class JSConnector {
  def onClick(): Unit = {
    print("Clicked")
  }

  def callJavascript(msg: String): Unit = {
    System.out.println("JS>> " + msg)
  }
}