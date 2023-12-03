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