package graphics

import java.io.File

import scalafx.stage.{FileChooser, Stage}

object Popup {
  def chooseFile(stage: Stage): File = {
    val fileChooser: FileChooser = new FileChooser()
    fileChooser.setTitle("Choose map file")

    val extFilter: FileChooser.ExtensionFilter = new FileChooser.ExtensionFilter("SVG files (*.svg)", "*.svg")
    fileChooser.getExtensionFilters.add(extFilter)

    val file: File = fileChooser.showOpenDialog(stage)
    file
  }
}
