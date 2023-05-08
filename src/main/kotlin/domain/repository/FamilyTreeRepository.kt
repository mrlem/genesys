package domain.repository

import domain.model.FamilyTree

interface FamilyTreeRepository {

    fun getTree(filename: String): FamilyTree

}