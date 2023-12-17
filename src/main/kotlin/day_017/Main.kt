package day_017

import java.io.File
import java.lang.RuntimeException
import java.util.*
import kotlin.math.abs

private const val inputPath =
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_017/input.txt"
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_017/input_small.txt"

//1448 - too high
// 1291
fun main() {
    val lines = File(inputPath).readLines()
    val inputs = mutableListOf<MutableList<Int>>()
    val inputs2 = mutableListOf<MutableList<Int>>()

    for (line in lines) {
        inputs.add(line.split("").filter { it.isNotBlank() }.map { it.toInt() }.toMutableList())
        inputs2.add(line.split("").filter { it.isNotBlank() }.map { it.toInt() }.toMutableList())
    }

    println(solutionV1(inputs))
}

data class Node(val coords: Pair<Int, Int>, val cost: Int)

fun solutionV1(grid: MutableList<MutableList<Int>>): Int {
    val startingPoint = 0 to 0
    val goal = grid.size - 1 to grid[0].size - 1
    val frontier: PriorityQueue<Node> = PriorityQueue { n1, n2 -> n1.cost - n2.cost }
    val cameFrom = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>?>()
    val costSoFar = mutableMapOf<Pair<Int, Int>, Int>()

    frontier.add(Node(startingPoint, 0))
    cameFrom[startingPoint] = null
    costSoFar[startingPoint] = 0

    while (frontier.isNotEmpty()) {
        val current = frontier.poll()!!
        if (current.coords == goal) {
            break
        }
        for (next in neighbours(current.coords, grid)) {
            val newCost = costSoFar[current.coords]!! + grid[next.first][next.second]
            val priority = newCost + heuristic(goal, next)
            if (next in costSoFar && newCost >= costSoFar[next]!!) {
                continue
            }
            cameFrom[next] = current.coords
            if (pathContainsThreeInRow(cameFrom, next)) {
                cameFrom.remove(next)
                continue
            }
            costSoFar[next] = newCost
            frontier.add(Node(next, priority))
        }
    }

    val path = pairs(goal, cameFrom)

    printMyThing(grid, path)
    println(path.sumOf { grid[it.first][it.second] })
    return costSoFar[goal]!!
}

fun heuristic(goal: Pair<Int, Int>, next: Pair<Int, Int>): Int {
    return abs(goal.first - next.first) + abs(goal.second - next.second)
}

//def heuristic(a, b):
//# Manhattan distance on a square grid
//return abs(a.x - b.x) + abs(a.y - b.y)

private fun pairs(
    goal: Pair<Int, Int>,
    cameFrom: MutableMap<Pair<Int, Int>, Pair<Int, Int>?>
): MutableList<Pair<Int, Int>> {
    var next = goal
    val path = mutableListOf<Pair<Int, Int>>()
    path.add(next)
    while (cameFrom[next] != null) {
        path.add(cameFrom[next]!!)
        next = cameFrom[next]!!
    }
    return path
}

fun printMyThing(grid: MutableList<MutableList<Int>>, path: MutableList<Pair<Int, Int>>) {
    for ((y, row) in grid.withIndex()) {
        for ((x, char) in row.withIndex()) {
            if (path.contains(y to x)) {
                print("x")
            } else {
                print(char)
            }
        }
        println()
    }
}

fun pathContainsThreeInRow(
    cameFrom: MutableMap<Pair<Int, Int>, Pair<Int, Int>?>,
    dest: Pair<Int, Int>
): Boolean {
    var next = dest
    val path = mutableListOf<Pair<Int, Int>>()
    path.add(next)
    while (cameFrom[next] != null) {
        path.add(cameFrom[next]!!)
        next = cameFrom[next]!!
    }

    val directions = path.zipWithNext().map { changeToDirection(it) }

    val groups = count(directions)
    return groups.any { it.count >= 4 }
}

fun count(values: List<String>): List<Group> {
    val groups = mutableListOf<Group>()
    values.forEach {
        val last = groups.lastOrNull()
        if (last?.value == it) {
            last.count++
        } else {
            groups.add(Group(it, 1))
        }
    }
    return groups
}

data class Group(val value: String, var count: Int)

fun changeToDirection(it: Pair<Pair<Int, Int>, Pair<Int, Int>>): String {
    val pair1 = it.first
    val pair2 = it.second
    if (pair1.first - pair2.first == 1) {
        return "a"
    }
    if (pair1.first - pair2.first == -1) {
        return "b"
    }
    if (pair1.second - pair2.second == 1) {
        return "c"
    }
    if (pair1.second - pair2.second == -1) {
        return "d"
    }
    throw RuntimeException("Something not right")
}

fun neighbours(e: Pair<Int, Int>, grid: MutableList<MutableList<Int>>): List<Pair<Int, Int>> {
    return listOf(e.left(), e.right(), e.up(), e.down())
        .filter { grid.getOrNull(it.first)?.getOrNull(it.second) != null }
}

private fun Pair<Int, Int>.up() = this.first - 1 to this.second
private fun Pair<Int, Int>.down() = this.first + 1 to this.second
private fun Pair<Int, Int>.right() = this.first to this.second + 1
private fun Pair<Int, Int>.left() = this.first to this.second - 1