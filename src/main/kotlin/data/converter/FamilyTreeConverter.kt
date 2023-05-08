package data.converter

import domain.model.FamilyTree
import domain.model.Name
import domain.model.Person
import domain.model.Sex
import org.folg.gedcom.model.Gedcom

class FamilyTreeConverter(
    private val nameConverter: NameConverter,
    private val dateConverter: DateConverter,
) {

    fun fromGedcom(gedcom: Gedcom): FamilyTree {
        return FamilyTree(root = personFromGedcom(gedcom.people.first().id, gedcom))
    }

    private fun personFromGedcom(
        personId: String,
        gedcom: Gedcom,
        personsCache: HashMap<String, Person> = HashMap(),
    ): Person {
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
        val sex = gedcomPerson.eventsFacts.firstOrNull { it.tag == "SEX" }
            ?.let {
                when (it.value) {
                    "M" -> Sex.MALE
                    "F" -> Sex.FEMALE
                    else -> null
                }
            }

        val birth = (
                gedcomPerson.eventsFacts.firstOrNull { it.tag == "BIRT" }
                    ?: gedcomPerson.eventsFacts.firstOrNull { it.tag == "CHR" }
            )
            ?.date
            ?.let { dateConverter.fromGedcom(it) }
        val death = (
                gedcomPerson.eventsFacts.firstOrNull { it.tag == "DEAT" }
                    ?: gedcomPerson.eventsFacts.firstOrNull { it.tag == "BURI" }
            )
            ?.date
            ?.let { dateConverter.fromGedcom(it) }

        return Person(
            id = gedcomPerson.id,
            name = name,
            parents = parents,
            sex = sex,
            birth = birth,
            death = death,
        )
    }

}