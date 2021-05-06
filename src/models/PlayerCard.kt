package models

import java.util.*

data class PlayerCard(val card: Card, val playerId: Int, val playableTurns: List<Int>, val isHidden: Boolean = false,
                      val id: UUID = UUID.randomUUID()) {

    fun assignedToPlayer(playerId: Int): PlayerCard {
        return copy(playerId = playerId)
    }

    fun hidden(): PlayerCard {
        return copy(isHidden = true)
    }

    fun unhidden(): PlayerCard {
        return copy(isHidden = false)
    }

    fun playableOnTurns(playableTurns: List<Int>): PlayerCard {
        return copy(playableTurns = playableTurns)
    }
}