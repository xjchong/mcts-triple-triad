package views

import models.GameState
import models.Position

class GameConsoleLayout(private val initialGameState: GameState): ConsoleLayout(32, 96) {

    private var currentGameState: GameState = initialGameState

    fun bind(gameState: GameState) {
        clear()
        currentGameState = gameState

        // Layout hands.
        for (player in initialGameState.players) {
            val playerCurrently = currentGameState.players.find { it.id == player.id } ?: continue

            for ((index, playerCard) in player.cards.withIndex()) {
                if (playerCurrently.cards.contains(playerCard)) {
                    val handPosition = if (player.id == 0) HAND_L_POS else HAND_R_POS
                    val position = when(index) {
                        0 -> handPosition
                        1 -> handPosition.withRelativeColumn(10)
                        2 -> handPosition.withRelativeColumn(20)
                        3 -> handPosition.withRelative(8, 5)
                        4 -> handPosition.withRelative(8, 15)
                        else -> null
                    }

                    add(CardConsoleView(playerCard), position)
                }
            }
        }

        add(BoardConsoleView(), Position(0, 31))

        // Layout board.
        // TODO
    }

    companion object {
        val HAND_L_POS = Position(5, 0)
        val HAND_R_POS = Position(5, 64)
    }
}