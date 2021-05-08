import extensions.second
import models.*
import views.GameConsoleView

data class Move(val playerCardIndex: Int, val position: Position)

object Testing {

    val TOP_LEFT = Position(0, 0)
    val TOP = Position(0, 1)
    val TOP_RIGHT = Position(0, 2)
    val LEFT = Position(1, 0)
    val CENTER = Position(1, 1)
    val RIGHT = Position(1, 2)
    val BOTTOM_LEFT = Position(2, 0)
    val BOTTOM = Position(2, 1)
    val BOTTOM_RIGHT = Position(2, 2)

    fun testRegularPlacement() {
        test(
            Player(0, setOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender)),
            Player(1, setOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender)),
            Move(4, LEFT),
            Move(2, CENTER),
            advancedRules = listOf(AllOpen)
        )
    }

    fun testSetup() {
        test(
            Player(0, setOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender)),
            Player(1, setOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender)),
            advancedRules = listOf(AllOpen)
        )
    }

    fun testSamePlacement() {
        test(
            Player(0, setOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender)),
            Player(1, setOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender)),
            Move(0, CENTER),
            Move(0, BOTTOM_LEFT),
            Move(3, LEFT),
            Move(2, BOTTOM),
            Move(1, BOTTOM_RIGHT),
            Move(1, RIGHT),
            advancedRules = listOf(Same)
        )
    }

    private fun test(startingPlayer: Player, followingPlayer: Player, vararg moves: Move,
                     advancedRules: List<AdvancedRule> = listOf()) {
        val gameEngine = GameEngine().apply {
            initialize(listOf(startingPlayer, followingPlayer), advancedRules = advancedRules, shouldShufflePlayers = false)
        }

        val gameConsoleView = GameConsoleView(gameEngine.states.last())

        moves.forEach { (cardIndex, position) ->
            gameEngine.makeMove(cardIndex, position)
            gameConsoleView.bind(gameEngine.states.last())
            gameConsoleView.draw()
        }
    }
}


fun main() {
    Testing.testSamePlacement()
}