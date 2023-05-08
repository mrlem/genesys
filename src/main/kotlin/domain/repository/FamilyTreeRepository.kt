package domain.repository

import domain.model.FamilyTree
import domain.model.RootPolicy

interface FamilyTreeRepository {

    fun getTree(filename: String, rootPolicy: RootPolicy): FamilyTree

}