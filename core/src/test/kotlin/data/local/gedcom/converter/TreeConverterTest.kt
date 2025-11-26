package data.local.gedcom.converter

import domain.model.Date
import domain.model.DatePrecision
import domain.model.Name
import domain.model.RootPolicy
import domain.model.Sex
import io.mockk.every
import io.mockk.mockk
import org.folg.gedcom.model.EventFact
import org.folg.gedcom.model.Family
import org.folg.gedcom.model.Gedcom
import org.folg.gedcom.model.ParentFamilyRef
import org.folg.gedcom.model.SpouseRef
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.folg.gedcom.model.Name as GedcomName
import org.folg.gedcom.model.Person as GedcomPerson

class TreeConverterTest {

    private lateinit var nameConverter: NameConverter
    private lateinit var dateConverter: DateConverter
    private lateinit var treeConverter: TreeConverter

    @BeforeEach
    fun setUp() {
        nameConverter = mockk()
        dateConverter = mockk()
        treeConverter = TreeConverter(nameConverter, dateConverter)

        // Default mock behavior for nameConverter
        every { nameConverter.fromGedcom(any()) } answers {
            val gedcomName = it.invocation.args[0] as GedcomName
            val parts = gedcomName.value.split(" /")
            Name(firstNames = parts[0], lastName = parts[1].removeSuffix("/"))
        }
        // Default mock behavior for dateConverter
        every { dateConverter.fromGedcom(any()) } returns null
    }

    @Test
    fun `fromGedcom with FirstIndividual policy returns tree with first person as root`() {
        // Arrange
        val gedcom = Gedcom()
        val gedcomPerson1 = GedcomPerson().apply {
            id = "I1"
            names = listOf(GedcomName().apply { value = "John /Smith/" })
        }
        val gedcomPerson2 = GedcomPerson().apply {
            id = "I2"
            names = listOf(GedcomName().apply { value = "Jane /Smith/" })
        }
        gedcom.people = listOf(gedcomPerson1, gedcomPerson2)
        gedcom.createIndexes()

        // Act
        val tree = treeConverter.fromGedcom(gedcom, RootPolicy.FirstIndividual)

        // Assert
        assertNotNull(tree.root)
        assertEquals("I1", tree.root?.id)
        assertEquals("John", tree.root?.name?.firstNames)
        assertEquals("Smith", tree.root?.name?.lastName)
    }

    @Test
    fun `fromGedcom with MostRecent policy returns tree with most recent person as root`() {
        // Arrange
        val gedcom = Gedcom()
        val gedcomPerson1 = GedcomPerson().apply {
            id = "I1"
            names = listOf(GedcomName().apply { value = "John /Smith/" })
        }
        val gedcomPerson2 = GedcomPerson().apply {
            id = "I2"
            names = listOf(GedcomName().apply { value = "Jane /Smith/" })
        }
        gedcom.people = listOf(gedcomPerson1, gedcomPerson2)

        // Mock dateConverter for specific dates
        every { dateConverter.fromGedcom("1980") } returns Date(1980, DatePrecision.EXACT)
        every { dateConverter.fromGedcom("1990") } returns Date(1990, DatePrecision.EXACT)

        gedcomPerson1.eventsFacts = listOf(
            EventFact().apply {
                tag = "BIRT"
                date = "1980"
            },
        )
        gedcomPerson2.eventsFacts = listOf(
            EventFact().apply {
                tag = "BIRT"
                date = "1990"
            },
        )
        gedcom.createIndexes()

        // Act
        val tree = treeConverter.fromGedcom(gedcom, RootPolicy.MostRecent)

        // Assert
        assertNotNull(tree.root)
        assertEquals("I2", tree.root?.id)
    }

