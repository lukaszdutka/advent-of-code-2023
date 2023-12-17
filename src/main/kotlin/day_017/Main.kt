package day_017

import java.io.File
import java.lang.RuntimeException
import java.util.*

private const val inputPath =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_017/input.txt"
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_017/input_small.txt"

//1448 - too high
// 1291 - too high
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

data class Node(val coords: Pair<Int, Int>, val direction: String?, val repeats: Int, val weight: Int)
data class NodeNoWeight(val coords: Pair<Int, Int>, val direction: String?, val repeats: Int)

fun solutionV1(grid: MutableList<MutableList<Int>>): Int {
    val startingNode = Node(0 to 0, null, 0, 0)
    val goal = grid.size - 1 to grid[0].size - 1

    val frontier: PriorityQueue<Node> = PriorityQueue(compareBy { it.weight })
    val visited = mutableSetOf<NodeNoWeight>() // coords and repeats

    fun value(coords: Pair<Int, Int>): Int? = grid.getOrNull(coords.first)?.getOrNull(coords.second)

    frontier.add(startingNode)

    while (frontier.isNotEmpty()) {
        val (coords, direction, repeats, weight) = frontier.poll()!!
        if (NodeNoWeight(coords, direction, repeats) in visited) {
            continue
        }
        visited.add(NodeNoWeight(coords, direction, repeats))
        if (coords == goal) {
            return weight
        }
        if (repeats < 3 && direction != null) {
            val next = coords.next(direction)
            if (value(next) != null) {
                frontier.add(Node(next, direction, repeats + 1, weight + value(next)!!))
            }
        }
        for (newDirection in listOf("left", "right", "up", "down")) {
            if (newDirection == direction || areOpposite(direction, newDirection)) {
                continue
            }
            val next = coords.next(newDirection)
            if (value(next) != null) {
                frontier.add(Node(next, newDirection, 1, weight + value(next)!!))
            }
        }
    }
    println("wtf")
    return -1
//    return min(costSoFar[Edge(goal.left(), goal)]!!, costSoFar[Edge(goal.up(), goal)]!!)
}

fun areOpposite(direction: String?, newDirection: String): Boolean {
    if (direction == null) {
        return false
    }
    if (direction == "right") {
        return newDirection == "left"
    }
    if (direction == "left") {
        return newDirection == "right"
    }
    if (direction == "up") {
        return newDirection == "down"
    }
    if (direction == "down") {
        return newDirection == "up"
    }
    return false
}

private fun Pair<Int, Int>.next(direction: String): Pair<Int, Int> = when (direction) {
    "right" -> this.right()
    "left" -> this.left()
    "up" -> this.up()
    "down" -> this.down()
    else -> throw RuntimeException("bad one direction")
}

//fun edges(from: Edge, grid: MutableList<MutableList<Int>>): List<Pair<Edge, Int>> {
//    return listOf(
//        Edge(from.to, from.to.right()),
//        Edge(from.to, from.to.left()),
//        Edge(from.to, from.to.up()),
//        Edge(from.to, from.to.down())
//    ).filter { it != Edge(from.to, from.from) }
//        .filter { weight(grid, it.to) != null }
//}
//
//fun heuristic(goal: Edge, next: Edge): Int {
//    return abs(goal.to.first - next.to.first) + abs(goal.to.second - next.to.second)
//}
//
////def heuristic(a, b):
////# Manhattan distance on a square grid
////return abs(a.x - b.x) + abs(a.y - b.y)
//
//private fun pairs(
//    goal: Edge,
//    cameFrom: MutableMap<Edge, Edge?>
//): MutableSet<Pair<Int, Int>> {
//    var next = goal
//    val usedNodes = mutableSetOf<Pair<Int, Int>>()
//    usedNodes.add(next.to)
//    usedNodes.add(next.from)
//    while (cameFrom[next] != null) {
//        val value = cameFrom[next]!!
//        usedNodes.add(value.from)
//        usedNodes.add(value.to)
//        next = cameFrom[next]!!
//    }
//    return usedNodes
//}
//
//fun printMyThing(grid: MutableList<MutableList<Int>>, path: MutableSet<Pair<Int, Int>>) {
//    for ((y, row) in grid.withIndex()) {
//        for ((x, char) in row.withIndex()) {
//            if (path.contains(y to x)) {
//                print("x")
//            } else {
//                print(char)
//            }
//        }
//        println()
//    }
//}
//
//fun pathContainsThreeInRow(cameFrom: MutableMap<Edge, Edge?>, dest: Edge): Boolean {
//    val dest2 = cameFrom[dest]
//    val dest3 = cameFrom[dest]?.let { cameFrom[it] }
//    val dest4 = cameFrom[dest]?.let { cameFrom[it] }?.let { cameFrom[it] }
//
//    val dir = changeToDirection(dest)
//    val dir2 = dest2?.let { changeToDirection(it) }
//    val dir3 = dest3?.let { changeToDirection(it) }
//    val dir4 = dest4?.let { changeToDirection(it) }
//
//    return dir == dir2 && dir2 == dir3 && dir3 == dir4
////    var next = dest
////    val path = mutableListOf(next)
////    while (cameFrom[next] != null) {
////        path.add(cameFrom[next]!!)
////        next = cameFrom[next]!!
////    }
////
////    val directions = path.map { changeToDirection(it) }
////
////    val groups = count(directions)
////    return groups.any { it.count > 3 }
//}
//
//fun count(values: List<String>): List<Group> {
//    val groups = mutableListOf<Group>()
//    values.forEach {
//        val last = groups.lastOrNull()
//        if (last?.value == it) {
//            last.count++
//        } else {
//            groups.add(Group(it, 1))
//        }
//    }
//    return groups
//}
//
//data class Group(val value: String, var count: Int)
//
//fun changeToDirection(edge: Edge): String {
//    val pair1 = edge.from
//    val pair2 = edge.to
//    if (pair1.first - pair2.first == 1) {
//        return "a"
//    }
//    if (pair1.first - pair2.first == -1) {
//        return "b"
//    }
//    if (pair1.second - pair2.second == 1) {
//        return "c"
//    }
//    if (pair1.second - pair2.second == -1) {
//        return "d"
//    }
//    throw RuntimeException("Something not right")
//}

private fun Pair<Int, Int>.up() = this.first - 1 to this.second
private fun Pair<Int, Int>.down() = this.first + 1 to this.second
private fun Pair<Int, Int>.right() = this.first to this.second + 1
private fun Pair<Int, Int>.left() = this.first to this.second - 1