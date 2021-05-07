package views

import models.Position

open class ConsoleLayout(height: Int, width: Int): ConsoleView(height, width) {

    private val views: MutableList<ConsoleView> = mutableListOf()
    private val viewPositions: MutableMap<ConsoleView, Position> = mutableMapOf()
    private var tileGrid: Array<Array<Tile?>> = clearTileGrid()

    final override fun getString(): String {
        return tileGrid.flatten().map { it?.char ?: transparentChar }.joinToString(separator = "")
    }

    final override fun getTileMap(): Map<Position, Tile?> {
        layoutViews()

        val tileMap = mutableMapOf<Position, Tile?>()

        for ((row, tileRow) in tileGrid.withIndex()) {
            for ((column, tile) in tileRow.withIndex()) {
                if (tile == null) continue

                tileMap[Position(row, column)] = tile
            }
        }

        return tileMap
    }

    fun add(view: ConsoleView, position: Position) {
        views.add(view)
        viewPositions[view] = position
    }

    fun remove(view: ConsoleView) {
        views.remove(view)
        viewPositions.remove(view)
    }

    fun clear() {
        views.clear()
        viewPositions.clear()
        clearTileGrid()
    }

    fun draw() {
        layoutViews()

        for (row in (0 until height)) {
            for (column in (0 until width)) {
                print(tileGrid[row][column]?.output ?: transparentChar)
            }
            println()
        }
    }

    private fun layoutViews() {
        clearTileGrid()
        views.forEach { layoutView(it) }
    }

    private fun layoutView(view: ConsoleView) {
        val (originRow, originColumn) = viewPositions[view] ?: return
        val tileMap = view.getTileMap()

        for (row in (0 until view.height)) {
            for (column in (0 until view.width)) {
                val actualRow = row + originRow
                val actualColumn = column + originColumn
                val tile = tileMap[Position(row, column)] ?: continue

                if (actualRow > height || actualColumn > width) break

                tileGrid[actualRow][actualColumn]?.run {
                    this.char = tile.char
                    this.backgroundColor = tile.backgroundColor
                } ?: {
                    tileGrid[actualRow][actualColumn] = Tile(tile.char, tile.backgroundColor)
                }()
            }
        }
    }

    private fun clearTileGrid(): Array<Array<Tile?>> {
        tileGrid = Array(height) { Array<Tile?>(width) { null } }

        return tileGrid
    }

}