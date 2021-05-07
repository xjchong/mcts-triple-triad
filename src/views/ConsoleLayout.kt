package views

import models.Position
import views.colors.ANSIColor
import views.colors.color

open class ConsoleLayout(height: Int, width: Int):  ConsoleView(height, width) {

    inner class Tile(var bit: Char = ' ', var backgroundColor: ANSIColor? = null) {
        val output: String
            get() = {
                backgroundColor?.let { bit.color(it) } ?: bit.toString()
            }()
    }

    private val views: MutableList<ConsoleView> = mutableListOf()
    private val viewPositions: MutableMap<ConsoleView, Position?> = mutableMapOf()

    private var tileGrid: Array<Array<Tile>> = clearTileGrid()

    final override fun getBitString(): String {
        return tileGrid.flatten().map { it.bit }.toString()
    }

    fun add(view: ConsoleView, position: Position?) {
        view.position = position
        views.add(view)
        viewPositions[view] = view.position
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
                print(tileGrid[row][column].output)
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
        val bitMap = view.getBitMap()
        val backgroundColor = view.backgroundColor

        for (row in (0 until view.height)) {
            for (column in (0 until view.width)) {
                val actualRow = row + originRow
                val actualColumn = column + originColumn
                val bit = bitMap[Position(row, column)] ?: continue

                if (actualRow > height || actualColumn > width) break

                tileGrid[actualRow][actualColumn].run {
                    this.bit = bit
                    this.backgroundColor = backgroundColor
                }
            }
        }
    }

    private fun clearTileGrid(): Array<Array<Tile>> {
        tileGrid = Array(height) { Array(width) { Tile() } }

        return tileGrid
    }

}