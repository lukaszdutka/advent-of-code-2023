package day_024

import day_008.lcm
import java.io.File
import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.math.BigDecimal.ZERO
import java.math.BigInteger
import java.math.MathContext
import kotlin.math.max


private const val inputPath =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_024/input.txt"
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_024/input_small.txt"

fun main() {
    val lines = File(inputPath).readLines()

    val points = lines.map { it.toPoint() }

//    calculateSolutionV1(points)
    calculateSolutionPart2(points)
}

fun calculateSolutionPart2(points: List<Object2D>) {
    val (x, y, xv, yv) = points[0]
    val (x2, y2, xv2, yv2) = points[1]

    val speedRange = -1000..1000

    val sortedByX = points.sortedBy { it.x }
    //it's one step before

    println("LOOP")
    for (speedInt in speedRange) {
        val speed = speedInt.toBigDecimal()
//        val x0 = x + (xv - speed)*t1
        val divisor = xv2 - speed
        if (divisor != ZERO) {
            val t1 = (x2 - x + (xv - speed)).divide(divisor, MathContext.DECIMAL128)
//            if (max(0, t1.stripTrailingZeros().scale()) <= 4) {
//            println("$speed => t1=$t1.")
            val distanceNeeded = (x + xv * t1 - x2 + xv2 * t1)
            if (distanceNeeded % t1 == ZERO) {
                println("distance: $distanceNeeded can be done with speed: ${distanceNeeded / t1}")
            }
//            }
        } else {
            println("Zero for speed $speed")
        }
    }

    //277903024391745, 368934106615824, 298537551311799 @ -118, -107, 62
    //183412557215942, 418647008376557, 219970939624628 @ 72, -215, 133

    //for speed = 214
    //it needs to go through
    //x + xv*t = x0 + 214*t
    //183412557215942 + 72t = x0 + 214t
    //x0 = 183412557215942 + (72-214)t1
    //x0 = 277903024391745 + (-118-214)t2
    //t1 = ( 277903024391745 - 183412557215942 + (-118-214)t2 ) / (72-214)
    //

    val number = x.toBigInteger()

//    println("x - x1 = ${x - x1}")
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
