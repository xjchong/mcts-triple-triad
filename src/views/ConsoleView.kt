package views

import models.Position


open class ConsoleView(var height: Int, var width: Int) {
    open val transparentBit = ' '
    open val opaqueBit: Char = '#'
    open var bitString: String = ""
    open var position: Position? = null

    fun getBitMap(): Map<Position, Char> {
        val bitMap = mutableMapOf<Position, Char>()
        val bitArray = bitString.toCharArray()

        for (row in (0 until height)) {
            for (column in (0 until width)) {
                val bit = bitArray.getOrNull(getBitIndex(row, column)) ?: break

                if (bit == transparentBit) continue

                bitMap[Position(row, column)] = if (bit == opaqueBit) transparentBit else bit
            }
        }

        return bitMap
    }

    private fun getBitIndex(row: Int, column: Int): Int {
        return (row * width) + column
    }
}