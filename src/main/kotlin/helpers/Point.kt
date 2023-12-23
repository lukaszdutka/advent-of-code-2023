package helpers

data class Point(val first: Int, val second: Int) {
    fun neighbours() = listOf(
        this.up(),
        this.right(),
        this.down(),
        this.left()
    )

    fun diagonalNeighbours() = listOf(
        this.up().right(),
        this.up().left(),
        this.down().left(),
        this.down().right()
    )

    fun all8Neighbours() = neighbours() + diagonalNeighbours()

    fun up() = Point(this.first - 1, this.second)
    fun down() = Point(this.first + 1, this.second)
    fun right() = Point(this.first, this.second + 1)
    fun left() = Point(this.first, this.second - 1)
    fun asPair(): Pair<Int, Int> = first to second
}
