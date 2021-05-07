package views

import models.*
import views.colors.ANSIColor

class CardConsoleView(private var playerCard: PlayerCard? = null): ConsoleView(HEIGHT, WIDTH) {
    override val transparentChar: Char = '#'
    override val backgroundColor: ANSIColor?
        get() = {
            when (playerCard?.playerId) {
                0 -> ANSIColor.BG_MAGENTA
                1 -> ANSIColor.BG_RED
                else -> null
            }
        }()

    override fun getString(): String {
        val card = playerCard?.card

        return if (card == null) {
            "".padEnd(height * width, transparentChar)
        } else {
            "._______." +
            "|${stars(card)} ${type(card)}|" +
            "| ${card.name.take(5).padEnd(5)} |"  +
            "|       |" +
            "|   ${card.n}   |" +
            "|  ${card.w} ${card.e}  |" +
            "|___${card.s}___|"
        }
    }

    fun bind(playerCard: PlayerCard?) {
        this.playerCard = playerCard
    }

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