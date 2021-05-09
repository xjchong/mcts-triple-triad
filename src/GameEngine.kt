import models.*

class GameEngine {

    val stateMachine: GameStateMachine = GameStateMachine()

    fun startGame(players: List<Player>, advancedRules: List<AdvancedRule>,
                  shouldShufflePlayers: Boolean = true): GameState {
        return stateMachine.initialize(players, advancedRules, shouldShufflePlayers)
    }

    fun nextState(): GameState {
        return redactState(stateMachine.states.last())
    }

    fun playMove(move: Move) {
        if (stateMachine.states.last().isGameOver()) {
            println("Game is over!")
        } else {
            stateMachine.makeMove(move.playerCardIndex, move.position)
        }
    }

    private fun redactState(gameState: GameState): GameState {
        val turnPlayerId = gameState.nextPlayer().id

        return gameState.copy(players = gameState.players.map { player ->
            if (player.id == turnPlayerId) {
                player
            } else {
                player.withCards(player.cards.map { playerCard ->
                    if (playerCard.isHidden) playerCard.copy(card = Card.UNKNOWN) else playerCard
                })
            }
        })

    }
}