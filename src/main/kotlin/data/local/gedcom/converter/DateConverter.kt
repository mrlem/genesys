package data.local.gedcom.converter

import domain.model.Date
import domain.model.DatePrecision

// TODO - handle age dates
// TODO - handle more exotic precisions
class DateConverter {

    companion object {
        private val GEDCOM_DATE_PATTERN = "^((?:ABT|BEF|AFT) )?(\\d{1,2}) ([a-zA-Z]{3}) (\\d{4})$".toRegex()
        private val GEDCOM_MONTH_PATTERN = "^((?:ABT|BEF|AFT) )?([a-zA-Z]{3}) (\\d{4})$".toRegex()
        private val GEDCOM_YEAR_PATTERN = "^((?:ABT|BEF|AFT) )?(\\d{4})$".toRegex()
    }

    fun fromGedcom(date: String): Date? =
        fromFullDate(date)
            ?: fromYearDate(date)
            ?: fromMonthDate(date)

    private fun fromFullDate(date: String): Date? {
        val (precision, _, _, year) = (GEDCOM_DATE_PATTERN.matchEntire(date) ?: return null)
            .destructured

        return Date(
            year = year.toInt(),
            precision = precision.asDatePrecision,
        )
    }

    private fun fromMonthDate(date: String): Date? {
        val (precision, _, year) = (GEDCOM_MONTH_PATTERN.matchEntire(date) ?: return null)
            .destructured

        return Date(
            year = year.toInt(),
            precision = precision.asDatePrecision,
        )
    }

    private fun fromYearDate(date: String): Date? {
        val (precision, year) = (GEDCOM_YEAR_PATTERN.matchEntire(date) ?: return null)
            .destructured

        return Date(
            year = year.toInt(),
            precision = precision.asDatePrecision,
        )
    }

    private val String.asDatePrecision: DatePrecision
        get() = when (this) {
            "ABT " -> DatePrecision.ABOUT
            "BEF " -> DatePrecision.BEFORE
            "AFT " -> DatePrecision.AFTER
            else -> DatePrecision.EXACT
        }

}