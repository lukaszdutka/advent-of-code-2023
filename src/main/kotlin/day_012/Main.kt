package day_012

import java.io.File
import java.util.*

private const val inputPath =
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_012/input.txt"
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_012/input_small.txt"

fun main() {
    val lines = File(inputPath).readLines()

//    solutionV1(lines)
    val linesV2 = lines.map { v2Lines(it) }
    solutionV1(linesV2)
}

fun v2Lines(it: String): String {
    val split = it.split(" ")
    val gears = split[0]
    val numbers = split[1]
    return listOf(gears, gears, gears, gears, gears).reduce { a, b -> "$a?$b" } +
            " " +
            listOf(numbers, numbers, numbers, numbers, numbers).reduce { a, b -> "$a,$b" }
}

fun solutionV1(lines: List<String>) {
    println(lines.sumOf { possibilitiesV1(it) })
}

var counter = 0
fun possibilitiesV1(line: String): Int {
    println(++counter)
    val splitted = line.split(" ")
    val damagedTemplate = splitted[0]

    val schema = splitted[1].split(",").map { it.toInt() }

    val maxGears = schema.sum()

    return calculate(damagedTemplate, schema, maxGears, 0)

}

private const val GEAR = '#'

fun calculate(damagedTemplate: String, schema: List<Int>, maxGears: Int, accumulator: Int): Int {
    val gears = damagedTemplate.count { it == GEAR }
    val modifablePlaces = damagedTemplate.count { it == '?' }
    if (gears == maxGears) {
        if (valid(damagedTemplate, schema)) {
            return 1
        } else {
            return 0
        }
    }
    if (gears + modifablePlaces < maxGears) {
        return 0
    }

    val gearReplacement = damagedTemplate.replaceFirst('?', GEAR)
    val nothingReplacement = damagedTemplate.replaceFirst('?', '.')

    return calculate(gearReplacement, schema, maxGears, accumulator) +
            calculate(nothingReplacement, schema, maxGears, accumulator)

}

fun valid(damagedTemplate: String, schema: List<Int>): Boolean {
    val stack = Stack<Int>()
    schema.reversed().forEach { stack.push(it) }

    var schemaValue = stack.pop()
    var consecutiveGears = 0
    var nextMustBeEmpty = false
    for (char in damagedTemplate) {
        if (nextMustBeEmpty && char == GEAR) {
            return false
        }
        if (nextMustBeEmpty && char != GEAR) {
            nextMustBeEmpty = false
        }
        if (char == GEAR) {
            consecutiveGears++
        } else {
            if (consecutiveGears != schemaValue && consecutiveGears > 0) {
                return false
            }
            consecutiveGears = 0
        }
        if (consecutiveGears > schemaValue) {
            return false
        }
        if (consecutiveGears == schemaValue) {
            nextMustBeEmpty = true
            consecutiveGears = 0
            if (stack.isEmpty()) {
                return true
            }
            schemaValue = stack.pop()
        }

//        if (nextMustBeEmpty && char == GEAR) {
//            return false
//        }
//        if (nextMustBeEmpty && (char != GEAR)) {
//            nextMustBeEmpty = false
//            if (stack.isEmpty()) {
//                println("Reason= stack empty")
//                return true
//            }
//            schemaValue = stack.pop()
//        }
//        if (char == GEAR) {
//            consecutiveGears++
//        }
//        if (char != GEAR) {
//            consecutiveGears = 0
//        }
//        if (consecutiveGears < schemaValue) {
//            continue
//        }
//        if (consecutiveGears == schemaValue) {
//            nextMustBeEmpty = true
//            continue
//        }
//        if (consecutiveGears > schemaValue) {
//            return false
//        }
    }
    return true
}
