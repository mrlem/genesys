import DI.genealogyRepository
import DI.treeRepository
import domain.model.OutputField
import domain.model.OutputPolicy
import domain.model.OutputType
import domain.model.RootPolicy
import presentation.cli.Parameters
import presentation.ui.GedcomFileChooser
import presentation.ui.Preview
import java.io.IOException

object Launcher {

    @JvmStatic
    fun main(arguments: Array<String>) {
        val parameters = Parameters(arguments)

        with(parameters) {
            input.let { input ->
                if (input == null) {
                    askFile { chosenFilename ->
                        chosenFilename.generate(outputPolicy, outputType, !noPreview, rootPolicy, outputField)
                    }
                } else {
                    input.generate(outputPolicy, outputType, !noPreview, rootPolicy, outputField)
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

    private fun String.generate(
        outputPolicy: OutputPolicy,
        outputType: OutputType,
        preview: Boolean,
        rootPolicy: RootPolicy,
        outputFields: List<OutputField>,
    ) {
        val filename = this
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
        } catch (e: NoSuchElementException) {
            System.err.println("[generation failed] missing element: ${e.message}")
        } catch (e: IOException) {
            System.err.println("[generation failed] io error: ${e.message}")
        }
    }
}