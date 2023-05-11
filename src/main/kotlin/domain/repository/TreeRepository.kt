package domain.repository

import domain.model.FamilyTree
import domain.model.OutputType
import java.io.IOException

interface TreeRepository {

    @Throws(IOException::class)
    fun exportTree(tree: FamilyTree, filename: String, outputType: OutputType)

}