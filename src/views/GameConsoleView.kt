package views

import models.GameState
import models.Position

class GameConsoleView(private val initialGameState: GameState): ConsoleLayout(32, 96) {

    private var currentGameState: GameState = initialGameState
    private val boardConsoleView = BoardConsoleView(currentGameState.board)
    private val playerHandViews: List<HandConsoleView>

    init {
        add(boardConsoleView, Position(0, 31))

        playerHandViews = initialGameState.players.map {
            HandConsoleView(it)
        }

        playerHandViews.getOrNull(0)?.run {
            this@GameConsoleView.add(this, HAND_L_POS)
        }
        playerHandViews.getOrNull(1)?.run {
            this@GameConsoleView.add(this, HAND_R_POS)
        }
    }

    fun bind(gameState: GameState) {
        currentGameState = gameState

        // Bind the board.
        boardConsoleView.bind(gameState.board)

        // Bind the player hands.
        for ((index, player) in gameState.players.withIndex()) {
            playerHandViews.getOrNull(index)?.bind(player)
        }
    }

    companion object {
        val HAND_L_POS = Position(5, 0)
        val HAND_R_POS = Position(5, 64)
    }
}