package data.repository

import data.converter.FamilyTreeConverter
import data.local.GedcomLocalDataSource
import domain.model.RootPolicy
import domain.repository.FamilyTreeRepository

class DefaultFamilyTreeRepository(
    private val dataSource: GedcomLocalDataSource,
    private val converter: FamilyTreeConverter,
) : FamilyTreeRepository {

    override fun getTree(filename: String, rootPolicy: RootPolicy) = dataSource.read(filename)
        .let { converter.fromGedcom(it, rootPolicy) }

}