import java.io.File
import kotlin.math.abs

fun parseData(filename: String): List<Int> = File(filename)
    .readLines()[0]
    .split(",")
    .map { it.toInt() }

fun med(list: List<Int>): List<Int> = list
    .sorted()
    .let { setOf(it[it.size / 2], it[it.size / 2 + 1]) }
    .toList()

fun avg(list: List<Int>): List<Int> = list
    .average()
    .toInt()
    .let { listOf(it, it + 1) }

fun dist(a: Int, b: Int): Int = abs(a - b)

fun part1(data: List<Int>): Int = med(data)
    .minOf { med -> data.sumOf { abs(it - med) } }

fun part2(data: List<Int>): Int = avg(data)
    .minOf { avg -> data.sumOf { dist(it, avg) * (dist(it, avg) + 1) / 2 } }

println("----(AOC2021 - Day 07)----------------------[Kotlin]----")

val exemple = parseData("day7.exemple")
println("Exemple :: Part 1 ====>     ${part1(exemple)}")
println("Exemple :: Part 2 ====>     ${part2(exemple)}")
println("--------------------------------------------------------")

val input = parseData("day7.input")
println("Input   :: Part 1 ====>     ${part1(input)}")
println("Input   :: Part 2 ====>     ${part2(input)}")
println("--------------------------------------------------------")
