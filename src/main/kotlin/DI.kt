import data.converter.DateConverter
import data.converter.FamilyTreeConverter
import data.converter.NameConverter
import data.local.GedcomLocalDataSource
import data.repository.DefaultFamilyTreeRepository
import domain.repository.FamilyTreeRepository

object DI {

    // poor man's DI
    val familyTreeRepository: FamilyTreeRepository = DefaultFamilyTreeRepository(
        dataSource = GedcomLocalDataSource(),
        converter = FamilyTreeConverter(
            nameConverter = NameConverter(),
            dateConverter = DateConverter(),
        ),
    )

}