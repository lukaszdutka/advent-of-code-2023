package day_024

import java.io.File
import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.math.BigDecimal.ZERO
import java.math.MathContext


private const val inputPath =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_024/input.txt"
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_024/input_small.txt"

fun main() {
    val lines = File(inputPath).readLines()

    calculateSolutionV1(lines.map { it.toPoint() })
}

private fun String.toPoint(): Object2D {
    val (coords, velocities) = this.split("@")
    val (x, y) = coords.split(",").map { it.trim().toBigDecimal() }
    val (xv, yv) = velocities.split(",").map { it.trim().toBigDecimal() }
    return Object2D(x, y, xv, yv)
}

data class Object2D(val x: BigDecimal, val y: BigDecimal, val xv: BigDecimal, val yv: BigDecimal)

fun calculateSolutionV1(list: List<Object2D>) {
    val lowerBound = BigDecimal.valueOf(200000000000000L)
    val upperBound = BigDecimal.valueOf(400000000000000L)
//    val lowerBound = BigDecimal.valueOf(7L)
//    val upperBound = BigDecimal.valueOf(27L)

    var count = 0
    for (i in list.indices) {
        for (j in i + 1..<list.size) {
            val first = list[i]
            val second = list[j]

            val t1 = t1(first, second)
            val t2 = t1(second, first)
            if (t1 < ZERO || t2 < ZERO) {
                continue
            }
            val x = first.x + first.xv * t1
            val y = first.y + first.yv * t1
            if (x in lowerBound..upperBound && y in lowerBound..upperBound) {
                count++
            }
        }
    }
    println(count)

    // xv * s + x = xv2 * s + x2
    // s(xv - xv2) = x2-x
    // s = (x2-x)/(xv-xv2)
    // res = xv*s+x
}

fun t1(first: Object2D, second: Object2D): BigDecimal {
    //different time, but same x and y
    // x + xv * t1 = x2 + xv2 * t2
    // y + yv * t1 = y2 + yv2 * t2
    val (x1, y1, vx1, vy1) = first
    val (x2, y2, vx2, vy2) = second
    val licznik = vy2 * (x1 - x2) - vx2 * (y1 - y2)
    val mianownik = vx2 * vy1 - vx1 * vy2
    if (mianownik == ZERO) {
        return -ONE
    }
    return licznik.divide(mianownik, MathContext.DECIMAL128)
}
