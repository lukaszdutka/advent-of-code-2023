package day_004

import java.io.File
import kotlin.math.pow

private const val inputPath =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_004/input4.txt"
//private const val inputPath =
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_004/input4_small.txt"

fun main() {
    val lines = File(inputPath)
        .readLines()

//    solutionV1(lines)
    solutionV2(lines)
}

fun solutionV2(lines: List<String>) {
    var totalScratchCards = 0;
    val gameNumberToQuantity = mutableMapOf<Int, Int>()

    for (line in lines) {
        val splitted = line.split(":")[0].split(" ")
        val gameNumber = splitted[splitted.size - 1].toInt()
        gameNumberToQuantity.putIfAbsent(gameNumber, 1)

        val s = line.split(":")[1]
        val winning = s.split("|")[0]
        val your = s.split("|")[1]

        val winningNumbers = winning.split(" ").filter { it.isNotBlank() }.map { it.toInt() }.toSet()
        val yourNumbers = your.split(" ").filter { it.isNotBlank() }.map { it.toInt() }.toSet()

        val count = winningNumbers.count { yourNumbers.contains(it) }

        val multiplier = gameNumberToQuantity[gameNumber]!!
        for (number in 1..count) {
            gameNumberToQuantity[gameNumber + number] = (gameNumberToQuantity[gameNumber + number] ?: 1) + multiplier
        }
    }
    println(gameNumberToQuantity.values.sum())
}

fun solutionV1(lines: List<String>) {
    var points = 0;
    for (line in lines) {
        val s = line.split(":")[1]
        val winning = s.split("|")[0]
        val your = s.split("|")[1]

        val winningNumbers = winning.split(" ").filter { it.isNotBlank() }.map { it.toInt() }.toSet()
        val yourNumbers = your.split(" ").filter { it.isNotBlank() }.map { it.toInt() }.toSet()

        var count = 0
        for (number in yourNumbers) {
            if (winningNumbers.contains(number)) {
                count++
            }
        }
//        count = count.coerceAtMost(4)
        points += 2.0.pow(count.toDouble() - 1).toInt()
    }
    println(points)
}