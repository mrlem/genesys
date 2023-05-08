import DI.familyTreeRepository
import DI.graphvizTreePresenter
import presentation.GedcomFileChooser

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        // back to business
        GedcomFileChooser().show { result ->
            if (result.isSuccess) {
                val filename = result.getOrNull()?.absolutePath ?: return@show
                val tree = familyTreeRepository.getTree(filename)
                graphvizTreePresenter.show("$filename.pdf", tree)
            }
        }
    }

}
