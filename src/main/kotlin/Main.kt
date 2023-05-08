import data.converter.DateConverter
import data.converter.FamilyTreeConverter
import data.converter.NameConverter
import data.local.GedcomLocalDataSource
import data.repository.DefaultFamilyTreeRepository
import domain.repository.FamilyTreeRepository
import presentation.GraphvizTreePresenter
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileSystemView

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        // poor man's DI
        val familyTreeRepository: FamilyTreeRepository = DefaultFamilyTreeRepository(
            dataSource = GedcomLocalDataSource(),
            converter = FamilyTreeConverter(
                nameConverter = NameConverter(),
                dateConverter = DateConverter(),
            ),
        )
        val graphvizTreePresenter = GraphvizTreePresenter()

        // back to business
        askFile("Choose a Gedcom file") { file ->
            if (file == null || file.isDirectory) {
                System.err.println("invalid gedcom file")
                return@askFile
            }

            val filename = file.absolutePath
            val tree = familyTreeRepository.getTree(filename)
            graphvizTreePresenter.show("$filename.pdf", tree)
        }
    }

    private fun askFile(
        title: String,
        onResult: (file: File?) -> Unit
    ) {
        val fileChooser = JFileChooser(FileSystemView.getFileSystemView())
        fileChooser.currentDirectory = File(System.getProperty("user.dir"))
        fileChooser.dialogTitle = title
        fileChooser.fileSelectionMode = JFileChooser.FILES_AND_DIRECTORIES
        fileChooser.isAcceptAllFileFilterUsed = true
        fileChooser.selectedFile = null
        fileChooser.currentDirectory = null
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            val file = fileChooser.selectedFile
            onResult(file)
        } else {
            onResult(null)
        }
    }

}
