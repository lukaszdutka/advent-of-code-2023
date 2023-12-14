package day_014

import java.io.File


private const val inputPath =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_014/input.txt"
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_014/input_small.txt"

// 414162 - too high
// 412683 - too high

//  96135 - bad
// 76 + 308x = 1_000_000_000
// 1mld - 76 / 308 = x
// 1000 + 308x =

// 97436 - bad
// 95555 ?
// 86248 - bad
// 86069 - GOOD

fun main() {
    val lines = File(inputPath).readLines()
    val inputs = mutableListOf<MutableList<String>>()
    val inputs2 = mutableListOf<MutableList<String>>()

    for (line in lines) {
        inputs.add(line.split("").filter { it.isNotBlank() }.toMutableList())
        inputs2.add(line.split("").filter { it.isNotBlank() }.toMutableList())
    }
//    println(solveV1(inputs))

//    println(1689 - 1381) // 308
//    println(1_000_000_000 % 308) // 76


// r = 28
// a1 = ?
// an = 1_000_000_000
// a1 + 28x = 1_000_000_000
// 1_000_000_000%28 = ?

//    println(iterations % 28) // 20
    // 20 + x28 = 1_000_000_000
    // a1 = 20

//    val r = 1728 - 1420
//    println(r)
//    val reszta = iterations % r
//    println(reszta)

    val jump = 1613 - 1536
    println(jump)
    val first = iterations - (iterations / jump) * jump
    println(first)

    println(solveV2(inputs2))

    // score for 76?

    //76
    //532 - 224 = 308
    // 308x = 1_000_000_000-224
    // score = 97436
}

//private const val iterations = 5
private const val iterations = 1_000_000_000

fun solveV2(lines: MutableList<MutableList<String>>): Int {
    val hashMap = HashMap<String, Int>()

    for (cycle in 1..iterations) {
        shiftRocksUp(lines) //wypycha na north
        for (rotations in 1..3) {
            rotateClockwise(lines)
            shiftRocksUp(lines) //wypycha na West, South, East (East do góry)
        }
        rotateClockwise(lines) //rotate w prawo, East z góry przechodzi na lewo (czyli u góry north)
        val points = calculatePoints(lines) // liczy punkty dla North u góry (kule są po prawej)
        val forCache = lines.fold("") { acc, row -> acc + row.reduce { a, b -> a + b } }

//        println("After $cycle cycles:")
//        lines.prettyPrint()


        if (cycle == 76 || cycle == 76 + 77 || cycle == 76 + 77 * 2 || cycle == 76 + 77 * 3) {
            println("cyclePoints=$points") // result from here
//            return points
        }

        if (hashMap.contains(forCache)) {
//            println("Previous cycle = ${hashMap[forCache]} now cycle=> $cycle")
//            println(points)
        }
        hashMap[forCache] = cycle
    }
    return 0
}

fun rotateClockwise(inputs: MutableList<MutableList<String>>) {
    rotateAnticlockwise(inputs)
    rotateAnticlockwise(inputs)
    rotateAnticlockwise(inputs)
}

private fun rotateAnticlockwise(inputs: MutableList<MutableList<String>>) {
    val N = inputs.size
    for (x in 0..<(N / 2)) {
        for (y in x..<(N - x - 1)) {
            val temp2 = inputs[x][y]
            inputs[x][y] = inputs[y][N - 1 - x]
            inputs[y][N - 1 - x] = inputs[N - 1 - x][N - 1 - y]
            inputs[N - 1 - x][N - 1 - y] = inputs[N - 1 - y][x]
            inputs[N - 1 - y][x] = temp2
        }
    }
}

fun solveV1(lines: MutableList<MutableList<String>>): Int {
    shiftRocksUp(lines)
    return calculatePoints(lines)
}

private fun calculatePoints(lines: MutableList<MutableList<String>>): Int {
    var sum = 0
    for ((index, row) in lines.withIndex()) {
        for (symbol in row) {
            if (symbol == MOVABLE) {
                sum += (lines.size - index)
            }
        }
    }
    return sum
}

private fun shiftRocksUp(lines: MutableList<MutableList<String>>) {
    for (columnIndex in lines[0].indices) {
        shiftRocksInColumn(columnIndex, lines)
    }
}

private val EMPTY = "."
private val MOVABLE = "O"
private val SOLID = "#"

private val EMPTY_CHAR = '.'
private val MOVABLE_CHAR = 'O'
private val SOLID_CHAR = '#'
fun shiftRocksInColumn(columnIndex: Int, lines: MutableList<MutableList<String>>) {
    var string = ""
    val indexesOfSolid = mutableListOf<Int>()
    indexesOfSolid.add(-1)
    for ((index, row) in lines.withIndex()) {
        val symbol = row[columnIndex]
        string += symbol
        if (symbol == SOLID) {
            indexesOfSolid.add(index)
        }
    }
    val counts = string.split(SOLID).map { it -> it.count { it == MOVABLE_CHAR } }
    for ((index, row) in lines.withIndex()) {
        val symbol = row[columnIndex]
        if (symbol == MOVABLE) {
            lines[index][columnIndex] = EMPTY
        }
    }
    shiftRocksInColumnLiterally(lines, columnIndex, indexesOfSolid, counts)
}

fun shiftRocksInColumnLiterally(
    lines: MutableList<MutableList<String>>,
    columnIndex: Int,
    solidIndexes: MutableList<Int>,
    counts: List<Int>
) {
    for ((index, solidIndex) in solidIndexes.withIndex()) {
        var count = counts[index]
        while (count > 0) {
            lines[solidIndex + count][columnIndex] = MOVABLE
            count--
        }
    }
}

private fun <String> MutableList<MutableList<String>>.prettyPrint() {
    for (line in this) {
        println(line.joinToString(separator = ""))
    }
}
