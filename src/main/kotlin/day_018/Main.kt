package day_018

import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private const val inputPath =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_018/input.txt"
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_018/input_small.txt"

//28538 - not right
fun main() {
    val lines = File(inputPath).readLines()

    println("v1=${solutionV1(lines)}")
//    println("v2=${solutionV2(inputs)}")
}

fun solutionV1(lines: List<String>): String {
    val moves = lines.map { it.split(" ")[0] to it.split(" ")[1].toInt() }
    var coords = 0 to 0
    val oldDiggedOut = mutableSetOf<Pair<Int, Int>>()

    for ((direction, steps) in moves) {
        var stepsLeft = steps
        while (stepsLeft > 0) {
            stepsLeft--
            coords = coords.next(direction)
            oldDiggedOut.add(coords)
        }
    }
    val smallestY = oldDiggedOut.minOfOrNull { it.first }!!
    val smallestX = oldDiggedOut.minOfOrNull { it.second }!!
    var shiftY = 0
    var shiftX = 0
    if (smallestY < 0) {
        shiftY = abs(smallestY)
    }
    if (smallestX < 0) {
        shiftX = abs(smallestX)
    }

    val diggedOut = oldDiggedOut.map { it.first + shiftY to it.second + shiftX }

    val gridY = diggedOut.maxOfOrNull { it.first }!!
    val gridX = diggedOut.maxOfOrNull { it.second }!!

    val map = mutableListOf<MutableList<String>>()
    for (y in 0..gridY) {
        val row = mutableListOf<String>()
        for (x in 0..gridX) {
            if (diggedOut.contains(y to x)) {
                row.add("#")
            } else {
                row.add(".")
            }
        }
        map.add(row)
    }

    map.prettyPrint()

    println(isPointInPolygon(6 to 5, diggedOut))
    val border = diggedOut.toList()

    for (y in 0..gridY) {
        for (x in 0..gridX) {
            if (isPointInPolygon(y to x, border)) {
                map[y][x] = "#"
            }
        }
    }
    println("new:")
    map.prettyPrint()

    val result = map.sumOf { it -> it.count { it == "#" } }

    return "$result"
}


private fun Pair<Int, Int>.next(direction: String): Pair<Int, Int> = when (direction) {
    "R" -> this.right()
    "L" -> this.left()
    "U" -> this.up()
    "D" -> this.down()
    else -> throw RuntimeException("bad one direction")
}

private fun Pair<Int, Int>.up() = this.first - 1 to this.second
private fun Pair<Int, Int>.down() = this.first + 1 to this.second
private fun Pair<Int, Int>.right() = this.first to this.second + 1
private fun Pair<Int, Int>.left() = this.first to this.second - 1


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
