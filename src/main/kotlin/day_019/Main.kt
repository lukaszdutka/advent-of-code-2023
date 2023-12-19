package day_019

import java.io.File
import java.math.BigInteger
import kotlin.RuntimeException

private const val inputPath =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_019/input.txt"
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_019/input_small.txt"

fun main() {
    val lines = File(inputPath).readLines()

    println("v2=${solutionV1(lines)}")
}

data class Part(val categories: Map<String, IntRange>) { // category to value
//data class Part(val x: IntRange, val m: IntRange, val a: IntRange, val s: IntRange) {

//    fun get(category: String): IntRange = when (category) {
//        "x" -> x
//        "m" -> m
//        "a" -> a
//        "s" -> s
//        else -> throw RuntimeException("bad categroy");
//    }

    fun satisfies(condition: Condition): Pair<Part?, Part?> {
        if (condition is SendingCondition) {
            return this to null
        }
        if (condition !is DecidingCondition) throw RuntimeException("bad")

        val category = condition.category
        val categoryRange = this.categories[category]!!
        if (condition.greaterThan) { // >
            return if (categoryRange.contains(condition.value)) {
                val satisfiedRange = condition.value + 1..categoryRange.last
                val notSatisfiedRange = categoryRange.first..condition.value

                returnNewMaps(category, satisfiedRange, notSatisfiedRange)
            } else if (categoryRange.first > condition.value) {
                this to null
            } else {
                null to this
            }
        } else {
            return if (categoryRange.contains(condition.value)) { // <
                val satisfiedRange = categoryRange.first..<condition.value
                val notSatisfiedRange = condition.value..categoryRange.last

                returnNewMaps(category, satisfiedRange, notSatisfiedRange)
            } else if (categoryRange.last < condition.value) {
                this to null
            } else {
                null to this
            }

        }
    }

    private fun returnNewMaps(
        category: String,
        satisfiedRange: IntRange,
        notSatisfiedRange: IntRange
    ): Pair<Part, Part> {
        val satisfiedMap = this.categories.toMutableMap().apply { put(category, satisfiedRange) }
        val notSatisfiedMap =
            this.categories.toMutableMap().apply { put(category, notSatisfiedRange) }

        return Part(satisfiedMap) to Part(notSatisfiedMap)
    }
}

abstract class Condition(val type: String, val sendTo: String)
class DecidingCondition(
    type: String,
    val category: String,
    val greaterThan: Boolean,
    val value: Int,
    sendTo: String
) : Condition(type, sendTo)

class SendingCondition(type: String, sendTo: String) : Condition(type, sendTo)
data class Rule(val id: String, val conditions: List<Condition>)

fun solutionV1(lines: List<String>): BigInteger {
    val rules = parseInput(lines)
    val acceptedParts = mutableSetOf<Part>()

    fun calculatePart(
        part: Part,
        ruleId: String = "in",
        conditionIndex: Int = 0
    ) {
        val condition = rules[ruleId]!!.conditions[conditionIndex]
        val (satisfying, notSatisfying) = part.satisfies(condition)
        if (satisfying != null) {
            val sendTo = condition.sendTo
            when (sendTo) {
                "R" -> {}
                "A" -> acceptedParts.add(satisfying)
                else -> calculatePart(satisfying, sendTo, 0)
            }
        }
        if (notSatisfying != null) {
            calculatePart(notSatisfying, ruleId, conditionIndex + 1)
        }
    }

    val fullRange = 1..4000
    val wholePart = Part(
        mapOf(
            "x" to fullRange,
            "m" to fullRange,
            "a" to fullRange,
            "s" to fullRange
        )
    )
    calculatePart(wholePart, "in", 0)

    return calculateSolution(acceptedParts)
}

fun calculateSolution(acceptedParts: MutableSet<Part>): BigInteger {
    var sum = BigInteger.ZERO
    for (part in acceptedParts) {
        val number = part.categories.values
            .map { BigInteger.valueOf(it.last.toLong() - it.first.toLong()) + 1.toBigInteger() }
            .reduce { a, b -> a * b }
        sum += number
    }
    return sum
}


private fun parseInput(lines: List<String>): MutableMap<String, Rule> {
    val rules = mutableMapOf<String, Rule>()
    for (line in lines) {
        if (line.isBlank()) {
            break
        }
        val id = line.split("{")[0]
        val conditionsStrings = line.split("{")[1].replace("}", "").split(",")
        val conditions = conditionsStrings.map { it.toCondition() }

        rules[id] = Rule(id, conditions)
    }
    return rules
}

private fun String.toCondition(): Condition {
//            mn{s<3661:A,s>3705:A,m<1698:R,A}
    val split = this.split(":")
    if (split.size == 1) {
        return SendingCondition("SENDING", split[0])
    }
    val sendTo = split[1]
    if (split[0].contains(">")) {
        return DecidingCondition(
            "GREATER_THAN",
            split[0].split(">")[0],
            true,
            split[0].split(">")[1].toInt(),
            sendTo
        )
    }
    if (split[0].contains("<")) {
        return DecidingCondition(
            "LESS_THAN",
            split[0].split("<")[0],
            false,
            split[0].split("<")[1].toInt(),
            sendTo
        )
    }
    throw RuntimeException("bad")
}











