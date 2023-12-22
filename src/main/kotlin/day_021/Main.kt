package day_021

import java.io.File
import java.math.BigInteger
import java.math.BigInteger.*

private const val inputPath =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_021/input.txt"
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_021/input_small.txt"

private const val x = 2

private const val boardSize = 131

//private const val boardSize = 11
private val startingPoint = (boardSize / 2 + 2 * boardSize) to (boardSize / 2 + 2 * boardSize)

private const val maxNumberOfSteps = boardSize / 2 + x * boardSize
//private const val maxNumberOfSteps = 65

//bad 696
//592765599408546 too low
//74443071567297 too low

//593125085771753 too low -- istotny

//593166010454856 - bad
//593168942554806 - bad
//593168942554280 - bad
//593168961570610 - bad
//593174091266625 - bad
//593174122420825 - GOOD
//615682809390825 - doesnt work
//570665361206542 - to small
fun main() {
//    printResultV2()
    val lines = File(inputPath).readLines()
    val inputs = mutableListOf<MutableList<String>>()
    val inputs2 = mutableListOf<MutableList<String>>()

    for (line in lines) {
        val regularLine = line.split("").filter { it.isNotBlank() }.toMutableList()
        inputs.add(regularLine)
    }
    for (i in 1..5) {
        for (line in lines) {
            val regularLine = line.split("").filter { it.isNotBlank() }.toMutableList()
            val fiveTimesLine = repeatFive(regularLine)
            inputs2.add(fiveTimesLine)
        }
    }

    println("v1=${solutionV1(inputs2)} for maxSteps=$maxNumberOfSteps")
    // zwiększamy do 5x5, patrzymy dzielimy na 3x3 kafelki i liczymy ile tam jest rzeczy.

//    println("v2=${solutionV2(inputs)}")
}

fun repeatFive(mutableList: MutableList<String>): MutableList<String> {
    val result = mutableList.toMutableList()
    result.addAll(mutableList)
    result.addAll(mutableList)
    result.addAll(mutableList)
    result.addAll(mutableList)
    return result
}

fun solutionV1(inputs: MutableList<MutableList<String>>): Int {
    // 202300 map w prawo, lewo, górę i dół
    // 65 stepów i dochodzimy do każdej ściany mapy.
    // 202300 * 131 - jesteśmy w stanie dojść do końca każdej następnej z 202300 map
    // one są na zmiane parzyste/nie parzyste

    //x = 0 (65+0*131), 3725 // 3725 // 3542
    //x = 1 (65+1*131), 32346 // 32346 // 32896
    //x = 2 (65+2*131), 91055 // 91055 // 90138
    //y = 15044 x^2 + 13577 x + 3725
    // f(202300)=

    //65 , f(0) => 3725
    //196, f(1) => 32896
    //327, f(2) => 91055
    //458, f(3) => 178202
    //589, f(4) => 294337
    //26501365 = 65 + 202300 * 131
    // f(202300) = ?

    quadraticFormula(3725, 32896, 91055)


//    val x = 202300.toBigInteger()
//    val a = 15044.toBigInteger()
//    val b = 13577.toBigInteger()
//    val c = 3725.toBigInteger()
//    println(a * x.pow(2) + b * x + c)
//
//    val x = 202300.toBigInteger()
//    val a = 13944.toBigInteger()
//    val b = 15410.toBigInteger()
//    val c = 3542.toBigInteger()
//    println(a * x.pow(2) + b * x + c)
//y = 13944 x^2 + 15410 x + 3542
//    throw RuntimeException("lets go")
    val checked = mutableMapOf<Pair<Int, Int>, Int>()

    fun calculateSteps(point: Pair<Int, Int>, steps: Int) {
        if (steps > maxNumberOfSteps) {
            return
        }
        var (y, x) = point
        while (y < 0) {
            y += boardSize
        }
        while (y >= boardSize) {
            y -= boardSize
        }
        while (x < 0) {
            x += boardSize
        }
        while (x >= boardSize) {
            x -= boardSize
        }
        val field = inputs.getOrNull(y)?.getOrNull(x)!!
        if (field == "#") {
            return
        }
        if ((checked[point] ?: Int.MAX_VALUE) <= steps) {
            return
        }
        checked[point] = steps

        calculateSteps(point.up(), steps + 1)
        calculateSteps(point.right(), steps + 1)
        calculateSteps(point.down(), steps + 1)
        calculateSteps(point.left(), steps + 1)
    }

    calculateSteps(startingPoint, 0)

    divideInto5by5(inputs, checked)

    val oddSteps = checked.filter { it.value % 2 == maxNumberOfSteps % 2 }
    val unique = oddSteps.map { it.key }.toSet()
    val result = unique.size
    return result
}

fun quadraticFormula(x0: Int, x1: Int, x2: Int) {
    val c = x0.toBigInteger()
    val a = ((x2 - 2 * x1 + x0) / 2).toBigInteger()
    val b = (x1 - x0 - a.toInt()).toBigInteger()

    println("a=$a")
    println("b=$b")
    println("c=$c")

    val x = 202300.toBigInteger()

    val quadraticFormulaBiatch = a * x * x + b * x + c
    println("res=$quadraticFormulaBiatch")
}


