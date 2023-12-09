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
    var line = lineString.split(" ").map { it.toInt() }
    val lasts = mutableListOf<Int>()

    while (line.any { it != 0 }) {
        lasts.add(line.first())
        line = line.zipWithNext { first, second -> second - first }
    }
    return lasts.reduceRight { x, acc -> x - acc }
}

fun solutionV1(lines: List<String>) {
    println(lines.sumOf(::solve))
}

fun solve(lineString: String): Int {
    var line = lineString.split(" ").map { it.toInt() }
    var sum = 0

    while (line.any { it != 0 }) {
        sum += line.last()
        line = line.zipWithNext { first, second -> second - first }
    }
    return sum
}

