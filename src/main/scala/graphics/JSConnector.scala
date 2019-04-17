package graphics

class JSConnector {
  def callJavascript(msg: String): Unit = {
    System.out.println("JS>> " + msg)
  }
}