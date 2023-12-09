package day_008

import java.io.File
import java.math.BigInteger

private const val inputPath =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_008/input.txt"
//private const val inputPath =
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_008/input_small.txt"

fun main() {
    val file = File(inputPath)

    solutionV1(file.readLines())
//    solutionV2(file.readLines())
}

fun solutionV1(lines: List<String>) {
    val instructions = lines.first().trim()

    val nodeStrings = lines.subList(2, lines.size)
    val nextStepMap = mutableMapOf<String, Pair<String, String>>()
    for (line in nodeStrings) {
        val key = line.split("=")[0].trim()
        val values = line.split("=")[1].trim()
            .replace("(", "")
            .replace(")", "")
            .split(",")
            .map { it.trim() }
        nextStepMap[key] = Pair(values[0], values[1])
    }

    val startingPoints = nextStepMap.keys.filter { it.endsWith("A") }
    val endResults = mutableListOf<BigInteger>()
//    println(startingPoints)
    println(startingPoints)
    for (startingPoint in startingPoints) {
//    for (startingPoint in startingPoints.subList(3, 4)) {
        println("Starting: $startingPoint")
        var steps = BigInteger.ZERO
        var current = startingPoint
        while (!current.endsWith("Z")) {
//        while (current != "ZZZ") {
            val i = instructions[steps.remainder(instructions.length.toBigInteger()).toInt()]
            steps++
            current = if (i == 'L') {
                nextStepMap[current]!!.first
            } else {
                nextStepMap[current]!!.second
            }
        }
        println("current=$current, steps=$steps")
        endResults.add(steps)
    }
    println(endResults)
//    vals = vals.map { it.minus(BigInteger.ONE) }.toMutableList()
//    vals = vals.map { it.plus(BigInteger.ONE) }.toMutableList()
    val a = lcm(endResults[0], endResults[1])
    println(a)
    val b = lcm(endResults[2], a)
    println(b)
    val c = lcm(endResults[3], b)
    println(c)
    val d = lcm(endResults[4], c)
    println(d)
    val e = lcm(endResults[5], d)
    println(e)
    println(lcm(e, instructions.length.toBigInteger()))
}

//12315788159977
//23995234098817186255050
fun lcm(x: BigInteger, y: BigInteger): BigInteger {
    return (x * y) / x.gcd(y)
}