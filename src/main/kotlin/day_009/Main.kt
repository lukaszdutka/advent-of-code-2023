package day_009

import java.io.File

private const val inputPath =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_009/input.txt"
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_009/input_small.txt"

fun main() {
    val file = File(inputPath)

    solutionV1(file.readLines())
    solutionV2(file.readLines())
}

fun solutionV2(lines: List<String>) {
    println(lines.sumOf(::solveV2))
}

fun solveV2(lineString: String): Int {
    var line = lineString.split(" ").map { it.toInt() }.reversed()
    val lasts = mutableListOf<Int>()

    while (true) {
        lasts.add(line.last())
        line = line.zipWithNext { first, second -> first - second }
        if (line.all { it == 0 }) {
            break
        }
    }
    return lasts.reversed().reduce { acc, x -> x - acc }
}

fun solutionV1(lines: List<String>) {
    println(lines.sumOf(::solve))
}

fun solve(lineString: String): Int {
    var line = lineString.split(" ").map { it.toInt() }
    var sum = 0

    while (true) {
        sum += line.last()
        line = line.zipWithNext { first, second -> second - first }
        if (line.all { it == 0 }) {
            break
        }
    }
    return sum
}

