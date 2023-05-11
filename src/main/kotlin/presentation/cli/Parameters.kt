package presentation.cli

import domain.model.OutputPolicy
import domain.model.OutputType
import domain.model.RootPolicy
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.cli.optional

class Parameters(arguments: Array<String>) {

    companion object {
        private const val DEFAULT_OUTPUT_TOKEN = "<GEDCOM file>.pdf"
        private const val DEFAULT_ROOT_TOKEN = "<most recent>"
    }

    private val parser = ArgParser("genesys")

    // accessors
    val input by parser.argument(
        ArgType.String,
        fullName = "GEDCOM file",
    )
        .optional()
    private val output by parser.option(
        ArgType.String,
        shortName = "o",
        description = "Output file",
    )
        .default(DEFAULT_OUTPUT_TOKEN)
    val outputType by parser.option(
        ArgType.Choice<OutputType>(),
        shortName = "t",
        description = "Output file type",
    )
        .default(OutputType.PDF)
    private val root by parser.option(
        ArgType.String, shortName = "r",
        description = "Individual, like \"John Henry Doe\", or identifier,  like \"I1\"",
    )
        .default(DEFAULT_ROOT_TOKEN)
    val noPreview by parser.option(
        ArgType.Boolean,
        shortName = "np",
        description = "No preview",
    )
        .default(false)

    val outputPolicy
        get() = when (output) {
            DEFAULT_OUTPUT_TOKEN -> OutputPolicy.Suffixed
            else -> OutputPolicy.Designated(output)
        }

    val rootPolicy
        get() = when (root) {
            DEFAULT_ROOT_TOKEN -> RootPolicy.MostRecent
            else -> RootPolicy.Designated(root)
        }

    init {
        parser.parse(arguments)
    }

}