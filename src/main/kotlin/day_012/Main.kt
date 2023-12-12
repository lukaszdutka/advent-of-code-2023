package day_012

import java.io.File

private const val inputPath =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_012/input.txt"
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_012/input_small.txt"

fun main() {
    val lines = File(inputPath).readLines()

    val linesV2 = lines.map { v2Lines(it) }

    println(part1(lines))
    println(part1(linesV2))
}

fun part1(lines: List<String>): Long {
    val input = lines.map { it.split(" ") }.map { Pair(it[0], it[1]) }
    var sum = 0L
    for (pair in input) {
        sum += calculate(pair.first, pair.second)
    }
    return sum
}

fun v2Lines(it: String): String {
    val split = it.split(" ")
    val gears = split[0]
    val numbers = split[1]
    return listOf(gears, gears, gears, gears, gears).reduce { a, b -> "$a?$b" } +
            " " +
            listOf(numbers, numbers, numbers, numbers, numbers).reduce { a, b -> "$a,$b" }
}

fun calculate(string: String, numbers: String): Long {
    val nums = numbers.split(",").map { it.toInt() }
    val cache = mutableMapOf<Pair<Int, Int>, Long>()

    return recursion(0, 0, nums, string, cache)
}

fun recursion(
    startIndex: Int,
    numsIndex: Int,
    nums: List<Int>,
    string: String,
    cache: MutableMap<Pair<Int, Int>, Long>
): Long {
    if (numsIndex == nums.size) {
        return if (string.substring(startIndex - 1).none { c -> c == '#' }) {
            1L
        } else {
            0L
        }
    }
    if (startIndex >= string.length) {
        return 0L
    }
    val pair = Pair(startIndex, numsIndex)
    if (cache[pair] != null) {
        return cache[pair]!!
    }
    var placed = 0L
    if (canPlace(startIndex, nums[numsIndex], string)) {
        placed = recursion(startIndex + nums[numsIndex] + 1, numsIndex + 1, nums, string, cache)
    }
    var notPlaced = 0L
    if (string[startIndex] != '#') {
        notPlaced = recursion(startIndex + 1, numsIndex, nums, string, cache)
    }
    cache[pair] = placed + notPlaced
    return placed + notPlaced
}

fun canPlace(index: Int, length: Int, string: String): Boolean {
    return blockWillFit(index, length, string) && endOfStringOrNoGearAtEnd(index, length, string)
}

fun blockWillFit(index: Int, length: Int, string: String): Boolean {
    for (i in index..<(index + length)) {
        if (i >= string.length || string[i] == '.') {
            return false
        }
    }
    return true
}

fun endOfStringOrNoGearAtEnd(index: Int, length: Int, string: String): Boolean {
    return index + length == string.length || string[index + length] != '#'
}

