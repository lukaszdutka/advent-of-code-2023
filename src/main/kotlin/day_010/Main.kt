package day_010

import java.io.File
import java.lang.RuntimeException
import java.util.*

private const val inputPath =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_010/input.txt"
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_010/input_small.txt"

//S is the starting position of the animal; there is a pipe on this tile, but your sketch doesn't show what shape the pipe has.
fun main() {
    val lines = File(inputPath).readLines()

    val array = Array(lines.size) { CharArray(lines[0].length) }
    for ((i, line) in lines.withIndex()) {
        array[i] = line.toCharArray()
    }
    val pipes = solutionV1(array)
    solutionV2(array, pipes)
}

val validPipesForDirection = mutableMapOf<String, Set<Char>>()
fun solutionV1(pipesMap: Array<CharArray>): MutableSet<Pair<Int, Int>> {
    val startY = 107
    val startX = 110
//    val startY = 2
//    val startX = 0
    pipesMap[startY][startX] = 'F'

    validPipesForDirection["down"] = setOf('|', 'L', 'J')
    validPipesForDirection["up"] = setOf('|', 'F', '7')
    validPipesForDirection["left"] = setOf('-', 'F', 'L')
    validPipesForDirection["right"] = setOf('-', 'J', '7')

    val toVisit = Stack<Node>()

    toVisit.addElement(Node("S", startY, startX, 0))
//    toVisit.addElement(Node("down", 107+1, 110 + 1, 1))
//    toVisit.addElement(Node("right", 107 , 110, 1))
    val processedValidNode = mutableMapOf<Pair<Int, Int>, Int>()

    val onlyRealValidPipesForV2 = mutableSetOf<Pair<Int, Int>>()
    while (!toVisit.isEmpty()) {
        val node = toVisit.pop()!!
        if (processedValidNode.contains(Pair(node.y, node.x))) {
            continue
        }
        val validNodes = listOf(
            Node("down", node.y + 1, node.x, node.distance + 1),
            Node("up", node.y - 1, node.x, node.distance + 1),
            Node("left", node.y, node.x - 1, node.distance + 1),
            Node("right", node.y, node.x + 1, node.distance + 1)
        ).filter { isValid(pipesMap, it) }
        toVisit.addAll(validNodes)
        onlyRealValidPipesForV2.addAll(validNodes.map { Pair(it.y, it.x) })
        processedValidNode[Pair(node.y, node.x)] = node.distance
    }
    println((processedValidNode.values.max() + 1) / 2)
    return onlyRealValidPipesForV2
}

fun isValid(pipesMap: Array<CharArray>, node: Node): Boolean {
    if (node.x < 0 || node.y < 0 || node.x >= pipesMap.size || node.y >= pipesMap.size) {
        return false
    }
    return validPipesForDirection[node.direction]!!.contains(pipesMap[node.y][node.x])
}

class Node(val direction: String, val y: Int, val x: Int, val distance: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Node

        if (y != other.y) return false
        if (x != other.x) return false

        return true
    }

    override fun hashCode(): Int {
        var result = y
        result = 31 * result + x
        return result
    }

    override fun toString(): String {
        return "Node(direction='$direction', y=$y, x=$x, distance=$distance)"
    }


}

fun solutionV2(oldArray: Array<CharArray>, usedPipes: MutableSet<Pair<Int, Int>>) {
//    println(array.fold("") { acc, row1 -> acc + "\n" + row1.fold("") { a, b -> a + "" + b } })
    val lines = newMap(oldArray, usedPipes).split("\n")

    val array = Array(lines.size) { CharArray(lines[0].length) }
    for ((i, line) in lines.withIndex()) {
        array[i] = line.toCharArray()
        println(line)
    }

    //todo think
    val midY = 107 * 3 + 1
    val midX = 110 * 3 + 1
    val startY = midY - 1
    val startX = midX + 1
    //26

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
        if (value == '0') {
            toCheck.push(Pair(coords.first + 1, coords.second))
            toCheck.push(Pair(coords.first - 1, coords.second))
            toCheck.push(Pair(coords.first, coords.second + 1))
            toCheck.push(Pair(coords.first, coords.second - 1))
            continue
        }
        if (value == '1') {
            counter += 1
            continue
        }
    }
    println("counter= $counter")
    println("counter/9= ${counter / 9}")
}

private fun newMap(
    array: Array<CharArray>,
    usedPipes: MutableSet<Pair<Int, Int>>
): String {
    for ((y, row) in array.withIndex()) {
        for ((x, _) in row.withIndex()) {
            if (!usedPipes.contains(Pair(y, x))) {
                array[y][x] = '.'
            }
        }
    }

//    println(array.fold("") { acc, row1 -> acc + "\n" + row1.fold("") { a, b -> a + "" + b } })

    var startingString = ""
    for (row in array) {
        var array0 = ""
        var array1 = ""
        var array2 = ""

        for (char in row) {
            val fValue = stringValue(char)
            array0 += fValue.split("\n")[0]
            array1 += fValue.split("\n")[1]
            array2 += fValue.split("\n")[2]
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