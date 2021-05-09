package ai

import GameStateMachine
import models.*
import kotlin.math.ln
import kotlin.math.roundToInt
import kotlin.math.sqrt

abstract class MCTSNode {
    abstract val parent: MCTSNode?
    abstract val children: List<MCTSNode>

    var accumulatedValue: Double = 0.0
        private set
    var visits: Int = 0
        private set

    val isFullyVisited: Boolean
        get() = children.all { it.visits > 0 }

    val isLeaf: Boolean
        get() = children.isEmpty()

    abstract val stateValue: Double

    abstract fun rollout(): Double

    fun backpropagate(value: Double) {
        ++visits
        accumulatedValue += value
        parent?.backpropagate(value)
    }
}

class GameStateMCTSNode(private val rootState: GameState, val moves: List<Move> = listOf(),
                        override val parent: MCTSNode? = null): MCTSNode() {

    override val children: List<MCTSNode> by lazy {
        // The number of cards left can be calculated by the number of moves played in conjunction with the root state.
        val rootCardsCount = rootState.players.flatMap { it.cards }.size
        var cardsCount = ((rootCardsCount - moves.size) / 2.0).roundToInt()
        val possibleChildren = mutableListOf<GameStateMCTSNode>()
        val rootOccupiedPositions = rootState.board.playerCards.filter { it.value != null }.keys
        val moveOccupiedPositions = moves.map { it.position }
        val remainingPositions = sequenceOf(
            Position.TOP_LEFT, Position.TOP, Position.TOP_RIGHT,
            Position.LEFT, Position.CENTER, Position.RIGHT,
            Position.BOTTOM_LEFT, Position.BOTTOM, Position.BOTTOM_RIGHT
        ).filter { position ->
            position !in moveOccupiedPositions && position !in rootOccupiedPositions
        }

        if (rootState.advancedRules.contains(Order)) {
            cardsCount = 1
        }

        for (cardIndex in (0 until cardsCount)) {
            for (position in remainingPositions) {
                val nextMoves = moves + listOf(Move(cardIndex, position))

                possibleChildren.add(
                    GameStateMCTSNode(rootState, nextMoves, this)
                )
            }
        }

        possibleChildren
    }

    override val stateValue: Double
        get() = {
            val gameStateMachine = GameStateMachine().also {
                it.initialize(getPossibleInitialState())
            }

            moves.forEach {
                gameStateMachine.makeMove(it.playerCardIndex, it.position)
            }

            val endState = gameStateMachine.states.last()
            val playerId = rootState.nextPlayer().id
            val boardPoints = endState.board.playerCards.values.filter { it?.playerId == playerId }.size
            val handPoints = endState.players.flatMap { it.cards }.filter { it.playerId == playerId }.size

            if (boardPoints + handPoints > 5) 1.0 else 0.0
        }()

    override fun rollout(): Double {
        var currentNode: MCTSNode = this

        while (!currentNode.isLeaf) {
            currentNode = currentNode.children.random()
        }

        return currentNode.stateValue
    }

    private fun getPossibleInitialState(): GameState {
        return rootState.copy(players = rootState.players.map { player ->
            player.withCards(player.cards.map { playerCard ->
                if (playerCard.card == Card.UNKNOWN) {
                    playerCard.copy(card = Card.Alpha)
                } else playerCard
            })
        })
    }
}

class MCTS(private val explorationConstant: Double = sqrt(2.0)) {

    fun getBestNode(rootNode: MCTSNode, maxIterations: Int?, maxTimeMs: Int?): MCTSNode {
        require(maxIterations != null || maxTimeMs != null)

        val startTime = System.currentTimeMillis()
        var iterationCount = 0

        while ((maxIterations == null || iterationCount++ < maxIterations) &&
            (maxTimeMs == null || System.currentTimeMillis() - startTime < maxTimeMs)) {
            traverse(rootNode).run {
                backpropagate(rollout())
            }
        }

        return rootNode.children.maxBy { it.visits } ?: rootNode
    }

    private fun traverse(node: MCTSNode): MCTSNode {
        var nextNode = node

        while (nextNode.isFullyVisited) {
            nextNode = nextNode.children.maxBy {
                (it.accumulatedValue / it.visits) + (explorationConstant * sqrt(ln(nextNode.visits / it.visits.toDouble())))
            } ?: break
        }

        return nextNode.children.shuffled().firstOrNull {
            it.visits == 0
        } ?: nextNode
    }
}