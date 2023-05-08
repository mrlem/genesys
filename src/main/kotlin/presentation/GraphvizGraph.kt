package presentation

import java.io.File

data class GraphvizGraph(private val dotContent: String) {

    fun generatePDF(filename: String) {
        println("content:\n$dotContent")
        val dotFile = File.createTempFile("genesys_", ".dot")
            .apply { writeText(dotContent) }
        dotFile.deleteOnExit()

        val dotProcess = Runtime.getRuntime().exec("dot -T pdf ${dotFile.absolutePath} -o $filename")
        dotProcess.waitFor()
    }

}