package day_005

import java.io.File
import java.math.BigInteger

private const val inputPath =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_005/input5.txt"
//private const val inputPath =
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_005/input5_small.txt"

fun main() {
    val file = File(inputPath)
    val lines = file
        .readLines()
    val wholeInput = file.readText()
//    solutionV1(lines, wholeInput)
    solutionV2(lines, wholeInput)
}

fun solutionV2(lines: List<String>, wholeInput: String) {
    val seedRanges = seedRanges(lines)
    val maps = initMap(wholeInput)

    var lowestSeed = seedRanges.last().endExclusive
    println("MAX before calc: $lowestSeed")

    // dla kazdego seed range
    // sprawdzic, czy intersectuje z ktoryms range z nowego typu
    // jesli itersectuje, to przemapowac zbior wspolny na nowy range
    // zebrac wszystkie nowe range
    // powtorzyc operacje
    // potem znalezc minimum
    var nextRanges = mutableListOf<OpenEndRange<BigInteger>>()
    var ranges = seedRanges.toMutableList()
    var type = "seed"
    while (type != "location") {
        val map = maps[type]!!
        type = map.to
        for (entry in map.entries) {
            for (range in ranges) {
                if (entry.isRangeOverlaps(range)) {
                    val validIntersectionRange = entry.mapRange(range)
                    if (!validIntersectionRange.isEmpty()) {
                        nextRanges.add(validIntersectionRange)
                    }
                }
            }
        }
        ranges = nextRanges
        nextRanges = mutableListOf()
    }
    val min = ranges.minOfOrNull { it.start } ?: 0
    println("end result = $min")
}

private fun initMap(wholeInput: String): MutableMap<String, MyMap> {
    val splitted = wholeInput.split("\n\n")
    val maps = mutableMapOf<String, MyMap>()
    for (string in splitted) {
        if (string.startsWith("seeds")) {
            continue
        }
        val parsed = MyMap.parse(string)
        maps[parsed.from] = parsed
    }
    return maps
}

private fun seedRanges(lines: List<String>) = lines[0].split(":")[1].trim().split(" ")
    .map { it.toBigInteger() }
    .chunked(2)
    .map { (start, count) ->
        start.rangeUntil(start.plus(count))
    }

fun solutionV1(lines: List<String>, wholeInput: String) {
    val seeds = lines[0].split(":")[1].trim().split(" ").map { it.toBigInteger() }
    val splitted = wholeInput.split("\n\n");

    val maps = mutableMapOf<String, MyMap>()
    for (string in splitted) {
        if (string.startsWith("seeds")) {
            continue
        }
        val parsed = MyMap.parse(string)
        maps[parsed.from] = parsed
    }

    var lowestSeed = seeds.max()
    for (seed in seeds) {
        var number = seed
        var type = "seed"
        while (type != "location") {
            val map = maps[type]!!
            type = map.to
            for (entry in map.entries) {
                if (entry.isNumberInsideRange(number)) {
                    number = entry.mapNumber(number)
                    break
                }
            }
        }
        if (number < lowestSeed) {
            lowestSeed = number
        }
    }
    println(lowestSeed)
}

class MyMap(
    val from: String,
    val to: String,
    val entries: List<MyMapEntry>,
) {

    companion object {
        fun parse(string: String): MyMap {
            var lines = string.split("\n")
            val from = lines[0].split(" ")[0].split("-")[0]
            val to = lines[0].split(" ")[0].split("-")[2]
            lines = lines.subList(1, lines.size)
            val allLines = lines.filter { it.isNotBlank() }.map { it1 ->
                val spl = it1.split(" ").filter { it.isNotBlank() }.map { it.toBigInteger() }
                MyMapEntry(spl[0], spl[1], spl[2])
            }
            return MyMap(from, to, allLines)
        }
    }
}

class MyMapEntry(
    public val destinationRangeStart: BigInteger,
    public val sourceRangeStart: BigInteger,
    public val rangeSize: BigInteger
) {
    fun isNumberInsideRange(number: BigInteger): Boolean {
        return sourceRangeStart.rangeUntil(sourceRangeStart.plus(rangeSize)).contains(number)
    }

    fun isRangeOverlaps(numberRange: OpenEndRange<BigInteger>): Boolean {
        val range = sourceRangeStart.rangeUntil(sourceRangeStart.plus(rangeSize))
//(StartA <= EndB) and (EndA >= StartB)
        return range.start <= numberRange.endExclusive && range.endExclusive >= numberRange.start
    }

    fun mapNumber(number: BigInteger): BigInteger {
        val shift = destinationRangeStart.minus(sourceRangeStart)
        return number.plus(shift)
    }

    fun mapRange(numberRange: OpenEndRange<BigInteger>): OpenEndRange<BigInteger> {
        val range = sourceRangeStart.rangeUntil(sourceRangeStart.plus(rangeSize))
        //find czesc wspolna source range i numberRange
        val startMax = range.start.max(numberRange.start)
        val endMax = range.endExclusive.min(numberRange.endExclusive)

        return mapNumber(startMax).rangeUntil(mapNumber(endMax.plus(BigInteger.ONE)))
    }
}
