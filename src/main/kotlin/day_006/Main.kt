package day_006

import java.io.File
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

private const val inputPath =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_006/input.txt"
private const val inputPath_v2 =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_006/input_v2.txt"
//private const val inputPath =
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_006/input_small.txt"

fun main() {
    val file = File(inputPath)
    val file_v2 = File(inputPath_v2)
    solutionV1(file.readLines())
    solutionV1(file_v2.readLines())
}

fun solutionV1(lines: List<String>) {
    val filtered = lines[0].split(" ").filter { it.isNotBlank() }
    val filtered2 = lines[1].split(" ").filter { it.isNotBlank() }
    val times = filtered.subList(1, filtered.size).map { it.toBigDecimal() }
    val distances = filtered2.subList(1, filtered2.size).map { it.toBigDecimal() }

    var result = BigDecimal.ONE
    for ((index, maxTime) in times.withIndex()) {
        val distance = distances[index]

        // s = V * (maxTime-V)
        // V nalezy do <0; maxTime>
        // s = -V^2 * V*maxTime - distance

        val a = -BigDecimal.ONE
        val b = maxTime
        val c = -distance

        val delta = (b.multiply(b)).minus(BigDecimal(4).multiply(a).multiply(c))
        val deltaSqrt = delta.sqrt(MathContext(16))

        val x1 = (-b + deltaSqrt).divide(BigDecimal(2).multiply(a)) + (BigDecimal(0.0001))
        val x2 = (-b - deltaSqrt).divide(BigDecimal(2).multiply(a)) - (BigDecimal(0.0001))

        val x2RoundedUp = x2.setScale(0, RoundingMode.DOWN)
        val x1RoundedDown = x1.setScale(0, RoundingMode.UP)

        val res = x2RoundedUp - x1RoundedDown + BigDecimal.ONE
        result = result.multiply(res)
    }
    println(result)
}
