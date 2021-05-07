package views

import models.GameState
import models.Player
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

        // Layout board.
        // TODO
    }

    fun print(perspectivePlayer: Player) {
        for (player in currentGameState.players) {
            println("Player ${player.id}")
            for (card in player.cards) {
                if (card.playerId != perspectivePlayer.id && card.isHidden) {
                    println("Hidden")
                } else {
                    println(card)
                }
            }
            println()
        }

        val board = currentGameState.board

        for (row in (0 until board.rows)) {
            for (column in (0 until board.columns)) {
                print(board.playerCards[Position(row, column)])
            }
            println()
        }
    }

    companion object {
        val HAND_L_POS = Position(8, 0)
        val HAND_R_POS = Position(8, 64)
    }
}