package presentation

import domain.model.OutputType
import java.io.File

data class GraphvizGraph(private val dotContent: String) {

    fun generate(filename: String, outputType: OutputType) {
        println("content:\n$dotContent")
        val dotFile = File.createTempFile("genesys_", ".dot")
            .apply { writeText(dotContent) }
        dotFile.deleteOnExit()

        val commands = arrayOf(
            "dot",
            "-T${outputType.name.lowercase()}",
            dotFile.absolutePath,
            "-o",
            filename,
        )
        val dotProcess = Runtime.getRuntime().exec(commands)
        println("exec: ${commands.joinToString(" ")}")
        dotProcess.waitFor()
        println("exec: returned ${dotProcess.exitValue()}")
    }

}