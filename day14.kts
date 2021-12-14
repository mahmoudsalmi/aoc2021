import java.io.File

typealias Polymer = Map<String, ULong>
typealias MutablePolymer = MutableMap<String, ULong>

typealias PolymerRules = MutableMap<String, Pair<String, String>>

data class Data(val polymer: Polymer, val lastChar: Char) {
    val rules: PolymerRules = mutableMapOf()

    fun addRule(line: String) {
        val splitLine = line.split(" -> ")
        val key = splitLine[0]
        val value = Pair(key[0] + splitLine[1], splitLine[1] + key[1])
        rules[key] = value
    }
}

fun parseData(filename: String): Data {
    val lines = File(filename)
        .readLines()
        .filter { it.isNotEmpty() }
        .toMutableList()

    val polymerStr = lines
        .removeAt(0)

    val polymer = polymerStr.windowed(2)
        .fold<String, MutablePolymer>(mutableMapOf()) { acc, s ->
            acc[s] = acc.getOrDefault(s, 0u) + 1u
            acc
        }

    val res = Data(polymer.toMap(), polymerStr.last())
    lines.forEach { res.addRule(it) }
    return res
}

fun nextStep(polymer: Polymer, rules: PolymerRules): Polymer {
    val res: MutablePolymer = mutableMapOf()

    polymer.entries.forEach {
        if (!rules.containsKey(it.key)) {
            res[it.key] = res.getOrDefault(it.key, 0u) + it.value
        } else {
            val pair = rules[it.key]!!
            res[pair.first] = res.getOrDefault(pair.first, 0u) + it.value
            res[pair.second] = res.getOrDefault(pair.second, 0u) + it.value
        }
    }

    return res
}

fun countChars(polymer: Polymer, lastChar: Char): Map<Char, ULong> {
    val res = mutableMapOf<Char, ULong>()
    polymer.entries.forEach {
        res[it.key[0]] = res.getOrDefault(it.key[0], 0u) + it.value
    }
    res[lastChar] = res.getOrDefault(lastChar, 0u) + 1u
    return res
}

fun solution(polymer: Polymer, rules: PolymerRules, lastChar: Char, steps: Int): ULong {
    var poly = polymer
    for (i in 1..steps) {
        poly = nextStep(poly, rules)
    }
    val counts = countChars(poly, lastChar).values
    return counts.maxOrNull()!! - counts.minOrNull()!!
}

fun part1(data: Data): ULong {
    return solution(data.polymer, data.rules, data.lastChar,10)
}

fun part2(data: Data): ULong {
    return solution(data.polymer, data.rules, data.lastChar, 40)
}

println("----(AOC2021 - Day 14)----------------------[Kotlin]----")

val exemple = parseData("day14.exemple")
println("Exemple :: Part 1 ====>     " + part1(exemple))
println("Exemple :: Part 2 ====>     " + part2(exemple))
println("--------------------------------------------------------")

val input = parseData("day14.input")
println("Input   :: Part 1 ====>     " + part1(input))
println("Input   :: Part 2 ====>     " + part2(input))
println("--------------------------------------------------------")
