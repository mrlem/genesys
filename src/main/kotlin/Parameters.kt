import domain.model.OutputType
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.optional

class Parameters(arguments: Array<String>) {

    private val parser = ArgParser("genesys")

    // accessors
    val input by parser.argument(
        ArgType.String,
        fullName = "GEDCOM file",
    )
        .optional()
    val output by parser.option(
        ArgType.String,
        shortName = "o",
        description = "Output file [default: <GEDCOM file>.pdf]",
    )
    val outputType by parser.option(
        ArgType.Choice<OutputType>(),
        shortName = "t",
        description = "Output file type [default: pdf]",
    )
    val root by parser.option(
        ArgType.String, shortName = "r",
        description = "Individual, like \"John Henry Doe\", or identifier,  like \"I1\" [default: most recent individual]",
    )
    val noPreview by parser.option(
        ArgType.Boolean,
        shortName = "np",
        description = "No preview",
    )

    init {
        parser.parse(arguments)
    }

}