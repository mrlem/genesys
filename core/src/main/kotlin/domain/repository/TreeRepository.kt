package domain.repository

import domain.model.OutputField
import domain.model.OutputType
import domain.model.Tree
import java.io.IOException

interface TreeRepository {

    @Throws(IOException::class)
    fun exportTree(tree: Tree, filename: String, outputType: OutputType, outputFields: List<OutputField>)
}