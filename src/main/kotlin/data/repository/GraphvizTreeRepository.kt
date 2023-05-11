package data.repository

import data.local.graphviz.*
import data.local.graphviz.GraphvizExecutor.generate
import data.local.graphviz.converter.TreeConverter
import domain.model.OutputField
import domain.model.Tree
import domain.model.OutputType
import domain.repository.TreeRepository
import java.io.IOException

class GraphvizTreeRepository : TreeRepository {

    @Throws(IOException::class)
    override fun exportTree(tree: Tree, filename: String, outputType: OutputType, outputFields: List<OutputField>) {
        val graph = TreeConverter(outputFields).generate(tree)

        generate(
            dotContent = graph,
            filename = filename,
            outputType = outputType,
        )
    }

}