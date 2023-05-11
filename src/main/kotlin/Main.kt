import DI.genealogyRepository
import DI.graphvizTreePresenter
import domain.model.OutputPolicy
import domain.model.OutputType
import domain.model.RootPolicy
import presentation.ui.GedcomFileChooser
import presentation.cli.Parameters

object Main {

    @JvmStatic
    fun main(arguments: Array<String>) {
        val parameters = Parameters(arguments)

        with(parameters) {
            input.let { input ->
                if (input == null) {
                    askFile { chosenFilename -> generate(chosenFilename, outputPolicy, outputType, !noPreview, rootPolicy) }
                } else {
                    generate(input, outputPolicy, outputType, !noPreview, rootPolicy)
                }
            }
        }
    }

    private fun askFile(onSelected: (filename: String) -> Unit) {
        GedcomFileChooser().show { result ->
            result.getOrNull()?.absolutePath
                ?.let(onSelected)
        }
    }

    private fun generate(filename: String, outputPolicy: OutputPolicy, outputType: OutputType, preview: Boolean, rootPolicy: RootPolicy) {
        try {
            val tree = genealogyRepository.getTree(filename, rootPolicy)

            val outputFilename = when (outputPolicy) {
                is OutputPolicy.Suffixed -> "$filename.${outputType.name.lowercase()}"
                is OutputPolicy.Designated -> outputPolicy.filename
            }

            println("generating $outputFilename")

            graphvizTreePresenter.generate(
                outputFile = outputFilename,
                outputType = outputType,
                tree = tree,
            )

            if (preview) {
                graphvizTreePresenter.show(outputFilename)
            }
        } catch (e: Exception) {
            System.err.println("generation failed:")
            e.printStackTrace()
        }
    }

}
