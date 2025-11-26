import data.local.gedcom.GedcomLocalDataSource
import data.local.gedcom.converter.DateConverter
import data.local.gedcom.converter.NameConverter
import data.local.gedcom.converter.TreeConverter
import data.repository.GedcomGenealogyRepository
import data.repository.GraphvizTreeRepository
import domain.repository.GenealogyRepository

// poor man's DI
object DI {

    val genealogyRepository: GenealogyRepository = GedcomGenealogyRepository(
        dataSource = GedcomLocalDataSource(),
        converter = TreeConverter(
            nameConverter = NameConverter(),
            dateConverter = DateConverter(),
        ),
    )
    val treeRepository = GraphvizTreeRepository()
}