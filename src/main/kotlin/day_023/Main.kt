package day_023

import java.io.File

private const val inputPath =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_023/input.txt"
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_023/input_small.txt"

fun main() {
    val lines = File(inputPath).readLines()

    val inputs = mutableListOf<MutableList<String>>()

    for (line in lines) {
        val regularLine = line.split("").filter { it.isNotBlank() }.toMutableList()
        inputs.add(regularLine)
    }
    calculateSolutionV1(inputs)
}

fun calculateSolutionV1(grid: MutableList<MutableList<String>>) {
    val start = 0 to 1

    val end = grid.size - 1 to grid.size - 2
    fun calculateMaxDistance(point: Pair<Int, Int>, cameFrom: Pair<Int, Int>, steps: Int): Int {
        if (point == end) {
            return steps
        }
        // > < v ^
        var next: List<Pair<Int, Int>>
        if (grid.getOrNull(point)!! == ">") {
            next = listOf(point.right())
        } else if (grid.getOrNull(point)!! == "<") {
            next = listOf(point.left())
        } else if (grid.getOrNull(point)!! == "^") {
            next = listOf(point.up())
        } else if (grid.getOrNull(point)!! == "v") {
            next = listOf(point.down())
        } else {
            next = listOf(
                point.down(),
                point.up(),
                point.right(),
                point.left()
            )
        }

        next = next.filter { grid.getOrNull(it) != null && grid.getOrNull(it) != "#" }
            .filter { it != cameFrom }
        if (next.isEmpty()) {
            return 0
        }
        return next.maxOfOrNull { calculateMaxDistance(it, point, steps + 1) }!!
    }
    println(calculateMaxDistance(start, start.up(), 0))
}

private fun MutableList<MutableList<String>>.getOrNull(coords: Pair<Int, Int>): String? {
    return this.getOrNull(coords.first)?.getOrNull(coords.second)
}


private fun Pair<Int, Int>.up() = this.first - 1 to this.second
private fun Pair<Int, Int>.down() = this.first + 1 to this.second
private fun Pair<Int, Int>.right() = this.first to this.second + 1
private fun Pair<Int, Int>.left() = this.first to this.second - 1