package models

import java.util.*

data class Card(val name: String, val rarity: CardRarity,
                val w: Int, val n: Int, val s: Int, val e: Int,
                val type: CardType? = null) {
    val uuid: UUID = UUID.randomUUID()

    companion object {
        val Bomb = Card("Bomb", Common, 4, 3, 3, 3)
        val Coerl = Card("Coerl", Common, 5, 2, 2, 5)
        val Dodo = Card("Dodo", Common, 4, 4, 3, 2)
        val Mandragora = Card("Mandragora", Common, 4, 2, 5, 3)
        val Sabotender = Card("Sabotender", Common, 3, 4, 3, 3)
    }
}