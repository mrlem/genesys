package presentation

import domain.model.OutputType
import java.io.File

data class GraphvizGraph(private val dotContent: String) {

    fun generate(filename: String, outputType: OutputType) {
        println("content:\n$dotContent")
        val dotFile = File.createTempFile("genesys_", ".dot")
            .apply { writeText(dotContent) }
        dotFile.deleteOnExit()

        val dotProcess = Runtime.getRuntime().exec("dot -T ${outputType.name.lowercase()} ${dotFile.absolutePath} -o $filename")
        dotProcess.waitFor()
    }

}