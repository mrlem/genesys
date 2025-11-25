package data.local.gedcom.converter

import domain.model.Date
import domain.model.DatePrecision
import kotlin.test.Test
import kotlin.test.assertEquals

class DateConverterTest {
    private val converter = DateConverter()

    @Test
    fun `fromGedcom should parse full date`() {
        val result = converter.fromGedcom("23 MAY 1984")
        assertEquals(Date(year = 1984, precision = DatePrecision.EXACT), result)
    }

    @Test
    fun `fromGedcom should parse full date with precision`() {
        val result = converter.fromGedcom("ABT 23 MAY 1984")
        assertEquals(Date(year = 1984, precision = DatePrecision.ABOUT), result)
    }

    @Test
    fun `fromGedcom should parse month date`() {
        val result = converter.fromGedcom("MAY 1984")
        assertEquals(Date(year = 1984, precision = DatePrecision.EXACT), result)
    }

    @Test
    fun `fromGedcom should parse month date with precision`() {
        val result = converter.fromGedcom("BEF MAY 1984")
        assertEquals(Date(year = 1984, precision = DatePrecision.BEFORE), result)
    }

    @Test
    fun `fromGedcom should parse year date`() {
        val result = converter.fromGedcom("1984")
        assertEquals(Date(year = 1984, precision = DatePrecision.EXACT), result)
    }

    @Test
    fun `fromGedcom should parse year date with precision`() {
        val result = converter.fromGedcom("AFT 1984")
        assertEquals(Date(year = 1984, precision = DatePrecision.AFTER), result)
    }

    @Test
    fun `fromGedcom should return null for invalid date`() {
        val result = converter.fromGedcom("invalid date")
        assertEquals(null, result)
    }
}