package views

import models.Position
import views.colors.ANSIColor


open class ConsoleView(var height: Int, var width: Int) {
    open val transparentBit: Char = ' '
    open var position: Position? = null
    open val backgroundColor: ANSIColor? = null

    protected open fun getBitString(): String {
        return ""
    }

    fun getBitMap(): Map<Position, Char?> {
        val bitMap = mutableMapOf<Position, Char?>()
        val maxLength = height * width
        val bitArray = getBitString().take(maxLength).padEnd(maxLength, transparentBit).toCharArray()

        for (row in (0 until height)) {
            for (column in (0 until width)) {
                val bit = bitArray.getOrNull(getBitIndex(row, column)) ?: break

                if (bit == transparentBit) continue

                bitMap[Position(row, column)] = if (bit == transparentBit) null else bit
            }
        }

        return bitMap
    }

    private fun getBitIndex(row: Int, column: Int): Int {
        return (row * width) + column
    }
}