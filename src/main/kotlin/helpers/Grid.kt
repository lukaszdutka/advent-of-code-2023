package helpers

class Grid(lines: List<String>) {
    val size = lines.size
    private val grid: MutableList<MutableList<String>> =
        lines.map { row ->
            row.split("").filter { it.isNotBlank() }.toMutableList()
        }.toMutableList()

    fun getOrNull(coords: Point): String? = grid.getOrNull(coords.first)?.getOrNull(coords.second)

    fun getRightDownCoords() = Point(grid.size - 1, grid[0].size - 1)

    fun getAllCoordsThatSatisfy(condition: (input: String) -> Boolean): List<Point> {
        return grid.flatMapIndexed { y, row ->
            row.mapIndexedNotNull { x, element ->
                if (condition.invoke(element)) Point(y, x) else null
            }
        }
    }

    fun getAllCoordsThatSatisfy(condition: (y: Int, x: Int, value: String) -> Boolean): List<Point> {
        return grid.flatMapIndexed { y, row ->
            row.mapIndexedNotNull { x, element ->
                if (condition.invoke(y, x, element)) Point(y, x) else null
            }
        }
    }

    fun replaceAll(toReplace: List<String>, replaceWith: String) {
        grid.forEach { it -> it.replaceAll { if (toReplace.contains(it)) replaceWith else it } }
    }

    fun replace(toReplace: String, replaceWith: String) {
        grid.forEach { it -> it.replaceAll { if (toReplace == it) replaceWith else it } }
    }
}