package data.converter

import domain.model.Name

class NameConverter {

    companion object {
        private val GEDCOM_NAME_PATTERN = "(.*) /(.*)/".toRegex()
    }

    fun fromGedcom(name: org.folg.gedcom.model.Name): Name {
        val (firstNames, lastName) = GEDCOM_NAME_PATTERN.matchEntire(name.value)!!
                .destructured
        return Name(
            firstNames = firstNames,
            lastName = lastName,
        )
    }

}