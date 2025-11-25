package data.local.graphviz.converter

import data.local.graphviz.Graph
import java.io.ByteArrayOutputStream
import java.io.PrintWriter

fun digraph(name: String, content: DigraphScope.() -> Unit): Graph {
    val out = ByteArrayOutputStream()
    val writer = PrintWriter(out)

    writer.println("digraph $name {")
    writer.println("  node [shape=underline, fontsize=8, fontname=arial, margin=0.05, height=0, width=0];")
    writer.println("  edge [dir=none, penwidth=0.5];")
    writer.println("  peripheries=0")

    DigraphScope(writer, 1).content()

    writer.println("}")

    writer.flush()
    return out.toString()
}

fun DigraphScope.subgraph(cluster: Boolean = false, content: DigraphScope.() -> Unit) {
    writer.println("${prefix}subgraph {")
    writer.println("$prefix  cluster=$cluster")

    DigraphScope(writer, level + 1).content()

    writer.println("$prefix}")
}

fun DigraphScope.node(id: String, label: String) {
    writer.println("${prefix}\"$id\" [label=\"${label.escape()}\"]")
}

fun DigraphScope.edge(fromId: String, toId: String, constraint: Boolean) {
    writer.println("${prefix}\"$fromId\" -> \"$toId\" [constraint=$constraint, style=${if (constraint) "solid" else "dotted"}]")
}

fun String.escape() = replace("\"", "\\\"")

@GraphvizDslMarker
class DigraphScope(
    val writer: PrintWriter,
    val level: Int,
) {
    val prefix = "  ".repeat(level)
}

@DslMarker
annotation class GraphvizDslMarker