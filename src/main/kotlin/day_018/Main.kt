package day_018

import java.io.File
import java.math.BigDecimal
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private const val inputPath =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_018/input.txt"
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_018/input_small.txt"
//48503
//
//28538 - not right

//952408144115
//952404941483
fun main() {
    val lines = File(inputPath).readLines()

    println("v1=${solutionV1(lines)}")
//    println("v2=${solutionV2(inputs)}")
}

fun solutionV1(lines: List<String>): String {
//    val moves = lines.map { it.split(" ")[0] to it.split(" ")[1].toInt() }
    val moves = lines.map { parseInputLine(it) }
    var coords = 0 to 0
    val nodes = mutableListOf<Pair<Int, Int>>()

    nodes.add(coords)
    for ((direction, steps) in moves) {
        coords = coords.next(direction, steps)
        nodes.add(coords)
    }
//    println(nodes.size)
//    val sides = nodes.zipWithNext().sumOf { (a, b) -> abs(a.first - b.first) + abs(a.second - b.second) - 1 }
//    println(sides)
    val polygonArea = polygonArea(nodes, nodes.size)

//    println(952408144115 - 952404941483)
    val additionalArea =
        nodes.zipWithNext().sumOf { (a, b) -> abs(a.first - b.first) + abs(a.second - b.second) } / 2 + 1
    println(additionalArea)
    return "${polygonArea + additionalArea.toBigDecimal()}"
}

fun parseInputLine(it: String): Pair<String, Int> {
    val string = it.split(" ")[2]
        .replace("(#", "")
        .replace(")", "")

    val directionNumber = string.substring(string.length - 1).toInt()
    val steps = string.substring(0, string.length - 1)

    val stepsDecimal = Integer.parseInt(steps, 16)
    if (directionNumber == 0) {
        return "R" to stepsDecimal
    }
    if (directionNumber == 1) {
        return "D" to stepsDecimal
    }
    if (directionNumber == 2) {
        return "L" to stepsDecimal
    }
    if (directionNumber == 3) {
        return "U" to stepsDecimal
    }
    throw RuntimeException("Something bad.")
}


private fun Pair<Int, Int>.next(direction: String, steps: Int = 1): Pair<Int, Int> = when (direction) {
    "R" -> this.right(steps)
    "L" -> this.left(steps)
    "U" -> this.up(steps)
    "D" -> this.down(steps)
    else -> throw RuntimeException("bad one direction")
}

private fun Pair<Int, Int>.up(steps: Int = 1) = this.first - steps to this.second
private fun Pair<Int, Int>.down(steps: Int = 1) = this.first + steps to this.second
private fun Pair<Int, Int>.right(steps: Int = 1) = this.first to this.second + steps
private fun Pair<Int, Int>.left(steps: Int = 1) = this.first to this.second - steps


private fun <T> List<List<T>>.prettyPrint() {
    for (line in this) {
        println(line.joinToString(separator = ""))
    }
}

fun isPointInPolygon(p: Pair<Int, Int>, polygon: List<Pair<Int, Int>>): Boolean {
    var minX: Double = polygon[0].first.toDouble()
    var maxX: Double = polygon[0].first.toDouble()
    var minY: Double = polygon[0].second.toDouble()
    var maxY: Double = polygon[0].second.toDouble()
    for (i in 1..<polygon.size) {
        val q = polygon[i]
        minX = min(q.first.toDouble(), minX)
        maxX = max(q.first.toDouble(), maxX)
        minY = min(q.second.toDouble(), minY)
        maxY = max(q.second.toDouble(), maxY)
    }
    if (p.first < minX || p.first > maxX || p.second < minY || p.second > maxY) {
        return false
    }

    // https://wrf.ecse.rpi.edu/Research/Short_Notes/pnpoly.html
    var inside = false
    var i = 0
    var j: Int = polygon.size - 1
    while (i < polygon.size) {
        if (polygon[i].second > p.second != polygon[j].second > p.second &&
            p.first < (polygon[j].first - polygon[i].first) * (p.second - polygon[i].second) / (polygon[j].second - polygon[i].second) + polygon[i].first
        ) {
            inside = !inside
        }
        j = i++
    }
    return inside
}

// (X[i], Y[i]) are coordinates of i'th point.
fun polygonArea(nodes: List<Pair<Int, Int>>, n: Int): BigDecimal {
    // Initialize area
    var area: BigDecimal = BigDecimal.ZERO

    // Calculate value of shoelace formula
    var j = n - 1
    for (i in 0..<n) {
        area +=
            (nodes[j].second.toBigDecimal() + nodes[i].second.toBigDecimal()) *
                    (nodes[j].first.toBigDecimal() - nodes[i].first.toBigDecimal())

        // j is previous vertex to i
        j = i
    }

    // Return absolute value
    return (area / 2.toBigDecimal()).abs()
}
