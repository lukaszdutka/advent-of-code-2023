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
//    solutionBetter(lines)
}

fun solutionBetter(lines: List<String>) {

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
    println("counter=${++counter}")
    val splitted = line.split(" ")
    val damagedTemplate = splitted[0]

    val schema = splitted[1].split(",").map { it.toInt() }

    val maxGears = schema.sum()

    val stack = Stack<Int>()
    schema.reversed().forEach { stack.push(it) }

    return calculateV2(damagedTemplate, stack, schema, 0, maxGears, 0)

}

private const val GEAR = '#'

fun calculateV2(
    myDamagedTemplate: String,
    schemaTemplate: Stack<Int>,
    unchangedSchema: List<Int>,
    accumulator: Int,
    maxGears: Int,
    startIndex: Int
): Int {
    var damagedTemplate = myDamagedTemplate
    val gears = damagedTemplate.count { it == GEAR }
    if (gears > maxGears) {
        return 0
    }
    if (schemaTemplate.isEmpty() || gears == maxGears) {
        return if (validFinish(damagedTemplate, unchangedSchema)) {
            1
        } else {
            0
        }
    }
    var sum = 0
    for (index in startIndex.rangeUntil(damagedTemplate.length)) {
        val char = damagedTemplate[index]
        if (char == '.') {
            continue
        }
        val numberOfGears = schemaTemplate.peek()
        val potentialPlacement = index
//            damagedTemplate.indexOfFirst { it == '?' || it == '#' }
        if (canReplace(damagedTemplate, potentialPlacement, numberOfGears)) {
            val replaced = damagedTemplate
                .replaceRange(
                    potentialPlacement,
                    potentialPlacement + numberOfGears,
                    "#".repeat(numberOfGears)
                )
            val schema = schemaTemplate.copy()
            schema.pop()
            sum += calculateV2(
                replaced,
                schema,
                unchangedSchema,
                accumulator,
                maxGears,
                index + numberOfGears
            )
        } else {
            if (damagedTemplate[potentialPlacement] == '?') {
                damagedTemplate = damagedTemplate.replaceRange(potentialPlacement, potentialPlacement + 1, ".")
            }
        }
    }

    return sum
}

fun canReplace(template: String, index: Int, numberOfGears: Int): Boolean {
    if (template.length < index + numberOfGears) {
        return false
    }
    if (template.getOrNull(index - 1) == '#') {
        return false
    }
    val substring = template.substring(index, index + numberOfGears)
    if (substring.all { it == '?' || it == '#' }) {
        return template.getOrNull(index + numberOfGears) != '#'
    } else {
        return false
    }
}

private fun <E> Stack<E>.copy(): Stack<E> {
    val stack = Stack<E>()
    stack.addAll(this)
    return stack
}

fun validFinish(damagedTemplate: String, schema: List<Int>): Boolean {
    var stringIndex = 0
    for (int in schema) {
        var schemaGears = int
        var gearsStarted = false
        while (schemaGears > 0) {
            val char = damagedTemplate[stringIndex]
            if (schemaGears == int && char == '#' && damagedTemplate.getOrElse(stringIndex - 1) { '.' } == '#') {
                return false
            }
            if (char == '#') {
                gearsStarted = true
                schemaGears--
            } else {
                if (gearsStarted) {
                    return false
                }
            }
            stringIndex++
        }
    }
    return true
}

fun slowValidFinish(damagedTemplate: String, schema: List<Int>): Boolean {
    val gearConsecutive = damagedTemplate.replace('?', '.')
        .split(".")
        .map { it.length }
        .filter { it > 0 }

    if (schema.size != gearConsecutive.size) {
        return false
    }
    return schema.zip(gearConsecutive).all { pair -> pair.first == pair.second }
}

//fun calculate(damagedTemplate: String, schema: List<Int>, maxGears: Int, accumulator: Int): Int {
//    val gears = damagedTemplate.count { it == GEAR }
//    val modifablePlaces = damagedTemplate.count { it == '?' }
//    if (gears > maxGears) {
//        return 0
//    }
//    if (gears == maxGears) {
//        if (validFinish(damagedTemplate, schema)) {
//            return 1
//        } else {
//            return 0
//        }
//    }
//    if (!validToFirstQuestionMark(damagedTemplate, schema)) {
//        return 0
//    }
//    if (gears + modifablePlaces < maxGears) {
//        return 0
//    }
//
//    val gearReplacement = damagedTemplate.replaceFirst('?', GEAR)
//    val nothingReplacement = damagedTemplate.replaceFirst('?', '.')
//
//    return calculate(gearReplacement, schema, maxGears, accumulator) +
//            calculate(nothingReplacement, schema, maxGears, accumulator)
//
//}

//fun validToFirstQuestionMark(damagedTemplate: String, schema: List<Int>): Boolean {
//    val stack = Stack<Int>()
//    schema.reversed().forEach { stack.push(it) }
//
//    var schemaValue = stack.pop()
//    var consecutiveGears = 0
//    var nextMustBeEmpty = false
//    for (char in damagedTemplate) {
//        if (char == '?') {
//            return true
//        }
//        if (nextMustBeEmpty && char == GEAR) {
//            return false
//        }
//        if (nextMustBeEmpty && char != GEAR) {
//            nextMustBeEmpty = false
//        }
//        if (char == GEAR) {
//            consecutiveGears++
//        } else {
//            if (consecutiveGears != schemaValue && consecutiveGears > 0) {
//                return false
//            }
//            consecutiveGears = 0
//        }
//        if (consecutiveGears > schemaValue) {
//            return false
//        }
//        if (consecutiveGears == schemaValue) {
//            nextMustBeEmpty = true
//            consecutiveGears = 0
//            if (stack.isEmpty()) {
//                return true
//            }
//            schemaValue = stack.pop()
//        }
//    }
//    return true
//}
