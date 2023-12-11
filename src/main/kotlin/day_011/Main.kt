package day_011

import java.io.File
import kotlin.math.abs
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
    solutionV1(array)
    solutionV2(array)
}

fun solutionV1(array: Array<CharArray>) {
    println("Solution V1")
    val galaxyMap = mutableListOf<MutableList<Char>>()

    for (row in array) {
        if (row.all { it == '.' }) {
            galaxyMap.add(row.toMutableList())
        }
        galaxyMap.add(row.toMutableList()) // góra-dół
    }
    for (x in array[0].indices.reversed()) {
        if (galaxyMap.all { (it[x] == '.') }) {
            for (index2 in galaxyMap.indices) {
                galaxyMap[index2].add(x, '.')
            }
        }
    }

    val allGalaxies = mutableListOf<Pair<Int, Int>>()
    for ((y, row) in galaxyMap.withIndex()) {
        for ((x, char) in row.withIndex()) {
            if (char == '#') {
                allGalaxies.add(Pair(y, x))
            }
        }
    }

    var distance = 0
    for (i in allGalaxies.indices) {
        for (j in i..<allGalaxies.size) {
            val g1 = allGalaxies[i]
            val g2 = allGalaxies[j]

            distance += abs(g1.first - g2.first) + abs(g1.second - g2.second)
        }
    }
    println(distance)
}

fun solutionV2(array: Array<CharArray>) {
    println("Solution V2")
    val galaxyMap = mutableListOf<MutableList<Char>>()
    val xValue = 1_000_000

    for (row in array) {
        if (row.all { it == '.' || it == 'x' }) {
            galaxyMap.add(row.toMutableList().map { 'x' }.toMutableList())
        } else {
            galaxyMap.add(row.toMutableList()) // góra-dół
        }
    }
    for (x in array[0].indices.reversed()) {
        if (galaxyMap.all { (it[x] == '.' || it[x] == 'x') }) {
            for (index2 in galaxyMap.indices) {
                galaxyMap[index2][x] = 'x'
            }
        }
    }

    val allGalaxies = mutableListOf<Pair<Int, Int>>()
    for ((y, row) in galaxyMap.withIndex()) {
        for ((x, char) in row.withIndex()) {
            if (char == '#') {
                allGalaxies.add(Pair(y, x))
            }
        }
    }

    var distance = 0L
    for (i in allGalaxies.indices) {
        for (j in i..<allGalaxies.size) {
            val g1 = allGalaxies[i]
            val g2 = allGalaxies[j]
            if (g1 == g2) {
                continue
            }

            var tempDistance = 0
            for (y in min(g1.first, g2.first).rangeUntil(max(g1.first, g2.first))) {
                if (galaxyMap[y][g1.second] == '.' || galaxyMap[y][g1.second] == '#') {
                    tempDistance += 1
                } else {
                    tempDistance += xValue
                }
            }
            for (x in min(g1.second, g2.second).rangeUntil(max(g1.second, g2.second))) {
                if (galaxyMap[g2.first][x] == '.' || galaxyMap[g2.first][x] == '#') {
                    tempDistance += 1
                } else {
                    tempDistance += xValue
                }
            }
            println("total temp distance=$tempDistance")
            distance += tempDistance.toLong()
            //exp: 374
            //exp: 9370588
        }
    }
    println(distance)
}