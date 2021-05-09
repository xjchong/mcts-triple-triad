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
        val playerCard = this.playerCard
        val card = playerCard?.card

        return if (playerCard == null || card == null) {
            "".padEnd(height * width, transparentChar)
        } else {
            // These 'r'ow and 'c'olumn variables are used for toggling the characters that indicate playability.
            val r = if (playerCard.isPlayable) '_' else ' '
            val c = if (playerCard.isPlayable) '|' else ' '
            "${if (playerCard.isPlayable) '.' else ' '}$r$r$r$r$r$r$r${if (playerCard.isPlayable) '.' else ' '}" +
            "$c${stars(card)} ${type(card)}$c" +
            "$c${if (playerCard.isHidden) "(" else " "}${card.name.take(5).padEnd(5)}${if (playerCard.isHidden) ")" else " "}$c"  +
            "$c       $c" +
            "$c   ${playerCard.n()}   $c" +
            "$c  ${playerCard.w()} ${playerCard.e()}  $c" +
            "$c$r$r$r${playerCard.s()}$r$r$r$c"
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