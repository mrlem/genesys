package presentation

import domain.model.FamilyTree
import domain.model.Person

class GraphvizTreePresenter {

    companion object {
        private const val MAX_LEVEL = 20
    }
    fun generate(outputFile: String, tree: FamilyTree) {
        val graph = digraph("G") {
            addTree(tree.root)
        }
        graph.generatePDF(outputFile)
    }

    fun show(file: String) {
        Runtime.getRuntime().exec("open $file")
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

        for (connection in connections) {
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