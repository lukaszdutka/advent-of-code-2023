package day_002

import java.io.File

private const val inputPath = "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_002/input2.txt"

fun main() {
    var count = 0
    var count2 = 0
    val lines = File(inputPath)
        .readLines()
    for (game in lines) {
        count += calculateV1(game)
        count2 += calculateV2(game)
    }
    println(count)
    println(count2)
}

fun calculateV1(gameString: String): Int {
    val games = gameString.split(":")
    val id = games[0].split(" ")[1].toInt()

    val valid = mapOf(
        Pair("red", 12),
        Pair("green", 13),
        Pair("blue", 14)
    )

    for (game in games[1].split(";")) {
        for (turn in game.split(",")) {
            val numberOfBalls = turn.trim().split(" ")[0].toInt()
            val color = turn.trim().split(" ")[1].trim()

            if (numberOfBalls > valid[color]!!) {
                return 0
            }
        }
    }
    return id
}

fun calculateV2(gameString: String): Int {
    val games = gameString.split(":")

    val minValues = HashMap<String, Int>()
    minValues["green"] = 0
    minValues["red"] = 0
    minValues["blue"] = 0

    for (game in games[1].split(";")) {
        for (turn in game.split(",")) {
            val numberOfBalls = turn.trim().split(" ")[0].toInt()
            val color = turn.trim().split(" ")[1].trim()

            if (numberOfBalls > minValues[color]!!) {
                minValues[color] = numberOfBalls
            }
        }
    }
    return minValues["green"]!! * minValues["red"]!! * minValues["blue"]!!
}

