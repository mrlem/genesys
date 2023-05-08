package presentation

import domain.model.FamilyTree
import domain.model.Person
import kotlin.math.max

class ConsoleTreePresenter {

    private var maxLevel: Int = 0

    fun show(tree: FamilyTree) {
        tree.root?.let { displayPerson(tree.root) }

        println("# of back-tracked generations: $maxLevel")
    }

    private fun displayPerson(person: Person, level: Int = 0) {
        maxLevel = max(maxLevel, level)

        // show current person
        val prefix = "  ".repeat(level)
        println("$prefix${person.name.firstNames} ${person.name.lastName} [${person.birth?.year ?: "?"}-${person.death?.year ?: "?"}]")

        // show parents recursively
        person.parents.forEach { parent ->
            displayPerson(parent, level + 1)
        }
    }

}