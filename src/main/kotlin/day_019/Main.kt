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
        category: String, satisfiedRange: IntRange, notSatisfiedRange: IntRange
    ): Pair<Part, Part> {
        val satisfiedMap = this.categories.toMutableMap().apply { put(category, satisfiedRange) }
        val notSatisfiedMap = this.categories.toMutableMap().apply { put(category, notSatisfiedRange) }

        return Part(satisfiedMap) to Part(notSatisfiedMap)
    }
}

abstract class Condition(val sendTo: String)
class DecidingCondition(
    val category: String, val greaterThan: Boolean, val value: Int, sendTo: String
) : Condition(sendTo)

class SendingCondition(sendTo: String) : Condition(sendTo)
data class Rule(val id: String, val conditions: List<Condition>)

fun solutionV1(lines: List<String>): BigInteger {
    val rules = parseInput(lines)
    val acceptedParts = mutableSetOf<Part>()

    fun calculatePart(part: Part, ruleId: String = "in", conditionIndex: Int = 0) {
        val condition = rules[ruleId]!!.conditions[conditionIndex]
        val (satisfying, notSatisfying) = part.satisfies(condition)
        if (satisfying != null) {
            when (val sendTo = condition.sendTo) {
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
    val wholePart = Part(mapOf("x" to fullRange, "m" to fullRange, "a" to fullRange, "s" to fullRange))

    calculatePart(wholePart, "in", 0)

    return calculateSolution(acceptedParts)
}

fun calculateSolution(acceptedParts: MutableSet<Part>): BigInteger {
    var sum = BigInteger.ZERO
    for (part in acceptedParts) {
        val number = part.categories.values
            .map { it.last - it.first + 1 }
            .map { it.toBigInteger() }
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
        val (id, conditionsStrings) = line.split("{")
        val conditions = conditionsStrings
            .replace("}", "")
            .split(",")
            .map { it.toCondition() }
        rules[id] = Rule(id, conditions)
    }
    return rules
}

private fun String.toCondition(): Condition {
    val split = this.split(":")
    if (split.size == 1) return SendingCondition(split[0])
    val sendTo = split[1]
    if (split[0].contains(">")) {
        val (key, value) = split[0].split(">")
        return DecidingCondition(key, true, value.toInt(), sendTo)
    }
    if (split[0].contains("<")) {
        val (key, value) = split[0].split("<")
        return DecidingCondition(key, false, value.toInt(), sendTo)
    }
    throw RuntimeException("bad")
}