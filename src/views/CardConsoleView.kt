package views

import models.*

class CardConsoleView(playerCard: PlayerCard): ConsoleView(HEIGHT, WIDTH) {

    override var bitString: String = {
        val card = playerCard.card

        "._______." +
        "|${stars(card)} ${type(card)}|" +
        "| ${card.name.take(5).padEnd(5)} |"  +
        "|       |" +
        "|   ${card.n}   |" +
        "|  ${card.w} ${card.e}  |" +
        "|___${card.s}___|"
    }()

    private fun stars(card: Card): String {
        val rarity = card.rarity.value
        var string = ""

        for (i in (0 until 5)) {
            string += if (i < rarity) "*" else " "
        }

        return string
    }

    private fun type(card: Card): String {
        return when (card.type) {
            null -> " "
            Beastman -> "B"
            Garland -> "G"
            Primal -> "P"
            Scions -> "S"
        }
    }

    companion object {
        const val HEIGHT = 7
        const val WIDTH = 9
    }
}