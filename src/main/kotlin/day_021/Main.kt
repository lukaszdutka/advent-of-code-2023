package day_021

import java.io.File
import java.math.BigInteger
import java.math.BigInteger.*

private const val inputPath =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_021/input.txt"
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_021/input_small.txt"

//bad 696
//592765599408546 too low
fun main() {
//    printResultV2()
    val lines = File(inputPath).readLines()
    val inputs = mutableListOf<MutableList<String>>()
    val inputs2 = mutableListOf<MutableList<String>>()

    for (line in lines) {
        inputs.add(line.split("").filter { it.isNotBlank() }.toMutableList())
        inputs2.add(line.split("").filter { it.isNotBlank() }.toMutableList())
    }

    println("v1=${solutionV1(inputs)}")
//    println("v2=${solutionV2(inputs)}")
}

fun printResultV2() {
    val left = 5479.toBigInteger()
    val right = 5508.toBigInteger()
    val up = 5515.toBigInteger()
    val down = 5472.toBigInteger()
    val edgesUpRight = 202300.toBigInteger() * 971.toBigInteger()
    val edgesUpLeft = 202300.toBigInteger() * 951.toBigInteger()
    val edgesDownRight = 202300.toBigInteger() * 944.toBigInteger()
    val edgesDownLeft = 202300.toBigInteger() * 935.toBigInteger()
    val numberOfSquares = (202299.toBigInteger() * 202300.toBigInteger()) / 2.toBigInteger()

    val oddFullSquare = 7252.toBigInteger()
    val evenFullSquare = 7232.toBigInteger()

    val odd = numberOfSquares * TWO * oddFullSquare
    val even = numberOfSquares * TWO * evenFullSquare

    val nieparzyste = (202299.toBigInteger().div(TWO) + ONE) * 4.toBigInteger() * oddFullSquare
    val parzyste = (202299.toBigInteger().div(TWO)) * 4.toBigInteger() * evenFullSquare

    println(left + right + up + down + edgesUpRight + edgesUpLeft + edgesDownRight + edgesDownLeft + odd + even + nieparzyste + parzyste)

}

fun solutionV1(inputs: MutableList<MutableList<String>>): Int {
//    val startingPoint = 0 to 0  //up left
//    val startingPoint = 130 to 0 //down left
//    val startingPoint = 130 to 130 //down right
//    val startingPoint = 0 to 130 //up right
    val startingPoint = startingPoint(inputs)

//    val maxNumberOfSteps = 26501365.toBigInteger()

    //starting point: middle, steps 130/131
    // 7232
    //starting point: 0,0, steps 65
    // 902
    // 0,0 | steps 130 | 3630


    // 202300 map w prawo, lewo, górę i dół
    // 65 stepów i dochodzimy do każdej ściany mapy.
    // 202300 * 131 - jesteśmy w stanie dojść do końca każdej następnej z 202300 map
    // one są na zmiane parzyste/nie parzyste

    val maxNumberOfSteps = (131).toBigInteger()
    val checked = mutableMapOf<Pair<Int, Int>, BigInteger>()

    fun yCoordinate(first: Int) =
        if (first < 0) inputs.size + (first % inputs.size) else first % inputs.size

    fun xCoordinate(second: Int) =
        if (second < 0) inputs.size + (second % inputs.size) else second % inputs.size

    //11 elems, 0-10

//    preCheck(yCoordinate(-12), 10)
//    preCheck(yCoordinate(-11), 0)
//    preCheck(yCoordinate(-3), 8)
//    preCheck(yCoordinate(-2), 9)
//    preCheck(yCoordinate(-1), 10)
//    preCheck(yCoordinate(0), 0)
//    preCheck(yCoordinate(1), 1)
//    preCheck(yCoordinate(10), 10)
//    preCheck(yCoordinate(11), 0)
//    preCheck(yCoordinate(12), 1)

    fun calculateSteps(point: Pair<Int, Int>, steps: BigInteger) {
        val y = yCoordinate(point.first)
        val x = xCoordinate(point.second)
        val field = inputs.getOrNull(y)?.getOrNull(x)
        if (field == "#" || field == null) {
            return
        }
        if (steps > maxNumberOfSteps) {
            return
        }
        if ((checked[point] ?: Int.MAX_VALUE.toBigInteger()) <= steps) {
            return
        }
        checked[point] = steps

        calculateSteps(point.up(), steps + ONE)
        calculateSteps(point.down(), steps + ONE)
        calculateSteps(point.right(), steps + ONE)
        calculateSteps(point.left(), steps + ONE)
    }

    calculateSteps(startingPoint, ZERO)

    inputs.prettyPrint(checked)
    val evenSteps = checked.filter { it.value.remainder(2.toBigInteger()) == ONE }
    val unique = evenSteps.map { it.key }.toSet()
    val result = unique.size
    return result
}

fun preCheck(actual: Int, expected: Int) {
    if (actual != expected) {
        throw RuntimeException("actual=$actual, expected=$expected");
    }
}

private fun <String> List<List<String>>.prettyPrint(checked: MutableMap<Pair<Int, Int>, BigInteger>) {
    for ((y, row) in this.withIndex()) {
        for ((x, char) in row.withIndex()) {
            if ((checked[y to x] ?: ONE).remainder(TWO) == ZERO) {
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