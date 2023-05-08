import data.converter.DateConverter
import data.converter.FamilyTreeConverter
import data.converter.NameConverter
import data.local.GedcomLocalDataSource
import data.repository.DefaultFamilyTreeRepository
import domain.repository.FamilyTreeRepository
import presentation.GedcomFileChooser
import presentation.GraphvizTreePresenter

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
        GedcomFileChooser().show { result ->
            when {
                result.isSuccess -> {
                    val filename = result.getOrNull()?.absolutePath ?: return@show
                    val tree = familyTreeRepository.getTree(filename)
                    graphvizTreePresenter.show("$filename.pdf", tree)
                }
                else -> {
                    System.err.println("invalid gedcom file")
                }
            }
        }
    }

}
