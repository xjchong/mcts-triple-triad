package models

data class Position(val row: Int, val column: Int) {

    fun neighbors(): List<Position> {
        return listOf(north(), east(), south(), west())
    }

    fun north(): Position {
        return this.withRelativeRow(-1)
    }

    fun east(): Position {
        return this.withRelativeColumn(1)
    }

    fun south(): Position {
        return this.withRelativeRow(1)
    }

    fun west(): Position {
        return this.withRelativeColumn(-1)
    }

    fun withRelativeRow(delta: Int): Position {
        return copy(row = row + delta)
    }

    fun withRelativeColumn(delta: Int): Position {
        return copy(column = column + delta)
    }

    fun withRelative(row: Int, column: Int): Position {
        return copy(row = this.row + row, column = this.column + column)
    }

    companion object {
        val ORIGIN = Position(0, 0)
    }
}