package data.repository

import data.local.graphviz.*
import data.local.graphviz.GraphvizExecutor.generate
import domain.model.FamilyTree
import domain.model.OutputType
import domain.model.Person
import domain.repository.TreeRepository

class GraphvizTreeRepository : TreeRepository {

    companion object {
        private const val MAX_LEVEL = 20
    }

    override fun exportTree(tree: FamilyTree, filename: String, outputType: OutputType) {
        val graph = digraph("G") {
            tree.root?.let { addTree(it) }
        }
        generate(
            dotContent = graph,
            filename = filename,
            outputType = outputType,
        )
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
        node(person.id, person.name.let { "${it.firstNames}\n${it.lastName}" })
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

}