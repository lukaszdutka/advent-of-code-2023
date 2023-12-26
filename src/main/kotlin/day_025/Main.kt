package day_025

import java.io.File


private const val inputPath =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_025/input.txt"
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_025/input_small.txt"

fun main() {
    val lines = File(inputPath).readLines()

    for (line in lines) {
        val (node, nodes) = line.split(":").map { it.trim() }
        for (neighbour in nodes.split(" ")) {
            println("$node ${neighbour.trim()}")
        }
    }

}