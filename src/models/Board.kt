package models

class Board private constructor(val rows: Int, val columns: Int, val playerCards: Map<Position, PlayerCard?> = mapOf()) {

    fun setCard(playerCard: PlayerCard, position: Position): Board? {
        if (!playerCards.containsKey(position)) return null // Position doesn't exist.

        return Board(rows, columns, playerCards.mapValues { (position, currentPlayerCard) ->
            if (position == position) playerCard else currentPlayerCard
        })
    }

    companion object {
        fun standardInstance(): Board {
            return Board(3, 3, mutableMapOf(
                Position(0, 0) to null,
                Position(0, 1) to null,
                Position(0, 2) to null,
                Position(1, 0) to null,
                Position(1, 1) to null,
                Position(1, 2) to null,
                Position(2, 0) to null,
                Position(2, 1) to null,
                Position(2, 2) to null
            ))
        }
    }
}