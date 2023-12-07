package day_007

import java.io.File

private const val inputPath =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_007/input.txt"
//private const val inputPath =
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_007/input_small.txt"

fun main() {
    val file = File(inputPath)
    solutionV1(file.readLines())
}

fun solutionV1(lines: List<String>) {
    val sorted = lines.map { Hand.parse(it) }.sorted()
    var sum = 0
    for ((index, hand) in sorted.withIndex()) {
        sum += (index + 1) * hand.bidAmount
    }
    println(sum)
}