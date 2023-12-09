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

fun solveV2(line: String): Int {
    val startingInts = line.split(" ").filter { it.isNotBlank() }.map { it.toInt() }.reversed()
    var ints = startingInts
    var pairs = ints.zipWithNext()

    ints = mutableListOf()
    val lasts = mutableListOf<Int>()
    var shouldContinue = true
    lasts.add(startingInts.last())
    while (shouldContinue) {
        for (pair in pairs) {
            ints.add(pair.first - pair.second)
        }
        pairs = ints.zipWithNext()
        lasts.add(ints[ints.size - 1])
        if (ints.all { it == 0 }) {
            shouldContinue = false
        }
        ints.clear()
    }

    val realLasts = mutableListOf<Int>()
    for (last in lasts.reversed()) {
        val x = last - (realLasts.lastOrNull() ?: 0)
        realLasts.add(x)
    }
    return realLasts.last()
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

