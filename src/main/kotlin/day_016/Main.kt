package day_016

import java.io.File
import java.lang.RuntimeException


private const val inputPath =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_016/input.txt"
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_016/input_small.txt"

fun main() {
    val lines = File(inputPath).readLines()
    val inputs = mutableListOf<MutableList<String>>()
    val inputs2 = mutableListOf<MutableList<String>>()

    for (line in lines) {
        inputs.add(line.split("").filter { it.isNotBlank() }.toMutableList())
        inputs2.add(line.split("").filter { it.isNotBlank() }.toMutableList())
    }
    println(solveV1(inputs, 0 to 0, "right"))
    println(solveV2(inputs2))
}

fun solveV2(inputs2: MutableList<MutableList<String>>): Int {
    var max = 0
    for (i in 0..<110) {
        max = listOf(
            max,
            solveV1(inputs2, i to 0, "right"),
            solveV1(inputs2, 0 to i, "down"),
            solveV1(inputs2, 109 to i, "up"),
            solveV1(inputs2, i to 109, "left")
        ).max()
    }
    return max
}

fun solveV1(grid: MutableList<MutableList<String>>, startingNode: Pair<Int, Int>, startingDirection: String): Int {
    val checkedMoves = mutableSetOf<Triple<Int, Int, String>>()

    fun drawBeam(coords: Pair<Int, Int>, direction: String) {
        val (y, x) = coords
        val cell = grid.getOrNull(y)?.getOrNull(x) ?: return
        if (Triple(y, x, direction) in checkedMoves) {
            return
        }
        checkedMoves.add(Triple(y, x, direction))

        if (cell == ".") {
            drawBeam(coords.change(direction), direction)
        } else if (cell == "/") {
            when (direction) {
                "right" -> drawBeam(coords.change("up"), "up")
                "left" -> drawBeam(coords.change("down"), "down")
                "up" -> drawBeam(coords.change("right"), "right")
                "down" -> drawBeam(coords.change("left"), "left")
            }
        } else if (cell == "\\") {
            when (direction) {
                "right" -> drawBeam(coords.change("down"), "down")
                "left" -> drawBeam(coords.change("up"), "up")
                "up" -> drawBeam(coords.change("left"), "left")
                "down" -> drawBeam(coords.change("right"), "right")
            }
        } else if (cell == "|") {
            when (direction) {
                "right", "left" -> {
                    drawBeam(coords.change("down"), "down")
                    drawBeam(coords.change("up"), "up")
                }

                "up" -> drawBeam(coords.change("up"), "up")
                "down" -> drawBeam(coords.change("down"), "down")
            }
        } else if (cell == "-") {
            when (direction) {
                "right" -> drawBeam(coords.change("right"), "right")
                "left" -> drawBeam(coords.change("left"), "left")
                "up", "down" -> {
                    drawBeam(coords.change("left"), "left")
                    drawBeam(coords.change("right"), "right")
                }
            }
        }
    }

    drawBeam(startingNode, startingDirection)
    return checkedMoves.map { t -> Pair(t.first, t.second) }.toSet().size
}

private fun MutableList<MutableList<String>>.print(energizedFields: MutableSet<Pair<Int, Int>>) {
    for ((y, row) in this.withIndex()) {
        for ((x, char) in row.withIndex()) {
            if (energizedFields.contains(y to x)) {
                print("#")
            } else {
                print(char)
            }
        }
        println()
    }
}

private fun Pair<Int, Int>.change(direction: String): Pair<Int, Int> {
    if (direction == "right") {
        return Pair(this.first, this.second + 1)
    }
    if (direction == "left") {
        return Pair(this.first, this.second - 1)
    }
    if (direction == "up") {
        return Pair(this.first - 1, this.second)
    }
    if (direction == "down") {
        return Pair(this.first + 1, this.second)
    }
    throw RuntimeException("bad direction $direction")
}

