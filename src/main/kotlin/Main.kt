import DI.familyTreeRepository
import DI.graphvizTreePresenter
import domain.model.OutputType
import domain.model.RootPolicy
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.optional
import presentation.GedcomFileChooser

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        // command line parsing
        val parser = ArgParser("genesys")
        val input by parser.argument(ArgType.String, "Input file").optional()
        val output by parser.option(ArgType.String, shortName = "o", description = "Output file name")
        val outputType by parser.option(ArgType.Choice<OutputType>(), shortName = "t", description = "Output file type")
        val noPreview by parser.option(ArgType.Boolean, shortName = "np", description = "No preview")
        val root by parser.option(ArgType.String, shortName = "r", description = "Root name")
        parser.parse(args)

        // proceed with generation
        val filename = input
        val preview = !(noPreview ?: false)
        val rootPolicy = root?.let { RootPolicy.Designated(it) } ?: RootPolicy.MostRecent

        if (filename == null) {
            askFile { chosenFilename -> generate(chosenFilename, output, outputType, preview, rootPolicy) }
        } else {
            generate(filename, output, outputType, preview, rootPolicy)
        }
    }

    private fun askFile(onSelected: (filename: String) -> Unit) {
        GedcomFileChooser().show { result ->
            result.getOrNull()?.absolutePath
                ?.let(onSelected)
        }
    }

    private fun generate(filename: String, outputFilename: String?, outputType: OutputType?, preview: Boolean, rootPolicy: RootPolicy) {
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
    }

}
