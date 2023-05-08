import DI.familyTreeRepository
import DI.graphvizTreePresenter
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
        val noPreview by parser.option(ArgType.Boolean, shortName = "np", description = "No preview")
        parser.parse(args)

        // proceed with generation
        val filename = input
        val preview = !(noPreview ?: false)
        if (filename == null) {
            askFile { chosenFilename -> generate(chosenFilename, output, preview) }
        } else {
            generate(filename, output, preview)
        }
    }

    private fun askFile(onSelected: (filename: String) -> Unit) {
        GedcomFileChooser().show { result ->
            result.getOrNull()?.absolutePath
                ?.let(onSelected)
        }
    }

    private fun generate(filename: String, outputFilename: String?, preview: Boolean) {
        val tree = familyTreeRepository.getTree(filename)

        val outputFilename = outputFilename ?: "$filename.pdf"
        println("generating $outputFilename")

        graphvizTreePresenter.generate(
            outputFile = outputFilename,
            tree = tree,
        )

        if (preview) {
            graphvizTreePresenter.show(outputFilename)
        }
    }

}
