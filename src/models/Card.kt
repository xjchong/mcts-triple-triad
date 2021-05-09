package models

class Card private constructor(val name: String, val rarity: CardRarity,
                val n: Int, val e: Int, val s: Int, val w: Int,
                val type: CardType? = null) {

    companion object {
        val DUD = Card("DUD", Legendary, 1, 1, 1, 1)

        val Adamantoise = Card("Adamantoise", Uncommon, 5, 7, 4, 4)
        val Adrammelech = Card("Adrammelech", Rare, 6, 1, 7, 8)
        val Ahriman = Card("Ahriman", Common, 5, 5, 2, 2)
        val AlexanderPrime = Card("Alexander Prime", Rare, 7, 3, 2, 8, Primal)
        val Alpha = Card("Alpha", Rare, 6, 6, 6, 6)
        val AlphinaudAlisaie = Card("Alphinaud & Alisaie", Epic, 9, 3, 3, 9, Scions)
        val Amaljaa = Card("Amalj'aa", Common, 1, 4, 7, 1, Beastman)
        val Amaro = Card("Amaro", Common, 1, 7, 2, 3)
        val Ananta = Card("Ananta", Uncommon, 3, 7, 5, 2, Beastman)
        val Apkallu = Card("Apkallu", Common, 3, 4, 4, 1)
        val Archaeornis = Card("Archaeornis", Uncommon, 5, 4, 6, 5)
        val Bomb = Card("Bomb", Common, 3, 4, 3, 3)
        val Coeurl = Card("Coeurl", Common, 2, 5, 2, 5)
        val Dodo = Card("Dodo", Common, 4, 2, 3, 4)
        val Mandragora = Card("Mandragora", Common, 4, 2, 5, 3)
        val Sabotender = Card("Sabotender", Common, 4, 3, 3, 3)
    }
}