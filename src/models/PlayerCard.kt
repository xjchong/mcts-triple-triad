package models

import java.util.*

data class PlayerCard(val card: Card, val playerId: Int, val playableTurns: List<Int> = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8), val isHidden: Boolean = true,
                      val modifier: Int = 0, val id: UUID = UUID.randomUUID()) {

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

    fun modified(modifier: Int): PlayerCard {
        return copy(modifier = modifier)
    }
}