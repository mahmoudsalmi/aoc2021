import java.io.File

println("Hello world!")

fun initDay() : HashMap<Int, Long> {
    val res: HashMap<Int, Long> = HashMap()
    for (i in 0 until 9) {
        res[i] = 0
    }
    return res
}

fun parseData(filename: String) : HashMap<Int, Long> {
    val res: HashMap<Int, Long> = initDay()

    File(filename)
        .readLines()[0]
        .split(",")
        .map { it.toInt() }
        .forEach{ res[it] = res[it]!!.plus(1) }

	return res
}

fun nextDay(day: HashMap<Int, Long>): HashMap<Int, Long> {
    val nextDay : HashMap<Int, Long> = initDay()

    for (i in 8 downTo  1) {
        nextDay[i-1] = day[i]!!
    }

    val zeros = day[0]!!
    if (zeros > 0) {
        nextDay[8] = nextDay[8]!! + zeros
        nextDay[6] = nextDay[6]!! + zeros
    }

    return nextDay
}

fun solution(data: HashMap<Int, Long>, days: Int): Long {
    var res = data
    for (i in 0 until days) {
        res = nextDay(res)
    }
    return res.values.sum()
}

fun part1(data: HashMap<Int, Long>): Long {
    return solution(data, 80)
}

fun part2(data: HashMap<Int, Long>): Long {
    return solution(data, 256)
}

println("----(AOC2021 - Day 06)----------------------[Kotlin]----")

val exemple = parseData("day6.exemple")
println("Exemple :: Part 1 ====>     " + part1(exemple))
println("Exemple :: Part 2 ====>     " + part2(exemple))
println("--------------------------------------------------------")

val input = parseData("day6.input")
println("Input   :: Part 1 ====>     " + part1(input))
println("Input   :: Part 2 ====>     " + part2(input))
println("--------------------------------------------------------")
