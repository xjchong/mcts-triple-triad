package utilities

import GameStateMachine
import models.*
import views.GameConsoleView


object Testing {

    data class Move(val playerCardIndex: Int, val position: Position)

    val TOP_LEFT = Position(0, 0)
    val TOP = Position(0, 1)
    val TOP_RIGHT = Position(0, 2)
    val LEFT = Position(1, 0)
    val CENTER = Position(1, 1)
    val RIGHT = Position(1, 2)
    val BOTTOM_LEFT = Position(2, 0)
    val BOTTOM = Position(2, 1)
    val BOTTOM_RIGHT = Position(2, 2)

    fun unitTest() {
        unitTestSetup()
        unitTestPlacement()
    }

    private fun unitTestSetup() {
        testAllOpen(false)
        testThreeOpen(false)
    }

    private fun unitTestPlacement() {
        testRegularPlacement(false)
        testSamePlacement(false)
        testPlusPlacement(false)
    }

    fun testRegularPlacement(shouldDraw: Boolean = true) {
        val (initialState, endState) = test(
            Player(0, setOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender)),
            Player(1, setOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender)),
            Move(4, LEFT),
            Move(2, CENTER),
            advancedRules = listOf(AllOpen),
            shouldDraw = shouldDraw
        )
    }

    fun testThreeOpen(shouldDraw: Boolean = true) {
        val (initialState, endState) = test(
            Player(0, setOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender)),
            Player(1, setOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender)),
            advancedRules = listOf(ThreeOpen),
            shouldDraw = shouldDraw
        )

        require(endState.players.flatMap { it.cards }.sumBy { if (it.isHidden) 0 else 1 } == 6) {
            drawState(initialState, endState)
        }
    }

    fun testAllOpen(shouldDraw: Boolean = true) {
        val (initialState, endState) = test(
            Player(0, setOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender)),
            Player(1, setOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender)),
            advancedRules = listOf(AllOpen, ThreeOpen),
            shouldDraw = shouldDraw
        )

        require(endState.players.flatMap { it.cards }.all { !it.isHidden }) { drawState(initialState, endState) }
    }

    fun testSamePlacement(shouldDraw: Boolean = true) {
        val (initialState, endState) = test(
            Player(0, setOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender)),
            Player(1, setOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender)),
            Move(0, CENTER),
            Move(0, BOTTOM_LEFT),
            Move(3, LEFT),
            Move(2, BOTTOM),
            Move(1, BOTTOM_RIGHT),
            Move(1, RIGHT),
            advancedRules = listOf(Same),
            shouldDraw = shouldDraw
        )

        require(with(endState.board) {
            listOf(
                playerCards[LEFT].isBlue(),
                playerCards[CENTER].isRed(),
                playerCards[RIGHT].isRed(),
                playerCards[BOTTOM_LEFT].isBlue(),
                playerCards[BOTTOM].isBlue(),
                playerCards[BOTTOM_RIGHT].isBlue()
            ).all { it }
        }) {
            drawState(initialState, endState)
        }
    }

    fun testPlusPlacement(shouldDraw: Boolean = true) {
        val (initialState, endState) = test(
            Player(0, setOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender)),
            Player(1, setOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender)),
            Move(0, CENTER),
            Move(0, BOTTOM_LEFT),
            Move(3, LEFT),
            Move(2, BOTTOM),
            Move(1, BOTTOM_RIGHT),
            Move(1, RIGHT),
            advancedRules = listOf(Plus),
            shouldDraw = shouldDraw
        )
    }

    private fun test(startingPlayer: Player, followingPlayer: Player, vararg moves: Move,
                     advancedRules: List<AdvancedRule> = listOf(), shouldDraw: Boolean = true): Pair<GameState, GameState> {
        val gameStateMachine = GameStateMachine().apply {
            initialize(listOf(startingPlayer, followingPlayer), advancedRules = advancedRules, shouldShufflePlayers = false)
        }

        val initialState = gameStateMachine.states.last()

        val gameConsoleView = GameConsoleView(gameStateMachine.states.last()).also {
            if (shouldDraw) {
                it.draw()
            }
        }

        moves.forEach { (cardIndex, position) ->
            gameStateMachine.makeMove(cardIndex, position)
            if (shouldDraw) {
                gameConsoleView.bind(gameStateMachine.states.last())
                gameConsoleView.draw()
            }
        }

        return Pair(initialState, gameStateMachine.states.last())
    }

    private fun drawState(initialState: GameState, endState: GameState) {
        GameConsoleView(initialState).run {
            bind(endState)
            draw()
        }
    }

    private fun PlayerCard?.isBlue(): Boolean = this?.playerId == 0
    private fun PlayerCard?.isRed(): Boolean = this?.playerId == 1
}