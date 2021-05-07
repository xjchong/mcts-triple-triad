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
        } else {
            initialGameState = setupFreePlay(initialGameState)
        }

        states = listOf(initialGameState)

        return initialGameState
    }

    @Throws(IllegalStateException::class)
    fun makeMove(player: Player, playerCard: PlayerCard, position: Position): GameState {
        val currentState = states.lastOrNull() ?:
            throw IllegalStateException("Need to initialize engine.")

        requireCardPlayable(currentState, player, playerCard, position)

        // Place the card on the board.
        val nextBoard = currentState.board.setCard(playerCard, position) ?:
            throw IllegalStateException("Board didn't allow placement.")

        // Remove the card from the player.
        val playerAfterMove = player.lessCard(playerCard)
        val playersAfterMove = currentState.players.map {
            if (it.id == playerAfterMove.id) playerAfterMove else it
        }

        val nextState = currentState.copy(board = nextBoard, players = playersAfterMove)

        states = states + nextState

        return nextState
    }

    @Throws(IllegalStateException::class)
    private fun requireCardPlayable(currentState: GameState, player: Player, playerCard: PlayerCard,
                                    position: Position) {
        if (currentState.nextPlayer() != player)
            throw IllegalStateException("Card not playable due to not being this player's turn.")

        if (!player.cards.contains(playerCard))
            throw IllegalStateException("Card not playable due to the player not owning that card.")

        val currentTurn = currentState.getTurn()
        if (!playerCard.playableTurns.contains(currentTurn))
            throw IllegalStateException("Card not playable (${playerCard.playableTurns}) on this turn ($currentTurn)")

        if (!currentState.board.playerCards.containsKey(position))
            throw IllegalStateException("Card can't be played to a position that doesn't exist.")

        if (currentState.board.playerCards[position] != null)
            throw IllegalStateException("Card can't be played to a position that is occupied.")
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

    private fun setupFreePlay(gameState: GameState): GameState {
        return gameState
    }

    private fun setupSwap(gameState: GameState): GameState {
        return gameState
    }

    inner class IllegalStateException(message: String) : Exception(message)
}