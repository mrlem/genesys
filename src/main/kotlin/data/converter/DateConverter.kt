package data.converter

import domain.model.Date

class DateConverter {

    companion object {
        private val GEDCOM_DATE_PATTERN = "(\\d{2}) (\\w{3}) (\\d{4})".toRegex()
        private val GEDCOM_YEAR_PATTERN = "(\\d{4})".toRegex()
    }

    fun fromGedcom(date: String): Date? =
        fromFullDate(date)
            ?: fromYearDate(date)

    private fun fromFullDate(date: String): Date? {
        val (_, _, year) = (GEDCOM_DATE_PATTERN.matchEntire(date) ?: return null)
            .destructured

        return Date(
            year = year.toInt(),
            exact = true, // TODO - handle non-exact dates
        )
    }

    private fun fromYearDate(date: String): Date? {
        val (year) = (GEDCOM_YEAR_PATTERN.matchEntire(date) ?: return null)
            .destructured

        return Date(
            year = year.toInt(),
            exact = true, // TODO - handle non-exact dates
        )
    }

}