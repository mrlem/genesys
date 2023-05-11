import DI.genealogyRepository
import DI.treeRepository
import domain.model.OutputField
import domain.model.OutputPolicy
import domain.model.OutputType
import domain.model.RootPolicy
import presentation.ui.GedcomFileChooser
import presentation.cli.Parameters
import presentation.ui.Preview

object Main {

    @JvmStatic
    fun main(arguments: Array<String>) {
        val parameters = Parameters(arguments)

        with(parameters) {
            input.let { input ->
                if (input == null) {
                    askFile { chosenFilename -> generate(chosenFilename, outputPolicy, outputType, !noPreview, rootPolicy, outputField) }
                } else {
                    generate(input, outputPolicy, outputType, !noPreview, rootPolicy, outputField)
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

    private fun generate(filename: String, outputPolicy: OutputPolicy, outputType: OutputType, preview: Boolean, rootPolicy: RootPolicy, outputFields: List<OutputField>) {
        try {
            val tree = genealogyRepository.getTree(filename, rootPolicy)

            val outputFilename = when (outputPolicy) {
                is OutputPolicy.Suffixed -> "$filename.${outputType.name.lowercase()}"
                is OutputPolicy.Designated -> outputPolicy.filename
            }

            println("generating $outputFilename")

            treeRepository.exportTree(
                tree = tree,
                filename = outputFilename,
                outputType = outputType,
                outputFields = outputFields,
            )

            if (preview) {
                Preview.show(outputFilename)
            }
        } catch (e: Exception) {
            System.err.println("generation failed:")
            e.printStackTrace()
        }
    }

}
