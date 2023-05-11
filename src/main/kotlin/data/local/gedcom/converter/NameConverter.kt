package data.local.gedcom.converter

import domain.model.Name

class NameConverter {

    companion object {
        private val GEDCOM_NAME_PATTERN = "(.*) /(.*)/".toRegex()
    }

    fun fromGedcom(name: org.folg.gedcom.model.Name): Name {
        val result = GEDCOM_NAME_PATTERN.matchEntire(name.value)

        val firstNames = name.given
            ?.takeUnless { it.isBlank() }
            ?: result?.groupValues?.getOrNull(1)
            ?: "?"
        val lastName = name.surname
            ?.takeUnless { it.isBlank() }
            ?: result?.groupValues?.getOrNull(2)
            ?: "?"

        return Name(
            firstNames = firstNames,
            lastName = lastName,
        )
    }

}