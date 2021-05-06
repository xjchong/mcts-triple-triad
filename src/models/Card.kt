package models

class Card private constructor(val name: String, val rarity: CardRarity,
                val n: Int, val e: Int, val s: Int, val w: Int,
                val type: CardType? = null) {

    companion object {
        val Bomb = Card("Bomb", Common, 3, 4, 3, 3)
        val Coeurl = Card("Coeurl", Common, 2, 5, 2, 5)
        val Dodo = Card("Dodo", Common, 4, 2, 3, 4)
        val Mandragora = Card("Mandragora", Common, 4, 2, 5, 3)
        val Sabotender = Card("Sabotender", Common, 4, 3, 3, 3)
    }
}