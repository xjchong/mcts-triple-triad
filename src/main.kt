import ai.GameStateMCTSNode
import ai.MCTS
import extensions.second
import models.*
import tests.Test
import tests.TestSame
import tests.Testing
import views.GameConsoleView

fun executePlaygroundTest() {
    Testing.execute(object : Test(
        arrayOf(Card.AlexanderPrime, Card.Ahriman, Card.DUD, Card.Mandragora, Card.Sabotender),
        arrayOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Adrammelech),
        advancedRules = listOf(Same, Chaos)
    ) {}, true)
}

fun testAi() {
    val gameEngine = GameEngine()

    val initialState = gameEngine.startGame(listOf(
        Player(1, arrayOf(Card.Adrammelech, Card.AlexanderPrime, Card.Dodo, Card.Mandragora, Card.Amaljaa)),
        Player(0, arrayOf(Card.Adamantoise, Card.Apkallu, Card.Bomb, Card.Coeurl, Card.Sabotender))),
        advancedRules = listOf(AllOpen, SuddenDeath),
        shouldShufflePlayers = false
    )

    val ai = MCTS()
    val gameConsoleView = GameConsoleView(initialState)
    var nextState = gameEngine.nextState()

    while (!nextState.isGameOver()) {
        println(nextState.movesPlayed)
        val move = if (nextState.nextPlayer().id == 0) {
            gameConsoleView.bind(nextState)
            gameConsoleView.draw()
            getPlayerMove(nextState)
        } else {
            getAiMove(ai, nextState)
        }

        gameEngine.playMove(move)
        nextState = gameEngine.nextState()

        if (nextState.nextPlayer().id == 1) {
            gameConsoleView.bind(nextState)
            gameConsoleView.draw()
        }
    }

    if (nextState.isGameOver() && nextState.nextPlayer().id == 0) {
        gameConsoleView.bind(nextState)
        gameConsoleView.draw()
    }
}

fun getAiMove(ai: MCTS, gameState: GameState): Move {
    val bestNode = ai.getBestNode(GameStateMCTSNode(gameState), null, 2000)

    return (bestNode as GameStateMCTSNode).moves.first()
}

fun getPlayerMove(gameState: GameState): Move {
    val validPositionStrings = listOf(
        "topleft", "top", "topright",
        "left", "center", "right",
        "bottomleft", "bottom", "bottomright"
    )

    while (true) {
        print("What is your move? (card index, position): ")
        val input = readLine() ?: continue
        val splitInput = input.toLowerCase().split(" ")

        if (splitInput.size != 2) {
            println("That's not a valid command!")
            continue
        }

        val cardIndex = splitInput.first().toIntOrNull()
        if (cardIndex == null || cardIndex >= gameState.nextPlayer().cards.size) {
            println("That's not a valid card index!")
            continue
        }

        val positionString = splitInput.second()
        if (positionString !in validPositionStrings) {
            println("That's not a valid position!")
            continue
        }

        val position = when(positionString) {
            "topleft" -> Position.TOP_LEFT
            "top" -> Position.TOP
            "topright" -> Position.TOP_RIGHT
            "left" -> Position.LEFT
            "center" -> Position.CENTER
            "right" -> Position.RIGHT
            "bottomleft" -> Position.BOTTOM_LEFT
            "bottom" -> Position.BOTTOM
            "bottomright" -> Position.BOTTOM_RIGHT
            else -> Position.CENTER
        }

        if (gameState.board.playerCards[position] != null) {
            println("There's already a card there!")
            continue
        }

        return Move(cardIndex, position)
    }


}

fun main() {
//    executePlaygroundTest()
//    Testing.execute(TestSameCombo, true)
//    Testing.executeUnitTests()
    testAi()
}