package presentation.ui

import java.awt.Desktop
import java.io.File

object Preview {

    fun show(file: String) {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(File(file))
        }
    }

}