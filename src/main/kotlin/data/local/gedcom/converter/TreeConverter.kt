package data.local.gedcom.converter

import domain.model.*
import org.folg.gedcom.model.Gedcom

class TreeConverter(
    private val nameConverter: NameConverter,
    private val dateConverter: DateConverter,
) {

    companion object {

        private val DATE_OLDEST = Date(year = Int.MIN_VALUE, precision = DatePrecision.EXACT)

    }

    @Throws(NoSuchElementException::class)
    fun fromGedcom(gedcom: Gedcom, rootPolicy: RootPolicy): Tree {
        val personsCache: HashMap<String, Person> = HashMap()
        val persons = gedcom.people
            .map { person -> personFromGedcom(personId = person.id, gedcom = gedcom, personsCache = personsCache) }

        val rootPerson = when (rootPolicy) {
            is RootPolicy.FirstIndividual -> persons.firstOrNull()
            is RootPolicy.MostRecent -> {
                persons.maxByOrNull { it.birth ?: DATE_OLDEST }
                    ?: throw NoSuchElementException("cannot find the most recent individual among ${persons.size} in GEDCOM")
            }
            is RootPolicy.Designated -> {
                persons.firstOrNull {
                    it.id.equals(rootPolicy.name, ignoreCase = true) ||
                    "${it.name.firstNames} ${it.name.lastName}".equals(rootPolicy.name, ignoreCase = true) ||
                            "${it.name.lastName} ${it.name.firstNames}".equals(rootPolicy.name, ignoreCase = true)
                }
                    ?: throw NoSuchElementException("cannot find root \"${rootPolicy.name}\"")
            }
        }
        return Tree(root = rootPerson)
    }

    private fun personFromGedcom(
        personId: String,
        gedcom: Gedcom,
        personsCache: HashMap<String, Person> = HashMap(),
    ): Person {
        personsCache[personId]?.let { return it }

        val gedcomPerson = gedcom.getPerson(personId)
        val family = gedcomPerson.parentFamilyRefs.firstOrNull()
            ?.ref
            ?.let { gedcom.getFamily(it) }

        val name = gedcomPerson.names.first()
            ?.let { nameConverter.fromGedcom(it) }
            ?: Name()
        val parents = (family?.husbandRefs.orEmpty() + family?.wifeRefs.orEmpty())
            .take(2)
            .map { it.ref }
            .map { spouseId ->
                personsCache[spouseId]
                    ?: personFromGedcom(spouseId, gedcom, personsCache)
                        .also { personsCache[spouseId] = it }
            }
            .sortedBy { it.sex }
        val sex = gedcomPerson.eventsFacts[EventType.SEX]
            ?.let {
                when (it.value) {
                    "M" -> Sex.MALE
                    "F" -> Sex.FEMALE
                    else -> null
                }
            }

        val birth = (
                gedcomPerson.eventsFacts[EventType.BIRTH]
                    ?: gedcomPerson.eventsFacts[EventType.CHRISTENING]
            )
            ?.date
            ?.let { dateConverter.fromGedcom(it) }
        val death = (
                gedcomPerson.eventsFacts[EventType.DEATH]
                    ?: gedcomPerson.eventsFacts[EventType.BURIAL]
            )
            ?.date
            ?.let { dateConverter.fromGedcom(it) }

        return Person(
            id = gedcomPerson.id,
            name = name,
            parents = parents,
            sex = sex,
            occupation = gedcomPerson.eventsFacts[EventType.OCCUPATION]?.value,
            birth = birth,
            death = death,
        )
    }

}