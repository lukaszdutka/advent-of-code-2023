package day_010

import java.io.File
import java.lang.RuntimeException
import java.util.*

private const val inputPath =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_010/input.txt"
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_010/input_small.txt"

fun main() {
    val lines = File(inputPath).readLines()

    val array = Array(lines.size) { CharArray(lines[0].length) }
    for ((i, line) in lines.withIndex()) {
        array[i] = line.toCharArray()
    }
    val pipes = solutionV1(array)
    solutionV2(array, pipes)
}

fun solutionV1(pipesMap: Array<CharArray>): MutableSet<Pair<Int, Int>> {
    val start = Pair(107, 110)
    pipesMap[start.first][start.second] = 'F'

    val toVisit = Stack<Node>()
    toVisit.addElement(Node("S", start, 0))

    val coordToDistance = mutableMapOf<Pair<Int, Int>, Int>()
    while (toVisit.isNotEmpty()) {
        val node = toVisit.pop()!!
        val coords = node.coords
        if (coordToDistance.contains(coords)) {
            continue
        }
        val validNodes = listOf(
            Node("down", Pair(coords.first + 1, coords.second), node.distance + 1),
            Node("up", Pair(coords.first - 1, coords.second), node.distance + 1),
            Node("left", Pair(coords.first, coords.second - 1), node.distance + 1),
            Node("right", Pair(coords.first, coords.second + 1), node.distance + 1)
        ).filter { isValid(pipesMap, it, node.coords) }
        toVisit.addAll(validNodes)
        coordToDistance[coords] = node.distance
    }
    println("solutionV1=${(coordToDistance.values.max() + 1) / 2}")
    return coordToDistance.keys
}

fun isValid(pipesMap: Array<CharArray>, node: Node, cameFromHere: Pair<Int, Int>): Boolean {
    val coords = node.coords
    if (coords.first !in pipesMap.indices || coords.second !in pipesMap.indices) {
        return false
    }
    return when (val char = pipesMap[cameFromHere.first][cameFromHere.second]) {
        'F' -> listOf("right", "down").contains(node.direction)
        'L' -> listOf("right", "up").contains(node.direction)
        'J' -> listOf("left", "up").contains(node.direction)
        '7' -> listOf("left", "down").contains(node.direction)
        '|' -> listOf("up", "down").contains(node.direction)
        '-' -> listOf("right", "left").contains(node.direction)
        else -> throw RuntimeException("invalid char: $char")
    }
}

class Node(val direction: String, val coords: Pair<Int, Int>, val distance: Int)

fun solutionV2(oldArray: Array<CharArray>, usedPipes: MutableSet<Pair<Int, Int>>) {
    val lines = newMap(oldArray, usedPipes).split("\n")
    val array = Array(lines.size) { CharArray(lines[0].length) }
    for ((i, line) in lines.withIndex()) {
        array[i] = line.toCharArray()
    }

    //todo think
    val midY = 107 * 3 + 1
    val midX = 110 * 3 + 1
    val startY = midY - 1
    val startX = midX - 1
    //+1+1 error
    //-1-1 52
    //-1+1 52
    //+1-1 52

    val checkedCoords = mutableSetOf<Pair<Int, Int>>()
    val toCheck = Stack<Pair<Int, Int>>()
    toCheck.push(Pair(startY, startX))
    var counter = 0

    while (toCheck.isNotEmpty()) {
        val coords = toCheck.pop()
        if (checkedCoords.contains(coords)) {
            continue
        }
        checkedCoords.add(coords)
        val value = array[coords.first][coords.second]
        if (value == 'x') {
            continue
        }
        if (value == '1') {
            counter += 1
        }
        toCheck.push(Pair(coords.first + 1, coords.second))
        toCheck.push(Pair(coords.first - 1, coords.second))
        toCheck.push(Pair(coords.first, coords.second + 1))
        toCheck.push(Pair(coords.first, coords.second - 1))
    }
    println("soultionV2=${counter / 9}")
}

private fun newMap(
    array: Array<CharArray>,
    usedPipes: MutableSet<Pair<Int, Int>>
): String {
    for ((y, row) in array.withIndex()) {
        for (x in row.indices) {
            if (!usedPipes.contains(Pair(y, x))) {
                array[y][x] = '.'
            }
        }
    }

    var startingString = ""
    for (row in array) {
        var array0 = ""
        var array1 = ""
        var array2 = ""

        for (char in row) {
            val charLines = stringValue(char).split("\n")
            array0 += charLines[0]
            array1 += charLines[1]
            array2 += charLines[2]
        }
        startingString += array0 + "\n"
        startingString += array1 + "\n"
        startingString += array2 + "\n"
    }
    return startingString
}

private fun stringValue(char: Char) =
    when (char) {
        '.' -> "111\n111\n111"
        '|' -> "0x0\n0x0\n0x0"
        '-' -> "000\nxxx\n000"
        'F' -> "000\n0xx\n0x0"
        '7' -> "000\nxx0\n0x0"
        'L' -> "0x0\n0xx\n000"
        'J' -> "0x0\nxx0\n000"
        else -> throw RuntimeException("unsupported char: $char")
    }