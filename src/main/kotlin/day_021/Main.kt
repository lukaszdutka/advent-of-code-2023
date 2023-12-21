package day_021

import java.io.File

private const val inputPath =
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_021/input.txt"
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_021/input_small.txt"


//bad 696
fun main() {
    val lines = File(inputPath).readLines()
    val inputs = mutableListOf<MutableList<String>>()
    val inputs2 = mutableListOf<MutableList<String>>()

    for (line in lines) {
        inputs.add(line.split("").filter { it.isNotBlank() }.toMutableList())
        inputs2.add(line.split("").filter { it.isNotBlank() }.toMutableList())
    }

    println("v1=${solutionV1(inputs)}")
//    println("v2=${solutionV2(inputs)}")
}

fun solutionV1(inputs: MutableList<MutableList<String>>): Int {
    val startingPoint = startingPoint(inputs)
//    val maxNumberOfSteps = 26501365
    val maxNumberOfSteps = 200
    val checked = mutableMapOf<Pair<Int, Int>, Int>()

//    println(-3 % inputs.size) // 8
//    println(-2 % inputs.size) // 9
//    println(-1 % inputs.size) // 10
//    println(0 % inputs.size) // 0
//    println(1 % inputs.size) // 1
//    println(11 % inputs.size) // 0
//    println(12 % inputs.size) // 1
//    println(13 % inputs.size) // 2


    fun calculateSteps(point: Pair<Int, Int>, steps: Int) {
        val field = inputs.getOrNull(point.first % inputs.size)
            ?.getOrNull(point.second % inputs[0].size)
        if (field == "#") {
            return
        }
        if (steps > maxNumberOfSteps) {
            return
        }
        if ((checked[point] ?: Int.MAX_VALUE) <= steps) {
            return
        }
        checked[point] = steps

        calculateSteps(point.up(), steps + 1)
        calculateSteps(point.down(), steps + 1)
        calculateSteps(point.right(), steps + 1)
        calculateSteps(point.left(), steps + 1)
    }

    calculateSteps(startingPoint, 0)

    inputs.prettyPrint(checked)
    val evenSteps = checked.filter { it.value % 2 == 0 }
    val unique = evenSteps.map { it.key }.toSet()
    val result = unique.size
    return result
}

private fun <String> List<List<String>>.prettyPrint(checked: MutableMap<Pair<Int, Int>, Int>) {
    for ((y, row) in this.withIndex()) {
        for ((x, char) in row.withIndex()) {
            if ((checked[y to x] ?: 1) % 2 == 0) {
                print("x")
            } else {
                print(char)
            }
        }
        println()
    }
}


private fun startingPoint(inputs: MutableList<MutableList<String>>): Pair<Int, Int> {
    for ((y, row) in inputs.withIndex()) {
        for ((x, char) in row.withIndex()) {
            if (char == "S") {
                return y to x
            }
        }
    }
    throw RuntimeException("no start point")
}


private fun Pair<Int, Int>.up() = this.first - 1 to this.second
private fun Pair<Int, Int>.down() = this.first + 1 to this.second
private fun Pair<Int, Int>.right() = this.first to this.second + 1
private fun Pair<Int, Int>.left() = this.first to this.second - 1