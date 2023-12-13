package day_013

import java.io.File

private const val inputPath =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_013/input.txt"
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_013/input_small.txt"

fun main() {
    val lines = File(inputPath).readLines()
    val inputs = mutableListOf<MutableList<String>>()

    var linesSoFar = mutableListOf<String>()
    for (line in lines) {
        if (line == "") {
            inputs.add(linesSoFar)
            linesSoFar = mutableListOf()
        } else {
            linesSoFar.add(line)
        }
    }

    println(solve(inputs))
}

fun solve(inputs: MutableList<MutableList<String>>): Long {
    var sum = 0L
//    inputs.sumOf { calculateSingle(it) }
    for ((i, input) in inputs.withIndex()) {
        println(i)
        sum += calculateSingle(i, input);
    }

    return sum
}

// add (columns to the left) or (100* rows above)
fun calculateSingle(id: Int, pattern: List<String>): Long {

    val array = mutableListOf<List<String>>()
    for (line in pattern) {
        array.add(line.split("").filter { it.isNotBlank() })
    }

    // '.' '#'

    val possibleMirrorsColumns = mutableListOf<Int>()
    for (mirrorPlacement in 1.rangeUntil(array[0].size)) {
        val list = array.map { line -> isMirrorPossibleColumn(line, mirrorPlacement) }
        if (list.all { it.first } && list.count { it.second } == 1) {
            possibleMirrorsColumns.add(mirrorPlacement)
        }
    }

    val possibleMirrorsRows = mutableListOf<Int>()
    for (flatMirrorPlacement in 1.rangeUntil(array.size)) {
        val list = 0.rangeUntil(array[0].size)
            .map { columnIndex -> isMirrorPossibleRow(array, columnIndex, flatMirrorPlacement) }
        if (list.all { it.first } && list.count { it.second } == 1) {
            possibleMirrorsRows.add(flatMirrorPlacement)
        }
    }


    val rows = possibleMirrorsRows.sumOf { it * 100 }
    val columns = possibleMirrorsColumns.sumOf { it.toLong() }
    if (rows + columns == 0L) {
        println("XD")
    }
    return rows + columns
}

//bad: 26293
//bad: 28493
//bad: 23383
// 27505?

fun isMirrorPossibleRow(
    array: MutableList<List<String>>,
    columnIndex: Int,
    mirrorPlacement: Int
): Pair<Boolean, Boolean> {
//    od 1 do ostatni valid index
    // jeśli mirrorPlacement = 1, to
    var shift = 1
    var upIndex = mirrorPlacement - shift
    var downIndex = mirrorPlacement - 1 + shift
    var up = array.getOrNull(upIndex)?.get(columnIndex)
    var down = array.getOrNull(downIndex)?.get(columnIndex)
    var smudgeFound = false;
    while (down != null && up != null) {
        if (down != up) {
            if (smudgeFound) {
                return Pair(false, true)
            } else {
                smudgeFound = true
            }
        }
        shift++
        upIndex = mirrorPlacement - shift
        downIndex = mirrorPlacement - 1 + shift
        up = array.getOrNull(upIndex)?.get(columnIndex)
        down = array.getOrNull(downIndex)?.get(columnIndex)
    }
    return Pair(true, smudgeFound)
}

fun isMirrorPossibleColumn(line: List<String>, mirrorPlacement: Int): Pair<Boolean, Boolean> {
    //if mirror placement = 1, oznacza to, że jest po prawej od kolumny 0
    // czyli pierwsze elementy to 0 i 1
    //if mirror placement = array.size-1 (ostatni index) czyli jest po prawej od kolumny przedostatniindex
    // czyli pierwsze elementy to przedostatni index i ostatni index
    var shift = 1
    var leftIndex = mirrorPlacement - shift
    var rightIndex = mirrorPlacement - 1 + shift
    var left = line.getOrNull(leftIndex)
    var right = line.getOrNull(rightIndex)
    var smudgeFound = false
    while (right != null && left != null) {
        if (right != left) {
            if (smudgeFound) {
                return Pair(false, true)
            } else {
                smudgeFound = true
            }
        }
        shift++
        leftIndex = mirrorPlacement - shift
        rightIndex = mirrorPlacement - 1 + shift
        left = line.getOrNull(leftIndex)
        right = line.getOrNull(rightIndex)
    }
    return Pair(true, smudgeFound)
}