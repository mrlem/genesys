import DI.familyTreeRepository
import presentation.GedcomFileChooser
import presentation.GraphvizTreePresenter

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        val graphvizTreePresenter = GraphvizTreePresenter()

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
