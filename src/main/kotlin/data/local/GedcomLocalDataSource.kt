package data.local

import org.folg.gedcom.model.Gedcom
import org.folg.gedcom.parser.ModelParser
import org.xml.sax.SAXParseException
import java.io.File
import java.io.IOException

class GedcomLocalDataSource {

    @Throws(IOException::class)
    fun read(filename: String): Gedcom {
        val input = File(filename).inputStream()
        val modelParser = ModelParser()
        try {
            return modelParser.parseGedcom(input)
                .also { it.createIndexes() }
        } catch (e: SAXParseException) {
            throw IOException("$filename parsing failed", e)
        }
    }

}