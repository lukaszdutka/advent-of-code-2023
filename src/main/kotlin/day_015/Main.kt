package day_015

import java.io.File

private const val inputPath =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_015/input.txt"
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_015/input_small.txt"

fun main() {
    val parts = File(inputPath).readLines()[0].split(",")

    println(solveV1(parts))
    println(solveV2(parts))
}

fun solveV2(parts: List<String>): Long {
    // =X / -

    val hashMap = HashMap<Int, MutableList<Pair<String, Int>>>()
    for (i in 0..255) {
        hashMap[i] = mutableListOf()
    }

    fun calculateV2(part: String) {
        //rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
        // rn - miejsce w hashmapie od 0 do 255
        // =1 => idź do boxu obliczonego i wsadź Lens(focalLength = 1) = wymień startowy, albo dodaj na koniec
        // - => idź do boxu obliczonego i usuń label "rn" jeśli tam jest,
        //          a potem przesuń resztę w prawo tak, żeby nie zmienić ich kolejności (czyli przed następnym kluczem imo)
        val code: String
        var focalLength = -1
        if (part.contains("=")) {
            code = part.split("=")[0]
            focalLength = part.split("=")[1].toInt()
        } else {
            code = part.split("-")[0]
        }

        val hash = calculateSingleHash(code)

        if (focalLength == -1) { // case "-"
            val indexOfFirst = hashMap[hash]!!.indexOfFirst { it.first == code }
            if (indexOfFirst != -1) { // jeśli znaleziono
                hashMap[hash]!!.removeAt(indexOfFirst)
            }
        } else { // case "="
            val indexOfFirst = hashMap[hash]!!.indexOfFirst { it.first == code }
            if (indexOfFirst == -1) {
                hashMap[hash]!!.add(Pair(code, focalLength))
            } else {
                hashMap[hash]!![indexOfFirst] = Pair(code, focalLength)
            }
        }
    }

    parts.forEach { calculateV2(it) }

    return calculateResultFrom(hashMap)
//    return parts.sumOf { calculateV2(it) }
}

fun calculateResultFrom(hashMap: HashMap<Int, MutableList<Pair<String, Int>>>): Long {
    var sum = 0L
    for (i in hashMap.keys) {
        val boxNumber = i + 1
        val boxLenses = hashMap[i]!!
        for ((j, lense) in boxLenses.withIndex()) {
            val slotNumber = j + 1
            val focalStrength = lense.second
            sum += boxNumber * slotNumber * focalStrength
        }
    }
    return sum
}

fun solveV1(parts: List<String>): Int {
    return parts.sumOf { calculateSingleHash(it) }
}

fun calculateSingleHash(singleHash: String): Int {
    var hash = 0
    for (char in singleHash.toCharArray()) {
        hash += char.code
        hash *= 17
        hash %= 256
    }
//    println("$singleHash => $hash")
    return hash

    //Determine the ASCII code for the current character of the string.
    //Increase the current value by the ASCII code you just determined.
    //Set the current value to itself multiplied by 17.
    //Set the current value to the remainder of dividing itself by 256.
}
