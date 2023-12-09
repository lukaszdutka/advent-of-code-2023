package day_009

import java.io.File


private const val inputPath =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_009/input.txt"
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_009/input_small.txt"

fun main() {
    val file = File(inputPath)

//    solutionV1(file.readLines())
    solutionV2(file.readLines())
}

fun solutionV2(lines: List<String>) {
    var sum = 0
    for (line in lines) {
        sum += solveV2(line)
    }
    println(sum)
}

fun solveV2(line: String): Int {
    val startingInts = line.split(" ").filter { it.isNotBlank() }.map { it.toInt() }.reversed()
    println("startingInts=$startingInts")
    var ints = startingInts
    var pairs = createPairs(ints)

    ints = mutableListOf()
    val lasts = mutableListOf<Int>()
    var shouldContinue = true
    lasts.add(startingInts.last())
    while (shouldContinue) {
        for (pair in pairs) {
            ints.add(pair.first - pair.second)
        }
        pairs = createPairs(ints)
        lasts.add(ints[ints.size - 1])
        if (ints.all { it == 0 }) {
            shouldContinue = false
        }
        println("ints=$ints")
        ints.clear()
    }

    println("lastsReversed=${lasts.reversed()}")
    val realLasts = mutableListOf<Int>()
    for (last in lasts.reversed()) {
        // prawy - X(lewy) = dół
        // last - x = realLasts.lastOrNull() ?: 0
        // x = last - realLasts.lastOrNull() ?: 0

        // x - last = realLasts(size)
        // x = realLasts + last
        val x = last - (realLasts.lastOrNull() ?: 0)
        realLasts.add(x)
    }
    println(realLasts)
    return realLasts.last()
}

fun solutionV1(lines: List<String>) {
    var sum = 0
    for (line in lines) {
//    for (line in lines.subList(0, 1)) {
        sum += solve(line)
    }
    println(sum)
}

fun solve(line: String): Int {
    val startingInts = line.split(" ").filter { it.isNotBlank() }.map { it.toInt() }
    var ints = startingInts
    var pairs = createPairs(ints)

    ints = mutableListOf()
    val lasts = mutableListOf<Int>()
    var shouldContinue = true
    lasts.add(startingInts.last())
    while (shouldContinue) {
        for (pair in pairs) {
            ints.add(pair.second - pair.first)
        }
        pairs = createPairs(ints)
        lasts.add(ints[ints.size - 1])
        if (ints.all { it == 0 }) {
            shouldContinue = false
        }
        ints.clear()
    }

    val realLasts = mutableListOf<Int>()
    for (last in lasts.reversed()) {
        // x - last = realLasts(size)
        // x = realLasts + last
        val x = (realLasts.lastOrNull() ?: 0) + last
        realLasts.add(x)
    }
    return realLasts.last()
}

fun createPairs(ints: List<Int>): List<Pair<Int, Int>> {
    return ints.zip(ints.subList(1, ints.size))
}
