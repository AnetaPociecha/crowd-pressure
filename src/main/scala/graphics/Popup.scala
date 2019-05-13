package graphics

import java.io.File

import org.apache.commons.io.FilenameUtils
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafx.stage.{FileChooser, Stage}

object Communicator {
  def chooseFile(stage: Stage): File = {
    val fileChooser: FileChooser = new FileChooser()
    fileChooser.setTitle("Choose map file")
    val file: File = fileChooser.showOpenDialog(stage)
    file
  }

  def invalidExtensionAlert(file: File, stage: Stage): Unit = {
    val header: String = "." + FilenameUtils.getExtension(file.getAbsolutePath) + " file extension is not allowed."
    new Alert(AlertType.Error) {
      initOwner(stage)
      title = "Upload Error"
      headerText = header
      contentText = "Please upload .svg file"
    }.showAndWait()
  }

}
