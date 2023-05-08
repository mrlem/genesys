import DI.familyTreeRepository
import DI.graphvizTreePresenter
import presentation.GedcomFileChooser

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        askFile { filename ->
            val tree = familyTreeRepository.getTree(filename)
            graphvizTreePresenter.show("$filename.pdf", tree)
        }
    }

    private fun askFile(onSelected: (filename: String) -> Unit) {
        GedcomFileChooser().show { result ->
            result.getOrNull()?.absolutePath
                ?.let(onSelected)
        }
    }

}
