package models

sealed class CardRarity(value: Int)
object Common : CardRarity(1)
object Uncommon : CardRarity(2)
object Rare : CardRarity(3)
object Epic : CardRarity(4)
object Legendary : CardRarity(5)