fun divideInto5by5(
    inputs: MutableList<MutableList<String>>,
    testChecked: MutableMap<Pair<Int, Int>, Int>
) {
    println("start")
    val checked = testChecked.filter { it.value <= (boardSize / 2 + 2 * boardSize) }.toMutableMap()
    val coordsToShape = calculateBlocks(inputs)

    fun numberOfGardens(
        pair: Pair<Int, Int>,
        even: Boolean = true
    ): BigInteger {
        val shiftX = pair.second * boardSize
        val shiftY = pair.first * boardSize
        val map = coordsToShape[pair]!!

        var sum = ZERO
        for ((y, row) in map.withIndex()) {
            for ((x, char) in row.withIndex()) {
                if (even) {
                    val cell = checked[y + shiftY to x + shiftX]
                    if (cell != null && cell.toInt() % 2 == 0 && (char == "." || char == "S")) {
                        sum += ONE
                    }
                } else {
                    val cell = checked[y + shiftY to x + shiftX]
                    if (cell != null && cell.toInt() % 2 != 0 && (char == "." || char == "S")) {
                        sum += ONE
                    }
                }
            }
        }

        return sum
    }

    val corners = numberOfGardens(0 to 2, even = false) +
            numberOfGardens(2 to 0, even = false) +
            numberOfGardens(4 to 2, even = false) +
            numberOfGardens(2 to 4, even = false)
    val edges = 202300.toBigInteger() * // there are 202300 edges of each type
            (numberOfGardens(1 to 0, even = true) +
                    numberOfGardens(1 to 4, even = true) +
                    numberOfGardens(4 to 1, even = true) +
                    numberOfGardens(4 to 3, even = true))
    val nearlyFull = 202299.toBigInteger() * // there are 202299 that are 7/8 full of each type
            (numberOfGardens(1 to 1, even = false) +
                    numberOfGardens(1 to 3, even = false) +
                    numberOfGardens(3 to 1, even = false) +
                    numberOfGardens(3 to 3, even = false))

    val fullEven = numberOfGardens(2 to 2, even = true)
    val fullOdd = numberOfGardens(2 to 2, even = false)

//    println("Looks like a=${fullEven + fullOdd}")
//    println("Looks like b=${fullEven + fullOdd}")
//    println("Looks like c=${fullEven + fullOdd}")

    val evenSquares = calculateEvenSquares()
    val oddSquares = calculateOddSquares()
    val result = corners + edges + nearlyFull +
            fullEven * evenSquares +
            fullOdd * oddSquares
    println("END RESULT = $result")
}

private fun calculateBlocks(inputs: MutableList<MutableList<String>>): MutableMap<Pair<Int, Int>, MutableList<MutableList<String>>> {
    val rowBasedChunked = inputs.chunked(boardSize)
    val coordsToShape = mutableMapOf<Pair<Int, Int>, MutableList<MutableList<String>>>()

    for ((y, row) in rowBasedChunked.withIndex()) {
        //row - 131 list które oznaczają 131 wierszy
        for (x in 0..4) {
            coordsToShape[y to x] = mutableListOf()
        }
        for ((_, rowOfRow) in row.withIndex()) {
            rowOfRow.chunked(boardSize)
                .map { it.toMutableList() }
                .forEachIndexed { index, strings -> coordsToShape[y to index]!!.add(strings) }
        }
    }
    return coordsToShape
}

private fun MutableMap<Pair<Int, Int>, MutableList<MutableList<String>>>.prettyPrint(
    pair: Pair<Int, Int>,
    checked: MutableMap<Pair<Int, Int>, BigInteger>
) {
    this[pair]!!.prettyPrint(checked, pair)
}

fun calculateEvenSquares(): BigInteger {
    val a1 = 3.toBigInteger()
    val r = 4.toBigInteger()
    val an = (202299 * 2 + 1).toBigInteger() //ile jest parzystych w największym słupku?

    val n = (an - a1) / r + ONE
    val sum = (a1 + an) * n / TWO

    val evenInBiggestRow = ((202299 - 1) / 2 + 1).toBigInteger() * TWO
    val result = sum * TWO - evenInBiggestRow

    return result
}

fun calculateOddSquares(): BigInteger {
    val a1 = ONE
    val r = 4.toBigInteger()
    val an = (202299 * 2 - 1).toBigInteger() //ile jest nieparzystych w największym słupku?

    val n = (an - a1) / r + ONE
    val sum = (a1 + an) * n / TWO

    val oddInBiggestRow = ((202298 - 2) / 2 + 1).toBigInteger() * TWO + ONE
    val result = sum * TWO - oddInBiggestRow

    return result
}

fun preCheck(actual: Int, expected: Int) {
    if (actual != expected) {
        throw RuntimeException("actual=$actual, expected=$expected");
    }
}


private fun <String> List<List<String>>.prettyPrint(
    checked: MutableMap<Pair<Int, Int>, BigInteger>,
    shift: Pair<Int, Int>
) {
    val (yMultiplier, xMultiplier) = shift
    println("Printing for multiplier: y=$yMultiplier, x=$xMultiplier")
    val shiftX = boardSize * xMultiplier //0-4
    val shiftY = boardSize * yMultiplier

    for ((y, row) in this.withIndex()) {
        for ((x, char) in row.withIndex()) {
            if ((checked[y + shiftY to x + shiftX] ?: ONE).remainder(TWO) == ZERO) {
                print("$")
            } else {
                print(char)
            }
        }
        println()
    }
}


private fun startingPoint(inputs: MutableList<MutableList<String>>): Pair<Int, Int> {
    for ((y, row) in inputs.withIndex()) {
        for ((x, char) in row.withIndex()) {
            if (char == "S") {
                return y to x
            }
        }
    }
    throw RuntimeException("no start point")
}


private fun Pair<Int, Int>.up() = this.first - 1 to this.second
private fun Pair<Int, Int>.down() = this.first + 1 to this.second
private fun Pair<Int, Int>.right() = this.first to this.second + 1
private fun Pair<Int, Int>.left() = this.first to this.second - 1