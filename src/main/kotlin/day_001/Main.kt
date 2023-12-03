package day_001

import java.io.File

private const val inputPath = "src/main/kotlin/day_001/input1.txt"

fun main() {
    var count = 0
    var count2 = 0
    val lines = File(inputPath)
        .readLines()
    for (line in lines) {
        count += calculateLineV1(line)
        count2 += calculateLineV2(line)
    }
    println(count)
    println(count2)
}

fun calculateLineV1(line: String): Int {
    var first = 0
    for (character in line.toCharArray()) {
        if (Character.isDigit(character)) {
            first = character.digitToInt()
            break
        }
    }
    for (character in line.reversed().toCharArray()) {
        if (Character.isDigit(character)) {
            return 10 * first + character.digitToInt()
        }
    }
    return 0
}

fun calculateLineV2(line: String): Int {
    val map = mapOf(
        Pair("one", 1),
        Pair("two", 2),
        Pair("three", 3),
        Pair("four", 4),
        Pair("five", 5),
        Pair("six", 6),
        Pair("seven", 7),
        Pair("eight", 8),
        Pair("nine", 9),
        Pair("1", 1),
        Pair("2", 2),
        Pair("3", 3),
        Pair("4", 4),
        Pair("5", 5),
        Pair("6", 6),
        Pair("7", 7),
        Pair("8", 8),
        Pair("9", 9)
    )

    var firstOccurrenceIndex = Int.MAX_VALUE
    var firstOccurrenceValue = -1

    var lastOccurrenceIndex = -1
    var lastOccurrenceValue = -1

    for (entry in map) {
        val occurrence = line.indexOf(entry.key)
        if (occurrence > -1 && occurrence < firstOccurrenceIndex) {
            firstOccurrenceIndex = occurrence
            firstOccurrenceValue = entry.value
        }
        val occurrenceBack = line.lastIndexOf(entry.key)
        if (occurrenceBack > -1 && occurrenceBack > lastOccurrenceIndex) {
            lastOccurrenceIndex = occurrenceBack
            lastOccurrenceValue = entry.value
        }
    }
    return firstOccurrenceValue * 10 + lastOccurrenceValue
}