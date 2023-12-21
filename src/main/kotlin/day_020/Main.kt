package day_020

import day_008.lcm
import java.io.File
import java.lang.RuntimeException
import java.math.BigInteger
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
fun solutionV1(lines: List<String>): BigInteger {
    // %, &, broadcaster
    val allModules = parseInput(lines)

    printForGraph(allModules)

    val gf: Conjunction = allModules["gf"] as Conjunction

    var buttonPressed = 0.toBigInteger()
    while (true) { // button push
        buttonPressed++
        val impulses: Queue<Impulse> = LinkedList()
        impulses.add(Impulse("button", BROADCASTER, LOW))



        while (impulses.isNotEmpty()) {
            val (source, destination, impulse) = impulses.poll()!!
            if (source == "gf") {
                val sources = gf.recentPulses.filter { it.value == HIGH }
                if (sources.isNotEmpty()) {
                    println("$buttonPressed => ${sources.keys}")
                }
                //kr, zs, kf, qk
                val kr = (11283 - 7522).toBigInteger()
                val zs = (12273 - 8182).toBigInteger()
                val kf = (22602 - 18835).toBigInteger()
                val qk = (28007 - 24006).toBigInteger()

                println("res = ${lcm(lcm(lcm(kr, zs), kf), qk)}")
            }
            if (destination == "rx" && impulse == LOW) {
                return buttonPressed
            }

            val module = allModules[destination] ?: continue
            val whatPulseToSend = module.whatPulseToSend(impulse, source)
            if (whatPulseToSend != null) {
                for (destinationModule in module.destinationKeys) {
                    impulses.add(Impulse(module.key, destinationModule, whatPulseToSend))
                }
            }
        }
    }

    throw RuntimeException("bad");
}

private fun printForGraph(allModules: MutableMap<String, Module>) {
    for (module in allModules) {
        for (destination in module.value.destinationKeys) {
            println("\"${module.value.keyWithType()}\" -- \"${allModules[destination]?.keyWithType() ?: "rx"}\"")
        }
    }
}

private fun parseInput(lines: List<String>): MutableMap<String, Module> {
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
    return allModules
}

data class Impulse(val source: String, val destination: String, val impulse: String)

private abstract class Module(val key: String, val destinationKeys: List<String>) {
    abstract fun whatPulseToSend(impulse: String, source: String): String?
    abstract fun keyWithType(): String
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

    override fun keyWithType(): String = FLIP_FLOP + key

    override fun toString(): String {
        return "FlipFlop(on=$on)"
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

    override fun keyWithType(): String = CONJUNCTION + key

    override fun toString(): String {
        return "Conjunction(recentPulses=$recentPulses)"
    }

}

private class Broadcast(key: String, destinationKeys: List<String>) : Module(key, destinationKeys) {
    override fun whatPulseToSend(impulse: String, source: String): String {
        return impulse
    }

    override fun toString(): String {
        return "Broadcast()"
    }

    override fun keyWithType(): String = BROADCASTER + key
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
