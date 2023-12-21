package day_021

import java.io.File
import java.math.BigInteger
import java.math.BigInteger.*

private const val inputPath =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_021/input.txt"
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_021/input_small.txt"

//bad 696
//592765599408546 too low
//74443071567297 too low

//593125085771753 too low -- istotny

//593166010454856 - bad
//593168942554806 - bad
//593168942554280 - bad
//593168961570610 - bad
fun main() {
//    printResultV2()
    val lines = File(inputPath).readLines()
    val inputs = mutableListOf<MutableList<String>>()
    val inputs2 = mutableListOf<MutableList<String>>()

    for (line in lines) {
        val regularLine = line.split("").filter { it.isNotBlank() }.toMutableList()
        inputs.add(regularLine)
        val tripledLine = repeatThree(regularLine)
        inputs2.add(tripledLine)
    }
    for (line in lines) {
        val regularLine = line.split("").filter { it.isNotBlank() }.toMutableList()
        val tripledLine = repeatThree(regularLine)
        inputs2.add(tripledLine)
    }
    for (line in lines) {
        val regularLine = line.split("").filter { it.isNotBlank() }.toMutableList()
        val tripledLine = repeatThree(regularLine)
        inputs2.add(tripledLine)
    }

    println("v1=${solutionV1(inputs2)}")
    // zwiększamy do 3x3, patrzymy dzielimy na 3x3 kafelki i liczymy ile tam jest rzeczy.

//    println("v2=${solutionV2(inputs)}")
}

