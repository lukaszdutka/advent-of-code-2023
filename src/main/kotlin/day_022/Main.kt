package day_022

import java.io.File

private const val inputPath =
    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_022/input.txt"
//    "/Users/lukaszdutka/IdeaProjects/fun/advent-of-code-2023/src/main/kotlin/day_022/input_small.txt"

fun main() {
    val lines = File(inputPath).readLines()

    calculateSolutionV1(lines)
}

//z=0 ground, brick can be only on z=1
//xyz
fun calculateSolutionV1(lines: List<String>) {
    checks()

    val supportedByMap = mutableMapOf<Int, MutableList<Int>>()
    val supportsMap = mutableMapOf<Int, MutableList<Int>>()
    val bricks = lines
        .mapIndexed { index, line -> line.parse(index) }
        .sortedBy { it.zStart }

    bricks.forEach { supportedByMap[it.id] = mutableListOf() }
    bricks.forEach { supportsMap[it.id] = mutableListOf() }

    dropBricks(bricks, supportedByMap, supportsMap)

    checkForNonMaxFallenBricks(bricks)

    println("Result part 1: ${calculateResultForPart1(bricks, supportsMap, supportedByMap)}")
    println("Result part 2: ${calculateResultForPart2(bricks)}")
}

private fun calculateResultForPart2(bricks: List<Brick>): Long {
    var result = 0L
    for (brick in bricks) {
        val newList = bricks.filter { it.id != brick.id }.map { it.copy() }.toMutableList()
        result += howMuchCanFall(newList)
    }
    return result
}

fun howMuchCanFall(bricks: MutableList<Brick>): Long {
    var count = 0L
    for (brick in bricks) {
        if (brick.zStart > 1) {
            brick.fall()
            if (!brick.intersectWithAny(bricks)) {
                count++
            } else {
                brick.fall(-1)
            }
        }
    }
    return count
}

private fun calculateResultForPart1(
    bricks: List<Brick>,
    supportsMap: MutableMap<Int, MutableList<Int>>,
    supportedByMap: MutableMap<Int, MutableList<Int>>
): Int {
    var count = 0
    for (brick in bricks) {
        "Counting result for: ${brick.id}".print()
        //jeśli brick nie podpiera niczego, to +1
        //jeśli brick podpiera coś, ale:
        // każde podparcie zawiera inne cegły (size >1) to +1
        //if brick jest jedynym podparciem dowolnej z cegiel, to false

        val bricksSupportedByMyBrick = supportsMap[brick.id]!!
        if (bricksSupportedByMyBrick.size == 0) {
            count++
            continue
        }
        if (bricksSupportedByMyBrick.all { supportedByMap[it]!!.size > 1 }) {
            count++
        }
    }
    return count
}

private fun dropBricks(
    bricks: List<Brick>,
    supportedByMap: MutableMap<Int, MutableList<Int>>,
    supportsMap: MutableMap<Int, MutableList<Int>>
) {
    for (brick in bricks) {
        "Falling brick: $brick".print()
        if (brick.zStart == 1) {
            continue
        }
        var droppedAtLeastOnce = false
        while (bricks.none { it.intersect(brick) } && brick.zStart > 0) {
            droppedAtLeastOnce = true
            brick.fall(1)
        }
        val supportedBy = bricks.filter { brick.intersect(it) }.map { it.id }.toMutableList()
        supportedBy.forEach { supportedByMap[brick.id]!!.add(it) }
        supportedBy.forEach { supportsMap[it]!!.add(brick.id) }
        if (droppedAtLeastOnce) {
            brick.fall(-1)
        }
    }
}

private fun checkForNonMaxFallenBricks(bricks: List<Brick>) {
    for (brick in bricks) {
        if (brick.zStart == 1) {
            continue
        }
        brick.fall(1)
        if (bricks.none { it.intersect(brick) }) {
            throw RuntimeException("Shouldnt be able to drop more!")
        }
        brick.fall(-1)
    }
}

fun checks() {
    val b0 = Brick(0, 0, 0, 0, 10, 10, 2)
    val b1 = Brick(1, 0, 0, 0, 0, 0, 0)
    val b2 = Brick(2, 1, 1, 1, 1, 1, 1)
    val b3 = Brick(3, 1, 0, 1, 1, 0, 1)
    preCheck(b1.intersect(b1), false)
    preCheck(b1.intersect(b2), false)
    preCheck(b2.intersect(b3), false)
    preCheck(b0.intersect(b1), true)
    preCheck(b0.intersect(b2), true)
    preCheck(b0.intersect(b3), true)
}

private fun Any.print() {
    println(this)
}

private fun String.parse(index: Int): Brick {
    val (start, end) = this.split("~")
    val (xS, yS, zS) = start.split(",").map { it.toInt() }
    val (xE, yE, zE) = end.split(",").map { it.toInt() }

    return Brick(index, xS, yS, zS, xE, yE, zE)
}

data class Brick(
    val id: Int,
    val xStart: Int,
    val yStart: Int,
    var zStart: Int,
    val xEnd: Int,
    val yEnd: Int,
    var zEnd: Int
) {
    fun fall(amount: Int = 1) {
        zStart -= amount
        zEnd -= amount
    }

    fun intersectWithAny(bricks: MutableList<Brick>) = bricks.any { it.intersect(this) }

    fun intersect(other: Brick): Boolean {
        if (id == other.id) {
            return false
        }
        return ((other.xStart in xStart..xEnd) || (xStart in other.xStart..other.xEnd)) &&
                ((other.yStart in yStart..yEnd) || (yStart in other.yStart..other.yEnd)) &&
                ((other.zStart in zStart..zEnd) || (zStart in other.zStart..other.zEnd))

    }
}

fun preCheck(actual: Any, expected: Any) {
    if (actual != expected) {
        throw RuntimeException("actual=$actual, expected=$expected")
    }
}


//    val cachedFalls = mutableMapOf<Int, Long>() //id to number of bricks
//    fun calculateFallenIfDisintegrated(brickId: Int): Long {
//        if (brickId in cachedFalls) {
//            return cachedFalls[brickId]!!
//        }
//        val myBrickSupportsThose = supportsMap[brickId]!!
//        if (myBrickSupportsThose.size == 0) {
//            return 0
//        }
//        val theOnlySupportIsThisOne = myBrickSupportsThose.filter { supportedByMap[it]!!.size == 1 }
//        cachedFalls[brickId] = theOnlySupportIsThisOne.size.toLong() +
//                theOnlySupportIsThisOne.sumOf { calculateFallenIfDisintegrated(it) }
//        return cachedFalls[brickId]!!
//    }