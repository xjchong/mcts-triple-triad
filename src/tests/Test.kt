package tests

import models.*

abstract class Test(
    val player1Cards: Array<Card>,
    val player2Cards: Array<Card>,
    vararg moves: Move,
    val doesPlayer1Start: Boolean = true,
    val advancedRules: List<AdvancedRule> = listOf(),
    val requirement: ((states: List<GameState>) -> Boolean)? = null
) {
    val moves: List<Move> = moves.toList()


    companion object {
        val TOP_LEFT = Position(0, 0)
        val TOP = Position(0, 1)
        val TOP_RIGHT = Position(0, 2)
        val LEFT = Position(1, 0)
        val CENTER = Position(1, 1)
        val RIGHT = Position(1, 2)
        val BOTTOM_LEFT = Position(2, 0)
        val BOTTOM = Position(2, 1)
        val BOTTOM_RIGHT = Position(2, 2)

        fun PlayerCard?.isBlue(): Boolean = this?.playerId == 0
        fun PlayerCard?.isRed(): Boolean = this?.playerId == 1
    }
}
