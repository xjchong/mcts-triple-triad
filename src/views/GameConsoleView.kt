package views

import models.GameState
import models.Position

class GameConsoleView: ConsoleLayout(32, 96) {

    private val boardConsoleView = BoardConsoleView()
    private val playerHandViews: List<HandConsoleView> = listOf(
        HandConsoleView(),
        HandConsoleView()
    )

    init {
        add(boardConsoleView, Position(0, 31))

        playerHandViews.getOrNull(0)?.run {
            this@GameConsoleView.add(this, HAND_L_POS)
        }
        playerHandViews.getOrNull(1)?.run {
            this@GameConsoleView.add(this, HAND_R_POS)
        }
    }

    fun bind(gameState: GameState) {
        // Bind the board.
        boardConsoleView.bind(gameState.board)

        // Bind the player hands.
        for (player in gameState.players) {
            when(player.id) {
                0 -> playerHandViews.getOrNull(0)?.bind(player)
                1 -> playerHandViews.getOrNull(1)?.bind(player)
            }
        }
    }

    companion object {
        val HAND_L_POS = Position(5, 0)
        val HAND_R_POS = Position(5, 64)
    }
}