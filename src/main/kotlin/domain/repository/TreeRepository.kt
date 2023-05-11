package domain.repository

import domain.model.Tree
import domain.model.OutputType
import java.io.IOException

interface TreeRepository {

    @Throws(IOException::class)
    fun exportTree(tree: Tree, filename: String, outputType: OutputType)

}