    @Test
    fun `fromGedcom with Designated policy by id returns correct person as root`() {
        // Arrange
        val gedcom = Gedcom()
        val gedcomPerson1 = GedcomPerson().apply {
            id = "I1"
            names = listOf(GedcomName().apply { value = "John /Smith/" })
        }
        val gedcomPerson2 = GedcomPerson().apply {
            id = "I2"
            names = listOf(GedcomName().apply { value = "Jane /Smith/" })
        }
        gedcom.people = listOf(gedcomPerson1, gedcomPerson2)
        gedcom.createIndexes()

        // Act
        val tree = treeConverter.fromGedcom(gedcom, RootPolicy.Designated("I2"))

        // Assert
        assertNotNull(tree.root)
        assertEquals("I2", tree.root?.id)
    }

    @Test
    fun `fromGedcom with Designated policy by name returns correct person as root`() {
        // Arrange
        val gedcom = Gedcom()
        val gedcomPerson1 = GedcomPerson().apply {
            id = "I1"
            names = listOf(GedcomName().apply { value = "John /Smith/" })
        }
        val gedcomPerson2 = GedcomPerson().apply {
            id = "I2"
            names = listOf(GedcomName().apply { value = "Jane /Smith/" })
        }
        gedcom.people = listOf(gedcomPerson1, gedcomPerson2)
        gedcom.createIndexes()

        // Act
        val tree = treeConverter.fromGedcom(gedcom, RootPolicy.Designated("Jane Smith"))

        // Assert
        assertNotNull(tree.root)
        assertEquals("I2", tree.root?.id)
    }

    @Test
    fun `fromGedcom with Designated policy throws exception when person not found`() {
        // Arrange
        val gedcom = Gedcom()
        val gedcomPerson1 = GedcomPerson().apply {
            id = "I1"
            names = listOf(GedcomName().apply { value = "John /Smith/" })
        }
        gedcom.people = listOf(gedcomPerson1)
        gedcom.createIndexes()

        // Act & Assert
        assertThrows<NoSuchElementException> {
            treeConverter.fromGedcom(gedcom, RootPolicy.Designated("NonExistent"))
        }
    }

    @Test
    fun `fromGedcom should correctly link parents`() {
        // Arrange
        val gedcom = Gedcom()

        val child = GedcomPerson().apply {
            id = "I1"
            parentFamilyRefs = listOf(ParentFamilyRef().apply { ref = "F1" })
            names = listOf(GedcomName().apply { value = "Child /Name/" })
        }
        val father = GedcomPerson().apply {
            id = "I2"
            eventsFacts = listOf(
                EventFact().apply {
                    tag = "SEX"
                    value = "M"
                },
            )
            names = listOf(GedcomName().apply { value = "Father /Name/" })
        }
        val mother = GedcomPerson().apply {
            id = "I3"
            eventsFacts = listOf(
                EventFact().apply {
                    tag = "SEX"
                    value = "F"
                },
            )
            names = listOf(GedcomName().apply { value = "Mother /Name/" })
        }

        val family = Family().apply {
            id = "F1"
            husbandRefs = listOf(SpouseRef().apply { ref = "I2" })
            wifeRefs = listOf(SpouseRef().apply { ref = "I3" })
        }

        gedcom.people = listOf(child, father, mother)
        gedcom.families = listOf(family)
        gedcom.createIndexes()

        // Act
        val tree = treeConverter.fromGedcom(gedcom, RootPolicy.FirstIndividual)

        // Assert
        assertNotNull(tree.root)
        assertEquals("I1", tree.root?.id)
        assertNotNull(tree.root?.parents)
        assertEquals(2, tree.root?.parents?.size)

        val parent1 = tree.root?.parents?.get(0)
        val parent2 = tree.root?.parents?.get(1)

        // Parents are sorted by sex (Male then Female)
        assertEquals("I2", parent1?.id)
        assertEquals(Sex.MALE, parent1?.sex)
        assertEquals("Father", parent1?.name?.firstNames)

        assertEquals("I3", parent2?.id)
        assertEquals(Sex.FEMALE, parent2?.sex)
        assertEquals("Mother", parent2?.name?.firstNames)
    }
}