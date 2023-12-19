package day_019

import java.io.File
import kotlin.RuntimeException

private const val inputPath =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_019/input.txt"
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_019/input_small.txt"

fun main() {
    val lines = File(inputPath).readLines()

    println("v1=${solutionV1(lines)}")
//    println("v2=${solutionV2(inputs)}")
}

data class Part(val categories: Map<String, Int>) // category to value
{
    fun satisfies(condition: Condition): Boolean {
        if (condition is SendingCondition) {
            return true
        }
        if (condition is DecidingCondition) {
            if (condition.greaterThan) {
                return this.categories[condition.category]!! > condition.value
            } else {
                return this.categories[condition.category]!! < condition.value
            }
        }
        throw RuntimeException("bad");
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

fun solutionV1(lines: List<String>): Int {
    val (parts, rules) = parseInput(lines)
    val acceptedParts = mutableSetOf<Part>()

    for (part in parts) {
        val processedPart = part(rules, part)
        if (processedPart != null) {
            acceptedParts.add(part)
        }
    }

    return acceptedParts.sumOf { it.categories.values.sum() }
}

private fun part(
    rules: MutableMap<String, Rule>,
    part: Part
): Part? {
    var ruleId = "in"
    while (true) {
        val currentConditions = rules[ruleId]!!.conditions
        for (condition in currentConditions) {
            if (part.satisfies(condition)) {
                val sendTo = condition.sendTo
                if (sendTo == "R") {
                    return null
                }
                if (sendTo == "A") {
                    return part
                }
                ruleId = sendTo
                break
            }
        }
    }
}

private fun parseInput(lines: List<String>): Pair<MutableList<Part>, MutableMap<String, Rule>> {
    val parts = mutableListOf<Part>()
    val rules = mutableMapOf<String, Rule>()
    var parseRules = true;
    for (line in lines) {
        if (line.isBlank()) {
            parseRules = false
            continue
        }
        if (parseRules) {
            val id = line.split("{")[0]
            val conditionsStrings = line.split("{")[1].replace("}", "").split(",")
            val conditions = conditionsStrings.map { it.toCondition() }

            rules[id] = Rule(id, conditions)
        } else {
            val withoutPars = line.replace("{", "").replace("}", "")
            val parsedPart = Part(withoutPars.split(",")
                .associate { it.split("=")[0] to it.split("=")[1].toInt() }
            )
            parts.add(parsedPart)
        }

    }
    return parts to rules
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











