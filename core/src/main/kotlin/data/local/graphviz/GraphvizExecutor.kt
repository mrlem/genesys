package data.local.graphviz

import domain.model.OutputType
import java.io.File

object GraphvizExecutor {

    private val FILENAME_WITHOUT_EXTENSION_REGEX = "(.*)(\\.[a-zA-Z]+)".toRegex()
    private const val EXIT_SUCCESS = 0

    @Throws(GraphvizException::class)
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
        println("graphviz: ${commands.joinToString(" ")}")

        val dotProcess = Runtime.getRuntime().exec(commands)
        dotProcess.inputStream.bufferedReader().readText()
            .takeUnless { it.isEmpty() }
            ?.let { println("graphviz: $it") }
        val dotError = dotProcess.errorStream.bufferedReader().readText()
        dotProcess.waitFor()
        val dotExitValue = dotProcess.exitValue()
        if (dotExitValue != EXIT_SUCCESS) {
            throw GraphvizException(dotError)
        }
    }

    private fun String?.default(default: String) = this ?: default
}