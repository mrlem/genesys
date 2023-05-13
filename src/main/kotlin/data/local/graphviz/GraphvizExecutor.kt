package data.local.graphviz

import domain.model.OutputType
import java.io.File

object GraphvizExecutor {

    private val FILENAME_WITHOUT_EXTENSION_REGEX = "(.*)(\\.[a-zA-Z]+)".toRegex()

    fun generate(dotContent: Graph, filename: String, outputType: OutputType) {
        val dotFilename = FILENAME_WITHOUT_EXTENSION_REGEX.matchEntire(filename)
            ?.groupValues?.getOrNull(1)
            .default(filename)
            .let { "$it.dot" }
        val dotFile = File(dotFilename)
            .apply { writeText(dotContent) }

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

    private fun String?.default(default: String) = this ?: default

}