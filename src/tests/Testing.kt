package tests

import GameStateMachine
import models.*
import views.GameConsoleView


object Testing {

    fun execute(test: Test, shouldDraw: Boolean = false) {
        val players = listOf(Player(0, test.player1Cards), Player(1, test.player2Cards)).run {
            if (test.doesPlayer1Start) this else this.reversed()
        }

        val states = GameStateMachine().apply {
            initialize(players, test.advancedRules, shouldShufflePlayers = false)
            test.moves.forEach { (cardIndex, position) ->
                makeMove(cardIndex, position)
            }
        }.states

        if (shouldDraw) {
            GameConsoleView(states.first()).also { gameConsoleView ->
                states.forEach { state ->
                    gameConsoleView.bind(state)
                    gameConsoleView.draw()
                }
            }
        }

        test.requirement?.let { requirement ->
            require(requirement(states)) {
                System.err.println("${test::class.simpleName} failed!")
            }
        }
    }

    fun unitTest() {
        unitTestSetup()
        unitTestPlacement()
    }

    private fun unitTestSetup() {
        execute(TestAllOpen)
        execute(TestThreeOpen)
    }

    private fun unitTestPlacement() {
        execute(TestBasicPlacement)
        execute(TestSamePlacement)
        execute(TestPlusPlacement)
    }
}