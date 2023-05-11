import data.converter.DateConverter
import data.converter.GenealogyConverter
import data.converter.NameConverter
import data.local.GedcomLocalDataSource
import data.repository.GedcomGenealogyRepository
import domain.repository.GenealogyRepository
import presentation.GraphvizTreePresenter

// poor man's DI
object DI {

    val genealogyRepository: GenealogyRepository = GedcomGenealogyRepository(
        dataSource = GedcomLocalDataSource(),
        converter = GenealogyConverter(
            nameConverter = NameConverter(),
            dateConverter = DateConverter(),
        ),
    )
    val graphvizTreePresenter = GraphvizTreePresenter()

}