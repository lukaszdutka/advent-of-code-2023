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
//    calculateSolutionV1(inputs)
    calculateSolutionV2(inputs)
}


fun calculateSolutionV2(grid: MutableList<MutableList<String>>) {
    clearGraphToOnlyDots(grid)
    val start = 0 to 1
    val end = grid.size - 1 to grid.size - 2

    val nodes = calculateNodes(start, end, grid)
    val edges = calculateEdges(nodes, grid)

    fun calculateLongestDistance(
        current: Pair<Int, Int>,
        steps: Int,
        visited: MutableSet<Pair<Int, Int>>
    ): Int {
        if (current == end) {
            return steps
        }
        val neighbours = edges[current]!!
        val paths = mutableListOf<Int>()
        for (neighbour in neighbours) {
            val (y, x, moreSteps) = neighbour
            val neighbourCoords = y to x
            if (neighbourCoords in visited) {
                continue
            }
            val newVisited = visited.toMutableSet()
            newVisited.add(neighbourCoords)
            paths.add(calculateLongestDistance(neighbourCoords, steps + moreSteps, newVisited))
        }
        return paths.maxOrNull() ?: 0
    }

    println(calculateLongestDistance(start, 0, mutableSetOf()))
}


private fun calculateNodes(
    start: Pair<Int, Int>,
    end: Pair<Int, Int>,
    grid: MutableList<MutableList<String>>
): MutableSet<Pair<Int, Int>> {
    val nodes = mutableSetOf<Pair<Int, Int>>()
    nodes.add(start)
    nodes.add(end)

    for ((y, row) in grid.withIndex()) {
        for ((x, char) in row.withIndex()) {
            val pair = y to x
            if (grid.getOrNull(pair) != ".") {
                continue
            }
            val validNeighbours = pair.neighbours().mapNotNull { grid.getOrNull(it) }.filter { it != "#" }
            if (validNeighbours.size > 2) {
                nodes.add(pair)
            }
        }
    }
    return nodes
}

private fun calculateEdges(
    nodes: MutableSet<Pair<Int, Int>>,
    grid: MutableList<MutableList<String>>
): MutableMap<Pair<Int, Int>, MutableSet<Triple<Int, Int, Int>>> {
    val edges = mutableMapOf<Pair<Int, Int>, MutableSet<Triple<Int, Int, Int>>>()
    for (node in nodes) {
        edges[node] = mutableSetOf()
        val acc = mutableSetOf<Triple<Int, Int, Int>>()
        calculateNextNodes(node, node, 0, acc, nodes, grid)
        edges[node] = acc
    }
    return edges
}

fun calculateNextNodes(
    point: Pair<Int, Int>,
    cameFrom: Pair<Int, Int>,
    steps: Int,
    acc: MutableSet<Triple<Int, Int, Int>>,
    allNodes: MutableSet<Pair<Int, Int>>,
    grid: MutableList<MutableList<String>>
) {
    val next = point.neighbours()
        .filter { grid.getOrNull(it) != null && grid.getOrNull(it) != "#" }
        .filter { it != cameFrom }

    next.filter { allNodes.contains(it) }.forEach { acc.add(Triple(it.first, it.second, steps + 1)) }

    val newNext = next.filter { !allNodes.contains(it) }
    newNext.forEach { calculateNextNodes(it, point, steps + 1, acc, allNodes, grid) }
}


private fun Pair<Int, Int>.neighbours(): List<Pair<Int, Int>> = listOf(
    this.up(),
    this.down(),
    this.right(),
    this.left()
)

fun calculateSolutionV1(grid: MutableList<MutableList<String>>) {
    val start = 0 to 1

    val end = grid.size - 1 to grid.size - 2
    fun calculateMaxDistance(point: Pair<Int, Int>, cameFrom: Pair<Int, Int>, steps: Int): Int {
        if (point == end) {
            return steps
        }
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


private fun <String> MutableList<MutableList<String>>.prettyPrint() {
    for (line in this) {
        println(line.joinToString(separator = ""))
    }
}

private fun clearGraphToOnlyDots(grid: MutableList<MutableList<String>>) {
    grid.forEach { it ->
        it.replaceAll {
            if (listOf(">", "<", "v", "^").contains(it)) "." else it
        }
    }
}


