package data.repository

import data.local.gedcom.converter.TreeConverter
import data.local.gedcom.GedcomLocalDataSource
import domain.model.RootPolicy
import domain.repository.GenealogyRepository
import java.io.IOException

class GedcomGenealogyRepository(
    private val dataSource: GedcomLocalDataSource,
    private val converter: TreeConverter,
) : GenealogyRepository {

    @Throws(NoSuchElementException::class, IOException::class)
    override fun getTree(filename: String, rootPolicy: RootPolicy) = dataSource.read(filename)
        .let { converter.fromGedcom(it, rootPolicy) }

}