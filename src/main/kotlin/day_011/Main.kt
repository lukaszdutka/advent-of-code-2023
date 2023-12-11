package day_011

import java.io.File
import kotlin.math.max
import kotlin.math.min


private const val inputPath =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_011/input.txt"
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_011/input_small.txt"

fun main() {
    val lines = File(inputPath).readLines()

    val array = Array(lines.size) { CharArray(lines[0].length) }
    for ((i, line) in lines.withIndex()) {
        array[i] = line.toCharArray()
    }
    solutionV2(array, xValue = 2)
    solutionV2(array, xValue = 1_000_000)
}

fun solutionV2(array: Array<CharArray>, xValue: Int) {
    println("Solution for xValue=$xValue")
    val galaxyMap = mutableListOf<MutableList<Char>>()

    for (row in array) {
        if (row.all { it != '#' }) {
            galaxyMap.add(row.toMutableList().map { 'x' }.toMutableList())
        } else {
            galaxyMap.add(row.toMutableList()) // góra-dół
        }
    }
    for (x in array[0].indices.reversed()) {
        if (galaxyMap.all { (it[x] != '#') }) {
            for (index2 in galaxyMap.indices) {
                galaxyMap[index2][x] = 'x'
            }
        }
    }

    val allGalaxies = galaxyMap.flatMapIndexed { y, row ->
        row.mapIndexedNotNull { x, char -> if (char == '#') Pair(y, x) else null }
    }.toList()

    var distance = 0L
    for (i in allGalaxies.indices) {
        for (j in i..<allGalaxies.size) {
            val g1 = allGalaxies[i]
            val g2 = allGalaxies[j]

            for (y in min(g1.first, g2.first).rangeUntil(max(g1.first, g2.first))) {
                distance += (if (galaxyMap[y][g1.second] != 'x') 1 else xValue)
            }
            for (x in min(g1.second, g2.second).rangeUntil(max(g1.second, g2.second))) {
                distance += (if (galaxyMap[g2.first][x] != 'x') 1 else xValue)
            }
        }
    }
    println(distance)
}