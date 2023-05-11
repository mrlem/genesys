import DI.familyTreeRepository
import DI.graphvizTreePresenter
import domain.model.OutputType
import domain.model.RootPolicy
import presentation.GedcomFileChooser

object Main {

    @JvmStatic
    fun main(arguments: Array<String>) {
        val parameters = Parameters(arguments)

        with(parameters) {
            val filename = input
            val preview = !(noPreview ?: false)
            val rootPolicy = root?.let { RootPolicy.Designated(it) } ?: RootPolicy.MostRecent

            if (filename == null) {
                askFile { chosenFilename -> generate(chosenFilename, output, outputType, preview, rootPolicy) }
            } else {
                generate(filename, output, outputType, preview, rootPolicy)
            }
        }
    }

    private fun askFile(onSelected: (filename: String) -> Unit) {
        GedcomFileChooser().show { result ->
            result.getOrNull()?.absolutePath
                ?.let(onSelected)
        }
    }

    private fun generate(filename: String, outputFilename: String?, outputType: OutputType?, preview: Boolean, rootPolicy: RootPolicy) {
        try {
            val tree = familyTreeRepository.getTree(filename, rootPolicy)

            val outputType = outputType ?: OutputType.PDF
            val outputFilename = outputFilename ?: "$filename.${outputType.name.lowercase()}"
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
