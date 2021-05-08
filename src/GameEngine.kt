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
        val nextBoard = placeCard(playerCard, position, currentState.board, currentState.advancedRules)

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
    private fun placeCard(playerCard: PlayerCard, position: Position, board: Board,
                          advancedRules: List<AdvancedRule>): Board {
        var nextBoard = board.setCard(playerCard, position) ?:
            throw IllegalStateException("Board didn't allow placement.")

        nextBoard = resolvePlacedCardBasic(nextBoard, position)

        return nextBoard
    }

    private fun resolvePlacedCardBasic(board: Board, position: Position): Board {
        return resolvePlacedCardCompareWith(board, position) { placedValue, otherValue ->
            placedValue > otherValue
        }
    }

    private fun resolvePlacedCardReverse(board: Board, position: Position): Board {
        return resolvePlacedCardCompareWith(board, position) { placedValue, otherValue ->
            placedValue < otherValue
        }
    }

    private fun resolvePlacedCardSame(board: Board, position: Position): Board {
        val positionsOfSameCards = mutableListOf<Position>()
        val placedCard = board.playerCards[position] ?: return board

        board.playerCards[position.north()]?.run {
            if (placedCard.n() == s()) {
                positionsOfSameCards.add(position.north())
            }
        }
        board.playerCards[position.east()]?.run {
            if (placedCard.e() == w()) {
                positionsOfSameCards.add(position.east())
            }
        }
        board.playerCards[position.south()]?.run {
            if (placedCard.s() == n()) {
                positionsOfSameCards.add(position.south())
            }
        }
        board.playerCards[position.west()]?.run {
            if (placedCard.w() == e()) {
                positionsOfSameCards.add(position.west())
            }
        }

        return if (positionsOfSameCards.size >= 2) {
            var nextBoard = board

            for (positionOfSame in positionsOfSameCards) {
                nextBoard = board.flipped(positionOfSame, placedCard.playerId)
            }

            nextBoard
        } else board
    }

    private fun resolvePlacedCardPlus(board: Board, position: Position): Board {
        val valuesToPositions = mutableMapOf<Int, MutableList<Position>>()
        val plusSet = mutableSetOf<Int>()
        val placedCard = board.playerCards[position] ?: return board
        val updateSet: (Int, Position) -> Unit = { value, otherPosition ->
            valuesToPositions.getOrDefault(value, mutableListOf()).run {
                if (isNotEmpty()) {
                    plusSet.add(value)
                }

                add(otherPosition)
            }
        }

        board.playerCards[position.north()]?.run {
            updateSet(placedCard.n() + s(), position.north())
        }
        board.playerCards[position.east()]?.run {
            updateSet(placedCard.e() + w(), position.east())
        }
        board.playerCards[position.south()]?.run {
            updateSet(placedCard.s() + n(), position.south())
        }
        board.playerCards[position.west()]?.run {
            updateSet(placedCard.w() + e(), position.west())
        }

        return if (plusSet.isNotEmpty()) {
            var nextBoard = board

            plusSet.forEach {
                valuesToPositions.getOrDefault(it, mutableListOf()).forEach { otherPosition ->
                    nextBoard = nextBoard.flipped(otherPosition, placedCard.playerId)
                }
            }

            nextBoard
        } else board
    }

    private fun resolvePlacedCardFallenAce(board: Board, position: Position): Board {
        return resolvePlacedCardCompareWith(board, position) { placedValue, otherValue ->
            placedValue > otherValue || (placedValue == 1 && otherValue == 10)
        }
    }

    private fun resolvePlacedCardFallenAceReverse(board: Board, position: Position): Board {
        return resolvePlacedCardCompareWith(board, position) { placedValue, otherValue ->
            placedValue < otherValue || (placedValue == 10 && otherValue == 1)
        }
    }

    private fun resolvePlacedCardCompareWith(board: Board, position: Position,
                                             compare: (placedValue: Int, otherValue: Int) -> Boolean): Board {
        var nextBoard = board.flippedIf(position, position.north()) { placedCard, otherCard ->
            compare(placedCard.n(), otherCard.s())
        }
        nextBoard = nextBoard.flippedIf(position, position.east()) { placedCard, otherCard ->
            compare(placedCard.e(), otherCard.w())
        }
        nextBoard = nextBoard.flippedIf(position, position.south()) { placedCard, otherCard ->
            compare(placedCard.s(), otherCard.n())
        }
        nextBoard = nextBoard.flippedIf(position, position.west()) { placedCard, otherCard ->
            compare(placedCard.w(), otherCard.e())
        }

        return nextBoard
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