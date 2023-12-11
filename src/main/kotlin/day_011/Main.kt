package day_011

import java.io.File
import kotlin.math.abs


private const val inputPath =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_011/input.txt"
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_011/input_small.txt"

fun main() {
    val lines = File(inputPath).readLines()

    val array = Array(lines.size) { CharArray(lines[0].length) }
    for ((i, line) in lines.withIndex()) {
        array[i] = line.toCharArray()
    }
//    solutionV1(array)
    solutionV2(array)
}

//fun solutionV1(array: Array<CharArray>) {
//    val galaxyMap = mutableListOf<MutableList<Char>>()
//
//    for (row in array) {
//        if (row.all { it == '.' }) {
//            galaxyMap.add(row.toMutableList())
//        }
//        galaxyMap.add(row.toMutableList()) // góra-dół
//    }
//    for (x in array[0].indices.reversed()) {
//        if (galaxyMap.all { (it[x] == '.') }) {
//            for (index2 in galaxyMap.indices) {
//                galaxyMap[index2].add(x, '.')
//            }
//        }
//    }
//
//    val allGalaxies = mutableListOf<Pair<Int, Int>>()
//    for ((y, row) in galaxyMap.withIndex()) {
//        for ((x, char) in row.withIndex()) {
//            if (char == '#') {
//                allGalaxies.add(Pair(y, x))
//            }
//        }
//    }
//
//    var distance = 0
//    for (i in allGalaxies.indices) {
//        for (j in i..<allGalaxies.size) {
//            val g1 = allGalaxies[i]
//            val g2 = allGalaxies[j]
//
//            distance += abs(g1.first - g2.first) + abs(g1.second - g2.second)
//        }
//    }
//    println(distance)
//}

fun solutionV2(array: Array<CharArray>) {
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