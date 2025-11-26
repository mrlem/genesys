package data.local.graphviz.converter

import domain.model.Date
import domain.model.DatePrecision
import domain.model.OutputField
import domain.model.Person
import domain.model.Tree

class TreeConverter(private val outputFields: List<OutputField>) {

    companion object {
        private const val MAX_LEVEL = 20
    }

    fun generate(tree: Tree) = digraph("G") {
        tree.root?.let { addTree(it) }
    }

    private fun DigraphScope.addTree(root: Person) {
        addPersons(root)
        addEdges(root)
    }

    private fun DigraphScope.addPersons(root: Person, level: Int = 0) {
        if (level == 0) {
            addPerson(root)
        }

        if (level <= MAX_LEVEL && root.parents.isNotEmpty()) {
            subgraph {
                subgraph(cluster = true) {
                    root.parents.forEach { parent ->
                        addPerson(parent)
                    }
                }
                root.parents.forEach { parent ->
                    subgraph {
                        addPersons(parent, level + 1)
                    }
                }
            }
        }
    }

    private fun DigraphScope.addPerson(person: Person) {
        node(
            person.id,
            listOfNotNull(
                person.formattedName,
                person.formattedDates.takeIf { outputFields.contains(OutputField.DATES) },
                person.occupation.takeIf { outputFields.contains(OutputField.OCCUPATION) },
            )
                .joinToString("\n"),
        )
    }

    private fun DigraphScope.addEdges(root: Person) {
        val connections = mutableSetOf<Pair<Person, Person>>()
            .apply { addPersonConnections(root) }

        val alreadyConnected = mutableSetOf<Person>()

        connections.forEach { connection ->
            val personAlreadyConnected = !alreadyConnected.add(connection.first)
            edge(connection.first.id, connection.second.id, constraint = !personAlreadyConnected)
        }
    }

    private fun MutableSet<Pair<Person, Person>>.addPersonConnections(person: Person, level: Int = 0) {
        person.parents.forEach { parent ->
            add(parent to person)
            if (level < MAX_LEVEL) {
                addPersonConnections(parent, level + 1)
            }
        }
    }

    private val Person.formattedName
        get() = "${name.firstNames}\n${name.lastName}"

    private val Person.formattedDates: String?
        get() {
            val birthYear = birth?.formattedDate
            val deathYear = death?.formattedDate

            return when {
                birthYear != null && deathYear != null -> "$birthYear - $deathYear"
                birthYear != null -> "*$birthYear"
                deathYear != null -> "†$deathYear"
                else -> null
            }
        }

    private val Date.formattedDate: String
        get() = when (precision) {
            DatePrecision.ABOUT -> "±$year"
            DatePrecision.BEFORE -> "<$year"
            DatePrecision.AFTER -> ">$year"
            DatePrecision.EXACT -> "$year"
        }
}