package data.repository

import data.converter.GenealogyConverter
import data.local.GedcomLocalDataSource
import domain.model.RootPolicy
import domain.repository.GenealogyRepository
import java.io.IOException

class GedcomGenealogyRepository(
    private val dataSource: GedcomLocalDataSource,
    private val converter: GenealogyConverter,
) : GenealogyRepository {

    @Throws(NoSuchElementException::class, IOException::class)
    override fun getTree(filename: String, rootPolicy: RootPolicy) = dataSource.read(filename)
        .let { converter.fromGedcom(it, rootPolicy) }

}