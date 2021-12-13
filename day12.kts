import java.io.File

data class Node(val name: String) {
    val big: Boolean = name != name.lowercase()
    val nodes: MutableList<Node> = mutableListOf()

    fun addNode(node: Node) {
        nodes.add(node)
    }
}

fun parseData(filename: String): MutableMap<String, Node> {
    val res: MutableMap<String, Node> = mutableMapOf()
    File(filename)
        .readLines()
        .map { it.split("-") }
        .filter { it.size == 2 }
        .forEach { (f, t) ->
            val from: Node = res.getOrPut(f) { Node(f) }
            val to: Node = res.getOrPut(t) { Node(t) }
            from.addNode(to)
            to.addNode(from)
        }
    return res
}

fun getPaths(
    start: Node,
    end: Node,
    previousPath: List<String> = listOf()
): List<List<String>> {
    if (start == end) {
        return listOf(
            previousPath + listOf(start.name)
        )
    }

    if (!start.big && previousPath.contains(start.name)) {
        return listOf()
    }

    return start.nodes.flatMap {
        getPaths(it, end, previousPath + listOf(start.name))
    }
}

fun getPaths2(
    start: Node,
    end: Node,
    previousPath: List<String> = listOf()
): List<List<String>> {
    if (start == end) {
        return listOf(
            previousPath + listOf(start.name)
        )
    }

    if (!start.big && previousPath.contains(start.name)) {
        if (start.name == "start") {
            return listOf()
        }
        val smalls = previousPath.filter { it == it.lowercase() }
        if (smalls.size != smalls.distinct().size) {
            return listOf()
        }
    }

    return start.nodes.flatMap {
        getPaths2(it, end, previousPath + listOf(start.name))
    }
}

fun part1(data: MutableMap<String, Node>): Int {
    return getPaths(data["start"]!!, data["end"]!!).size
}

fun part2(data: MutableMap<String, Node>): Int {
    return getPaths2(data["start"]!!, data["end"]!!).size
}

println("----(AOC2021 - Day 12)----------------------[Kotlin]----")

val exemple = parseData("day12.exemple")
println("Exemple :: Part 1 ====>     " + part1(exemple))
println("Exemple :: Part 2 ====>     " + part2(exemple))
println("--------------------------------------------------------")

val input = parseData("day12.input")
println("Input   :: Part 1 ====>     " + part1(input))
println("Input   :: Part 2 ====>     " + part2(input))
println("--------------------------------------------------------")
