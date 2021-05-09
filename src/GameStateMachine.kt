import models.*
import kotlin.random.Random

class GameStateMachine {

    var states: List<GameState> = listOf()
        private set

    fun initialize(players: List<Player>, advancedRules: List<AdvancedRule> = listOf(),
                   shouldShufflePlayers: Boolean = true): GameState {
        val startingPlayers = if (shouldShufflePlayers) players.shuffled() else players
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
    fun makeMove(playerCardIndex: Int, position: Position): GameState {
        val currentState = states.lastOrNull() ?:
            throw IllegalStateException("Need to initialize engine.")

        val playerCard = currentState.nextPlayer().cards.getOrNull(playerCardIndex) ?:
            throw IllegalStateException("Couldn't get a card for player ${currentState.nextPlayer().id} with card index $playerCardIndex.")

        return makeMove(playerCard, position)
    }

    @Throws(IllegalStateException::class)
    fun makeMove(playerCard: PlayerCard, position: Position): GameState {
        val currentState = states.lastOrNull() ?:
            throw IllegalStateException("Need to initialize engine.")

        val player = currentState.players.find { player ->
            player.cards.any { it.id == playerCard.id }
        } ?: throw IllegalStateException("No player owns the card being played.")

        requireCardPlayable(currentState, player, playerCard, position)

        // Place the card on the board.
        val nextBoard = placeCard(playerCard, position, currentState.board, currentState.advancedRules)

        // Remove the card from the player.
        val playerAfterMove = player.lessCard(playerCard)
        val playersAfterMove = currentState.players.map {
            if (it.id == playerAfterMove.id) playerAfterMove else it
        }

        val nextState = resolvePlayability(currentState.copy(board = nextBoard, players = playersAfterMove))

        states = states + nextState

        return nextState
    }

    @Throws(IllegalStateException::class)
    private fun placeCard(playerCard: PlayerCard, position: Position, board: Board,
                          advancedRules: List<AdvancedRule>): Board {
        var nextBoard = board.setCard(playerCard.unhidden(), position) ?:
            throw IllegalStateException("Board didn't allow placement.")

        for (advancedRule in advancedRules) {
            nextBoard = when(advancedRule) {
                Plus -> resolvePlacedCardPlus(nextBoard, position, advancedRules)
                Same -> resolvePlacedCardSame(nextBoard, position, advancedRules)
                else -> nextBoard
            }
        }

        nextBoard = when {
            advancedRules.contains(FallenAce) && advancedRules.contains(Reverse) -> {
                resolvePlacedCardFallenAceReverse(nextBoard, position)
            }
            advancedRules.contains(Reverse) -> resolvePlacedCardReverse(nextBoard, position)
            advancedRules.contains(FallenAce) -> resolvePlacedCardFallenAce(nextBoard, position)
            else -> resolvePlacedCardBasic(nextBoard, position)
        }

        return nextBoard
    }

    private fun resolvePlayability(gameStateAfterPlace: GameState): GameState {
        val nextPlayer = gameStateAfterPlace.nextPlayer()
        val advancedRules = gameStateAfterPlace.advancedRules
        val playableIndices = when {
            advancedRules.contains(Chaos) -> listOf(Random.nextInt(0, nextPlayer.cards.size))
            advancedRules.contains(Order) -> listOf(0)
            else -> nextPlayer.cards.indices
        }

        return gameStateAfterPlace.copy(players = gameStateAfterPlace.players.map { player ->
            if (player == nextPlayer) {
                player.withCards(player.cards.mapIndexed { cardIndex, card ->
                    if (cardIndex in playableIndices) card.playable() else card.unplayable()
                })
            } else {
                player.withCards(player.cards.map { it.unplayable() })
            }
        })

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

    private fun resolvePlacedCardSame(board: Board, position: Position, advancedRules: List<AdvancedRule>): Board {
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
            val comboPositions = mutableListOf<Position>()

            for (positionOfSame in positionsOfSameCards) {
                if (board.playerCards[positionOfSame]?.playerId != placedCard.playerId) {
                   comboPositions.add(positionOfSame)
                }

                nextBoard = nextBoard.flipped(positionOfSame, placedCard.playerId)
            }


            resolveCombo(comboPositions, nextBoard, advancedRules)
        } else board
    }

    private fun resolvePlacedCardPlus(board: Board, position: Position, advancedRules: List<AdvancedRule>): Board {
        val valuesToPositions = mutableMapOf<Int, MutableList<Position>>()
        val plusSet = mutableSetOf<Int>()
        val placedCard = board.playerCards[position] ?: return board
        val updateSet: (Int, Position) -> Unit = { value, otherPosition ->
            valuesToPositions.getOrDefault(value, mutableListOf()).run {
                if (isNotEmpty()) {
                    plusSet.add(value)
                }

                add(otherPosition)
                valuesToPositions[value] = this
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
            val comboPositions = mutableListOf<Position>()

            plusSet.forEach {
                valuesToPositions.getOrDefault(it, mutableListOf()).forEach { otherPosition ->
                    if (nextBoard.playerCards[otherPosition]?.playerId != placedCard.playerId) {
                        comboPositions.add(otherPosition)
                    }

                    nextBoard = nextBoard.flipped(otherPosition, placedCard.playerId)
                }
            }

            resolveCombo(comboPositions, nextBoard, advancedRules)
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

    private fun resolveAscension(gameState: GameState): GameState {
        return resolveModifier(gameState, 1)
    }

    private fun resolveDescension(gameState: GameState): GameState {
        return resolveModifier(gameState, -1)
    }

    private fun resolveModifier(gameState: GameState, modifierIncrement: Int): GameState {
        val typeCount = mutableMapOf<CardType, Int>()

        gameState.board.playerCards.values.filterNotNull().mapNotNull { it.card.type }.forEach { type ->
           typeCount[type] = typeCount.getOrDefault(type, 0) + 1
        }

        var nextState = gameState
        for ((type, count) in typeCount) {
            nextState = resolveModifier(nextState, type, count * modifierIncrement)
        }

        return nextState
    }

    private fun resolveModifier(gameState: GameState, type: CardType, modifier: Int): GameState {
        val nextPlayers = gameState.players.map { player ->
            player.withCards(
                player.cards.map { playerCard ->
                    if (playerCard.card.type == type) {
                        playerCard.modified(modifier)
                    } else {
                        playerCard
                    }
                }
            )
        }

        var nextBoard = gameState.board

        for ((position, playerCard) in gameState.board.playerCards) {
            if (playerCard == null || playerCard.card.type != type) continue

            nextBoard = nextBoard.setCard(playerCard.modified(modifier), position) ?: nextBoard
        }

        return GameState(nextBoard, nextPlayers, gameState.advancedRules)
    }

    private fun resolveCombo(positions: List<Position>, board: Board, advancedRules: List<AdvancedRule>): Board {
        if (positions.isEmpty()) return board

        var nextBoard = board

        for (position in positions) {
            nextBoard = when {
                advancedRules.contains(FallenAce) && advancedRules.contains(Reverse) -> {
                    resolvePlacedCardFallenAceReverse(nextBoard, position)
                }
                advancedRules.contains(Reverse) -> resolvePlacedCardReverse(nextBoard, position)
                advancedRules.contains(FallenAce) -> resolvePlacedCardFallenAce(nextBoard, position)
                else -> resolvePlacedCardBasic(nextBoard, position)
            }
        }

        val newlyFlippedPositions = nextBoard.playerCards.filter { (nextPosition, nextCard) ->
            nextCard != null &&
                board.playerCards[nextPosition]?.playerId != nextCard.playerId
        }.keys.toList()

        return resolveCombo(newlyFlippedPositions, nextBoard, advancedRules)
    }

    @Throws(IllegalStateException::class)
    private fun requireCardPlayable(currentState: GameState, player: Player, playerCard: PlayerCard,
                                    position: Position) {
        if (currentState.nextPlayer() != player)
            throw IllegalStateException("Card not playable due to not being this player's turn.")

        if (!player.cards.contains(playerCard))
            throw IllegalStateException("Card not playable due to the player not owning that card.")

        if (!playerCard.isPlayable)
            throw IllegalStateException("Card not playable on this turn.")

        if (!currentState.board.playerCards.containsKey(position))
            throw IllegalStateException("Card can't be played to a position that doesn't exist.")

        if (currentState.board.playerCards[position] != null)
            throw IllegalStateException("Card can't be played to a position that is occupied.")
    }

    private fun setupAllOpen(gameState: GameState): GameState {
        return gameState.copy(players = gameState.players.map { player ->
            player.withCards(player.cards.map {
                it.unhidden()
            })
        })
    }

    private fun setupThreeOpen(gameState: GameState): GameState {
        return gameState.copy(players = gameState.players.map { player ->
            val indicesToOpen = listOf(0, 1, 2, 3, 4).shuffled().take(3)

            player.withCards(player.cards.mapIndexed { index, playerCard ->
                if (index in indicesToOpen) playerCard.unhidden() else playerCard.hidden()
            })
        })
    }

    private fun setupOrder(gameState: GameState): GameState {
        return gameState.copy(players = gameState.players.mapIndexed { playerIndex, player ->
            if (playerIndex == 0) {
                player.withCards(player.cards.mapIndexed { cardIndex, card ->
                    if (cardIndex == 0) card.playable() else card.unplayable()
                })
            } else {
                player.withCards(player.cards.map { it.unplayable() })
            }
        })
    }

    private fun setupChaos(gameState: GameState): GameState {
        return gameState.copy(players = gameState.players.mapIndexed { playerIndex, player ->
            if (playerIndex == 0) {
                val randomCardIndex = Random.nextInt(0, player.cards.size)
                player.withCards(player.cards.mapIndexed { cardIndex, card ->
                    if (cardIndex == randomCardIndex) card.playable() else card.unplayable()
                })
            } else {
                player.withCards(player.cards.map { it.unplayable() })
            }
        })
    }

    private fun setupFreePlay(gameState: GameState): GameState {
        return gameState.copy(players = gameState.players.mapIndexed { playerIndex, player ->
            if (playerIndex == 0) {
                player.withCards(player.cards.map { it.playable() })
            } else {
                player.withCards(player.cards.map { it.unplayable() })
            }
        })
    }

    private fun setupSwap(gameState: GameState): GameState {
        return gameState
    }

    inner class IllegalStateException(message: String) : Exception(message)
}