fun repeatThree(mutableList: MutableList<String>): MutableList<String> {
    val result = mutableList.toMutableList()
    result.addAll(mutableList)
    result.addAll(mutableList)
    return result
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
    val startingPoint = (131 + 65) to (131 + 65)

    // 202300 map w prawo, lewo, górę i dół
    // 65 stepów i dochodzimy do każdej ściany mapy.
    // 202300 * 131 - jesteśmy w stanie dojść do końca każdej następnej z 202300 map
    // one są na zmiane parzyste/nie parzyste

    val maxNumberOfSteps = (65 + 131).toBigInteger()
    val checked = mutableMapOf<Pair<Int, Int>, BigInteger>()

    fun calculateSteps(point: Pair<Int, Int>, steps: BigInteger) {
        val (y, x) = point
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

    divideInto3by3(inputs, checked)

    val evenSteps =
        checked.filter { it.value <= maxNumberOfSteps }.filter { it.value.remainder(2.toBigInteger()) == ONE }
    val unique = evenSteps.map { it.key }.toSet()
    val result = unique.size
    return result
}

fun divideInto3by3(
    inputs: MutableList<MutableList<String>>,
    testChecked: MutableMap<Pair<Int, Int>, BigInteger>
) {
    println("start")
    val checked = testChecked.filter { it.value <= (65 + 131).toBigInteger() }.toMutableMap()
    val up = inputs.subList(0, 131)
    val mid = inputs.subList(131, 262)
    val bot = inputs.subList(262, 393)

    val leftUp = mutableListOf<MutableList<String>>()
    val midUp = mutableListOf<MutableList<String>>()
    val rightUp = mutableListOf<MutableList<String>>()
    for (row in up) {
        leftUp.add(row.subList(0, 131))
        midUp.add(row.subList(131, 262))
        rightUp.add(row.subList(262, 393))
    }
    val leftMid = mutableListOf<MutableList<String>>()
    val midMid = mutableListOf<MutableList<String>>()
    val rightMid = mutableListOf<MutableList<String>>()
    for (row in mid) {
        leftMid.add(row.subList(0, 131))
        midMid.add(row.subList(131, 262))
        rightMid.add(row.subList(262, 393))
    }
    val leftDown = mutableListOf<MutableList<String>>()
    val midDown = mutableListOf<MutableList<String>>()
    val rightDown = mutableListOf<MutableList<String>>()
    for (row in bot) {
        leftDown.add(row.subList(0, 131))
        midDown.add(row.subList(131, 262))
        rightDown.add(row.subList(262, 393))
    }
    println("leftUp:")
    leftUp.prettyPrint(checked, "left", "up")
    println("midUp:")
    midUp.prettyPrint(checked, "mid", "up")
    println("rightUp:")
    rightUp.prettyPrint(checked, "right", "up")
    println("leftMid:")
    leftMid.prettyPrint(checked, "left", "mid")
    println("midMid:")
    midMid.prettyPrint(checked, "mid", "mid")
    println("rightMid:")
    rightMid.prettyPrint(checked, "right", "mid")
    println("leftDown:")
    leftDown.prettyPrint(checked, "left", "down")
    println("midDown:")
    midDown.prettyPrint(checked, "mid", "down")
    println("rightDown:")
    rightDown.prettyPrint(checked, "right", "down")

    val corners = numberOfGardens(leftMid, checked, column = "left", row = "mid") +
            numberOfGardens(midUp, checked, column = "mid", row = "up") +
            numberOfGardens(rightMid, checked, column = "right", row = "mid") +
            numberOfGardens(midDown, checked, column = "mid", row = "down")
    val edges = 202300.toBigInteger() * // there are 202300 edges of each type
            (numberOfGardens(leftUp, checked, even = false, "left", "up") +
                    numberOfGardens(rightUp, checked, even = false, "right", "up") +
                    numberOfGardens(leftDown, checked, even = false, "left", "down") +
                    numberOfGardens(rightDown, checked, even = false, "right", "down"))

    val fullEven = numberOfGardens(midMid, checked, even = true, "mid", "mid")
    val fullOdd = numberOfGardens(midMid, checked, even = false, "mid", "mid")

    val evenSquares = calculateEvenSquares()
    val oddSquares = calculateOddSquares()
    val result = corners + edges + fullEven * evenSquares + fullOdd * oddSquares
    println("END RESULT = $result")
}

fun calculateOddSquares(): BigInteger {
    val a1 = 3.toBigInteger()
    val r = 4.toBigInteger()
    val an = (202299 * 2 + 1).toBigInteger() //ile jest nieparzystych w największym słupku?

    val n = (an - a1) / r + ONE
    val sum = (a1 + an) * n / TWO

    val oddInBiggestRow = ((202299 * 2 + 1).toBigInteger() / TWO) + ONE
    val result = sum * TWO - oddInBiggestRow

    return result
}

fun calculateEvenSquares(): BigInteger {
    val a1 = ONE
    val r = 4.toBigInteger()
    val an = (202298 * 2 + 1).toBigInteger() //ile jest parzystych w największym słupku?

    val n = (an - a1) / r + ONE
    val sum = (a1 + an) * n / TWO

    val evenInBiggestRow = (202299 * 2 + 1).toBigInteger() / TWO
    val result = sum * TWO - evenInBiggestRow

    return result
}

fun preCheck(actual: Int, expected: Int) {
    if (actual != expected) {
        throw RuntimeException("actual=$actual, expected=$expected");
    }
}

private fun numberOfGardens(
    map: MutableList<MutableList<String>>,
    myMap: MutableMap<Pair<Int, Int>, BigInteger>,
    even: Boolean = true,
    column: String,
    row: String
): BigInteger {
    val shiftX = when (column) {
        "left" -> 0
        "mid" -> 131
        "right" -> 262
        else -> throw RuntimeException("bad shift")
    }
    val shiftY = when (row) {
        "up" -> 0
        "mid" -> 131
        "down" -> 262
        else -> throw RuntimeException("bad shift")
    }

    var sum = ZERO
    for ((y, row) in map.withIndex()) {
        for ((x, char) in row.withIndex()) {
            if (even) {
                val cell = myMap[y + shiftY to x + shiftX]
                if (cell != null && cell.toInt() % 2 == 0 && (char == "." || char == "S")) {
                    sum += ONE
                }
            } else {
                val cell = myMap[y + shiftY to x + shiftX]
                if (cell != null && cell.toInt() % 2 != 0 && (char == "." || char == "S")) {
                    sum += ONE
                }
            }
        }
    }

    return sum
}

private fun <String> List<List<String>>.prettyPrint(
    checked: MutableMap<Pair<Int, Int>, BigInteger>,
    column: kotlin.String,
    row: kotlin.String
) {
    //column: left, mid, right
    //row: up, mid, down
    val shiftX = when (column) {
        "left" -> 0
        "mid" -> 131
        "right" -> 262
        else -> throw RuntimeException("bad shift")
    }
    val shiftY = when (row) {
        "up" -> 0
        "mid" -> 131
        "down" -> 262
        else -> throw RuntimeException("bad shift")
    }

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