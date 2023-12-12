package day_012

import java.io.File
import java.util.*

private const val inputPath =
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_012/input.txt"
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_012/input_small.txt"

fun main() {
    val lines = File(inputPath).readLines()

//    myCheck("???.### 1,1,3", 1, "first case failed")
    myCheck(".??..??...?##. 1,1,3", 4, "second case failed")
//    myCheck("?#?#?#?#?#?#?#? 1,3,1,6", 1, "third case failed")
//    myCheck("????.######..#####. 1,6,5", 4, "fourth case failed")
//    myCheck("?###???????? 3,2,1", 10, "fifth case failed")
//    solutionV1(lines)
    val linesV2 = lines.map { v2Lines(it) }
//    solutionV1(linesV2)
//    solutionBetter(lines)
//    solutionBetter(linesV2)
}

fun myCheck(value: String, expected: Int, message: String) {
    val calculated = calculatePaper(value)
    if (calculated != expected) {
        println("For string: $value")
        println("FAIL $message => expected=$expected, but got calculated=$calculated")
    } else {
        println("For string: $value")
        println("SUCCESS => expected == calculated ($expected)")
    }
}

//according to thing
private const val WHITE = 0
private const val BLACK = 1
private const val UNKNOWN = 2

fun calculatePaper(line: String): Int {
    val splitted = line.split(" ")

//    val dTemp = calculateDescriptions(splitted)
//    val d = IntArray(dTemp.size + 1)
//    d[0] = 0
//    dTemp.copyInto(d, 1)
    val dFirst = calculateDescriptions(splitted)
    val solvedLineFirst = calculateSolvedLine(splitted) //partial colouring, if not everythink known
    val d = IntArray(dFirst.size + 1)
    dFirst.copyInto(d, 1)
    d[0] = -99
    val solvedLine = IntArray(solvedLineFirst.size + 1)
    solvedLineFirst.copyInto(solvedLine, 1)
    solvedLine[0] = -99

    // n - length of line
    // k - number of blocks of black cells (d.size)
    val n = solvedLine.size - 1
    val k = d.size - 1

    val sol = Array(n + 1) { IntArray(k + 1) { -1 } }

    // d[j] -> j'ty element d/description
    // 1>1(j) "returns 1 if to the left of block j there must be at least one white cell"
    // (?) - usually 1, or not if it's start of array

//    sol[0][0] = -1
    val solve = solve(n, k, sol, d, solvedLine)
//    println(
//        solvedLine.joinToString(separator = "")
//            .replace("-1", "?")
//            .replace("0", ".")
//            .replace("1", "#")
//    )
    return solve
}

private fun atLeastOneWhiteCell(j: Int): Int = if (j == 1) 0 else 1

private fun calculateDescriptions(splitted: List<String>) =
    splitted[1].split(",").map { it.toInt() }.toIntArray()

private fun calculateSolvedLine(splitted: List<String>) = splitted[0].split("").mapNotNull {
    when (it) {
        "?" -> -1
        "#" -> BLACK
        "." -> WHITE
        else -> null
    }
}.toMutableList()
    .toIntArray()


fun solve(i: Int, j: Int, sol: Array<IntArray>, d: IntArray, solvedLine: IntArray): Int {
    if (i < 0 || j < 0) {
        return 0
    }
    if (i == 0 && j == 0) {
        return 1
    }
    if (sol[i][j] != -1) {
        val value = sol[i][j]
        return value
    }
    sol[i][j] = 0
    if (solve(i - 1, j, sol, d, solvedLine) > 0 && solvedLine[i] != BLACK) {
        updateCellColour(i, WHITE, solvedLine) //i index of cell
        val value = sol[i][j]
        val solved = solve(i - 1, j, sol, d, solvedLine)
        sol[i][j] = value + solved
    }
    if (solve(i - d[j] - atLeastOneWhiteCell(j), j - 1, sol, d, solvedLine) > 0 && canPlaceBlock(i, j, d, solvedLine)) {
        updateBlockColour(i, j, BLACK, d, solvedLine)
        sol[i][j] = sol[i][j] + solve(i - d[j] - atLeastOneWhiteCell(j), j - 1, sol, d, solvedLine)
    }
    return sol[i][j]
}

fun canPlaceBlock(i: Int, j: Int, d: IntArray, solvedLine: IntArray): Boolean {
    val untilExcluded = i - d[j]
    for (m in i.rangeUntil(untilExcluded)) {
        if (solvedLine[m] == WHITE) {
            return false
        }
    }
    if (atLeastOneWhiteCell(j) == 1 && solvedLine[untilExcluded] == BLACK) {
        return false
    }
    return true
}

fun updateBlockColour(i: Int, j: Int, color: Int, d: IntArray, solvedLine: IntArray) {
    val numberOfGears = d[j]
    val untilExcluded = i - numberOfGears
    for (m in i.rangeUntil(untilExcluded)) {
        solvedLine[m] = color
    }
    if (atLeastOneWhiteCell(j) == 1) {
        solvedLine[i - 1] = WHITE
    }
}


fun updateCellColour(i: Int, color: Int, solvedLine: IntArray) {
    if (solvedLine[i] == -1) {
        solvedLine[i] = color
    } else if (solvedLine[i] != color) {
        solvedLine[i] = UNKNOWN // conflict, both colors possible
    } // no update
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
