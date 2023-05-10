package domain.repository

import domain.model.FamilyTree
import domain.model.RootPolicy
import java.io.IOException

interface FamilyTreeRepository {

    @Throws(NoSuchElementException::class, IOException::class)
    fun getTree(filename: String, rootPolicy: RootPolicy): FamilyTree

}