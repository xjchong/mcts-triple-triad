package views

import models.*
import views.colors.ANSIColor

class CardConsoleView(private var playerCard: PlayerCard? = null): ConsoleView(HEIGHT, WIDTH) {
    override val transparentChar: Char = '#'
    override val backgroundColor: ANSIColor?
        get() = when {
            playerCard?.isPlayable == false -> ANSIColor.BG_WHITE
            playerCard?.playerId == 0 -> ANSIColor.BG_MAGENTA
            playerCard?.playerId == 1 -> ANSIColor.BG_RED
            else -> null
        }

    override fun getString(): String {
        val playerCard = this.playerCard
        val card = playerCard?.card

        return if (playerCard == null || card == null) {
            "".padEnd(height * width, transparentChar)
        } else {
            val isUnknown = card == Card.UNKNOWN
            "._______." +
            "|${stars(card)} ${type(card)}|" +
            "|${if (playerCard.isHidden && !isUnknown) "(" else " "}${card.name.take(5).padEnd(5)}${if (playerCard.isHidden && !isUnknown) ")" else " "}|"  +
            "|       |" +
            "|   ${if (isUnknown) " " else value(playerCard.n())}   |" +
            "|  ${if (isUnknown) " " else value(playerCard.w())} ${if (isUnknown) " " else value(playerCard.e())}  |" +
            "|___${if (isUnknown) " " else value(playerCard.s())}___|"
        }
    }

    fun bind(playerCard: PlayerCard?) {
        this.playerCard = playerCard
    }

    private fun value(number: Int): String {
        return when {
            number >= 10 -> "A"
            number <= 0 -> "1"
            else -> number.toString()
        }
    }

    private fun stars(card: Card): String {
        val rarity = card.rarity.value
        var string = ""

        if (card.rarity == CardRarity.Unknown) {
            return "     "
        }

        for (i in (0 until 5)) {
            string += if (i < rarity) "*" else " "
        }

        return string
    }

    private fun type(card: Card): String {
        return when (card.type) {
            null -> " "
            CardType.Beastman -> "B"
            CardType.Garland -> "G"
            CardType.Primal -> "P"
            CardType.Scions -> "S"
            CardType.Unknown -> " "
        }
    }

    companion object {
        const val HEIGHT = 7
        const val WIDTH = 9
    }
}