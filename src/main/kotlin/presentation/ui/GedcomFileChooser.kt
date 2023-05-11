package presentation.ui

import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileSystemView
import kotlin.coroutines.cancellation.CancellationException

class GedcomFileChooser : JFileChooser(FileSystemView.getFileSystemView()) {

    init {
        currentDirectory = File(System.getProperty("user.dir"))
        dialogTitle = "Choose a Gedcom file"
        fileSelectionMode = FILES_ONLY
        isAcceptAllFileFilterUsed = true
        selectedFile = null
        currentDirectory = null
    }

    fun show(onResult: (Result<File>) -> Unit) {
        if (showOpenDialog(null) == APPROVE_OPTION)
        {
            onResult(Result.success(selectedFile))
        } else
        {
            onResult(Result.failure(CancellationException("file selection cancelled by user")))
        }
    }

}