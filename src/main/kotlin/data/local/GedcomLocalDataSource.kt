package data.local

import org.folg.gedcom.model.Gedcom
import org.folg.gedcom.parser.ModelParser
import java.io.File

class GedcomLocalDataSource {

    fun read(filename: String): Gedcom {
        val input = File(filename).inputStream()
        val modelParser = ModelParser()
        return modelParser.parseGedcom(input)
            .also { it.createIndexes() }
    }

}