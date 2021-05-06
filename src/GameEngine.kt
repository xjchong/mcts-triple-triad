import models.*

class GameEngine() {

    var states: List<GameState> = listOf()
        private set

    fun initialize(players: List<Player>, advancedRules: List<AdvancedRule> = listOf()): GameState {
        val startingPlayers = players.shuffled()
        var initialGameState = GameState(Board.standardInstance(), startingPlayers, advancedRules)

        // Setup the advanced rules.
        if (advancedRules.contains(AllOpen)) {
            initialGameState = setupAllOpen(initialGameState)
        } else if (advancedRules.contains(ThreeOpen)) {
            initialGameState = setupThreeOpen(initialGameState)
        }

        if (advancedRules.contains(Swap)) {
            initialGameState = setupSwap(initialGameState)
        }

        if (advancedRules.contains(Chaos)) {
            initialGameState = setupChaos(initialGameState)
        } else if (advancedRules.contains(Order)) {
            initialGameState = setupOrder(initialGameState)
        }

        states = listOf(initialGameState)

        return initialGameState
    }

    fun makeMove(player: Player, playerCard: PlayerCard, position: Position): GameState? {
        val currentState = states.lastOrNull() ?: return null // Need to initialize the engine.

        // Check if the card is playable.
        if (!isCardPlayable(currentState, player, playerCard, position)) return null

        // Place the card on the board.
        val nextBoard = currentState.board.setCard(playerCard, position) ?: return null // Couldn't resolve the board for some reason after placing.

        // Remove the card from the player.
        val playerAfterMove = player.lessCard(playerCard)
        val playersAfterMove = currentState.players.map {
            if (it.id == playerAfterMove.id) playerAfterMove else it
        }

        val nextState = currentState.copy(board = nextBoard, players = playersAfterMove)

        states = states + nextState

        return nextState
    }

    private fun isCardPlayable(currentState: GameState, player: Player, playerCard: PlayerCard,
                               position: Position): Boolean {
        if (currentState.nextPlayer() != player) return false // It is not this player's turn.

        // Check if the player actually owns the card.
        if (!player.cards.contains(playerCard)) return false

        // Check that the card is playable given its turn restrictions.
        val currentTurn = currentState.getTurn()
        if (!playerCard.playableTurns.contains(currentTurn)) return false

        // Check that the position exists on the board.
        if (!currentState.board.playerCards.containsKey(position)) return false

        // Check that the position is empty on the board.
        if (currentState.board.playerCards[position] != null) return false

        return true
    }

    private fun setupAllOpen(gameState: GameState): GameState {
        return gameState
    }

    private fun setupThreeOpen(gameState: GameState): GameState {
        return gameState
    }

    private fun setupOrder(gameState: GameState): GameState {
        return gameState
    }

    private fun setupChaos(gameState: GameState): GameState {
        return gameState
    }

    private fun setupSwap(gameState: GameState): GameState {
        return gameState
    }
}