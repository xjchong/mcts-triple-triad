package models

class Board private constructor(val playerCards: Map<Position, PlayerCard?>) {

    fun setCard(playerCard: PlayerCard, position: Position): Board? {
        if (!playerCards.containsKey(position)) return null // Card doesn't exist at that position.

        return Board(playerCards.mapValues { (position, currentPlayerCard) ->
            if (position == position) playerCard else currentPlayerCard
        })
    }

    companion object {
        fun standardInstance(): Board {
            return Board(mutableMapOf(
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