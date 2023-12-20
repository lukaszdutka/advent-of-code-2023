package day_020

import java.io.File
import java.lang.RuntimeException
import java.util.LinkedList
import java.util.Queue


private const val inputPath =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_020/input.txt"
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_020/input_small.txt"

fun main() {
    val lines = File(inputPath).readLines()

    println("v1=${solutionV1(lines)}")
}

const val FLIP_FLOP = "%"
const val CONJUNCTION = "&"
const val BROADCASTER = "broadcaster"
fun solutionV1(lines: List<String>): Int {
    // %, &, broadcaster

    val allModules = mutableMapOf<String, Module>()
    val conjunctionModules = mutableListOf<Conjunction>()
    for (module in lines.map { it.parse() }) {
        allModules[module.key] = module
        if (module is Conjunction) {
            conjunctionModules.add(module)
        }
    }
    for (conjunctionModule in conjunctionModules) {
        for (module in allModules) {
            if (module.value.destinationKeys.contains(conjunctionModule.key)) {
                conjunctionModule.recentPulses[module.key] = LOW
            }
        }
    }
    var lowImpulse = 0
    var highImpulse = 0

    for (i in 1..1000) { // button push
        lowImpulse++
        val impulses: Queue<Impulse> = LinkedList()
        impulses.add(Impulse("button", BROADCASTER, LOW))

        while (impulses.isNotEmpty()) {
            val (source, destination, impulse) = impulses.poll()!!

//            if (destination == "rx" && impulse == LOW) {
//                return i
//            }

            val module = allModules[destination] ?: continue
            val whatPulseToSend = module.whatPulseToSend(impulse, source)
            if (whatPulseToSend != null) {
                for (destinationModule in module.destinationKeys) {
                    if (whatPulseToSend == LOW) {
                        lowImpulse++
                    } else {
                        highImpulse++
                    }
                    impulses.add(Impulse(module.key, destinationModule, whatPulseToSend))
                }
            }
        }
    }

    println(lowImpulse)
    println(highImpulse)
    return lowImpulse * highImpulse
}

data class Impulse(val source: String, val destination: String, val impulse: String)

private abstract class Module(val key: String, val destinationKeys: List<String>) {
    abstract fun whatPulseToSend(impulse: String, source: String): String?
}

private const val HIGH = "high"
private const val LOW = "low"

private class FlipFlop(key: String, destinationKeys: List<String>, var on: Boolean = false) :
    Module(
        key,
        destinationKeys
    ) {
    override fun whatPulseToSend(impulse: String, source: String): String? {
        if (impulse == HIGH) {
            return null
        }
        on = !on
        return if (on) HIGH else LOW
    }
}

private class Conjunction(
    key: String,
    destinationKeys: List<String>,
    var recentPulses: MutableMap<String, String> = mutableMapOf()
) :
    Module(key, destinationKeys) {
    override fun whatPulseToSend(impulse: String, source: String): String {
        recentPulses[source] = impulse
        if (recentPulses.values.all { it == HIGH }) {
            return LOW
        }
        return HIGH
    }
}

private class Broadcast(key: String, destinationKeys: List<String>) : Module(key, destinationKeys) {
    override fun whatPulseToSend(impulse: String, source: String): String {
        return impulse
    }
}


private fun String.parse(): Module {
    if (this.startsWith(BROADCASTER)) {
        val (id, destinations) = this.split("->")
        return Broadcast(id.trim(), destinations.trim().split(", "))
    }
    if (this.startsWith(FLIP_FLOP)) {
        val (id, destinations) = this.removePrefix(FLIP_FLOP).split("->")
        return FlipFlop(id.trim(), destinations.trim().split(", "), false)
    }
    if (this.startsWith(CONJUNCTION)) {
        val (id, destinations) = this.removePrefix(CONJUNCTION).split("->")
        return Conjunction(id.trim(), destinations.trim().split(", "), mutableMapOf())
    }
    throw RuntimeException("Not valid type: $this")
}
