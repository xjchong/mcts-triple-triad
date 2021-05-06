package models

sealed class CardType(val name: String)
object Beastman : CardType("Beastman")
object Garland : CardType("Garland")
object Primal : CardType("Primal")
object Scions : CardType("Scions")