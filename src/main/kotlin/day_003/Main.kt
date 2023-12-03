package day_003

import java.io.File

private const val inputPath =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_003/input.txt"
//private const val inputPath =
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_003/input_small.txt"

fun main() {
    val lines = File(inputPath)
        .readLines()

    val array = Array(lines.size) { CharArray(lines[0].length) }
    for ((i, line) in lines.withIndex()) {
        array[i] = line.toCharArray()
    }
    solutionV1(array)
    solutionV2(array)
}

fun solutionV2(array: Array<CharArray>) {
    var result = 0
    for ((x, row) in array.withIndex()) {
        for ((y, char) in row.withIndex()) {
            if (char == '*') {
                result += tryCreateGear(x, y, array)
            }
        }
    }
    println(result)
}

fun tryCreateGear(xGear: Int, yGear: Int, array: Array<CharArray>): Int {
    var numberSoFar = ""
    var isNumberValid = false

    val adjacentNumbers = mutableListOf<Int>()

    for (x in xGear - 1..xGear + 1) {
        for ((y, char) in array[x].withIndex()) {
            if (char.isDigit()) {
                numberSoFar += char
                isNumberValid = isNumberValid || isNearPotentialGear(x, y, xGear, yGear)
                continue
            }
            if (isNumberValid) {
                adjacentNumbers.add(numberSoFar.toInt())
                numberSoFar = ""
                isNumberValid = false
            } else {
                numberSoFar = ""
                isNumberValid = false
            }

        }
        if (isNumberValid) {
            adjacentNumbers.add(numberSoFar.toInt())
            numberSoFar = ""
            isNumberValid = false
        } else {
            numberSoFar = ""
            isNumberValid = false
        }
    }
    if (adjacentNumbers.size == 2) {
        return adjacentNumbers[0] * adjacentNumbers[1]
    }
    return 0
}

fun isNearPotentialGear(x: Int, y: Int, xGear: Int, yGear: Int): Boolean {
    val xRange = xGear - 1..xGear + 1
    val yRange = yGear - 1..yGear + 1
    return xRange.contains(x) && yRange.contains(y)
}

private fun solutionV1(array: Array<CharArray>) {
    val validNumbersMap = mutableSetOf<Pair<Int, Int>>()

    for ((x, row) in array.withIndex()) {
        for ((y, char) in row.withIndex()) {
            if (char != '.' && !char.isDigit()) {
                validNumbersMap.add(Pair(x - 1, y - 1))
                validNumbersMap.add(Pair(x - 1, y))
                validNumbersMap.add(Pair(x - 1, y + 1))
                validNumbersMap.add(Pair(x, y - 1))
                validNumbersMap.add(Pair(x, y + 1))
                validNumbersMap.add(Pair(x + 1, y - 1))
                validNumbersMap.add(Pair(x + 1, y))
                validNumbersMap.add(Pair(x + 1, y + 1))

            }
        }
    }

    var result = 0

    var numberSoFar = ""
    var isNumberValid = false
    for ((x, row) in array.withIndex()) {
        for ((y, char) in row.withIndex()) {
            if (char.isDigit()) {
                numberSoFar += char
                isNumberValid = isNumberValid || validNumbersMap.contains(Pair(x, y))
                continue
            }
            if (isNumberValid) {
                result += numberSoFar.toInt()
                numberSoFar = ""
                isNumberValid = false
            } else {
                numberSoFar = ""
                isNumberValid = false
            }

        }
        if (isNumberValid) {
            result += numberSoFar.toInt()
            numberSoFar = ""
            isNumberValid = false
        } else {
            numberSoFar = ""
            isNumberValid = false
        }
    }
    println(result)
}