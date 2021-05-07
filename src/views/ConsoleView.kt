package views

import models.Position
import views.colors.ANSIColor
import views.colors.color


open class ConsoleView(var height: Int, var width: Int) {
    open val transparentChar: Char = ' '
    open val backgroundColor: ANSIColor? = null

    protected open fun getString(): String {
        return ""
    }

    open fun getTileMap(): Map<Position, Tile?> {
        val tileMap = mutableMapOf<Position, Tile?>()
        val maxLength = height * width
        val charArray = getString().take(maxLength).padEnd(maxLength, transparentChar).toCharArray()

        for (row in (0 until height)) {
            for (column in (0 until width)) {
                val char = charArray.getOrNull(getCharIndex(row, column)) ?: break

                if (char == transparentChar) continue

                tileMap[Position(row, column)] = if (char == transparentChar) null else Tile(char, backgroundColor)
            }
        }

        return tileMap
    }

    private fun getCharIndex(row: Int, column: Int): Int {
        return (row * width) + column
    }

    inner class Tile(var char: Char = ' ', var backgroundColor: ANSIColor? = null) {
        val output: String
            get() = {
                backgroundColor?.let { char.color(it) } ?: char.toString()
            }()
    }
}