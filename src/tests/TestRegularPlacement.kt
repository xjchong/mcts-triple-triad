package tests

import models.AllOpen
import models.Card

object TestRegularPlacement: Test(
    arrayOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender),
    arrayOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender),
    Move(4, LEFT),
    Move(2, CENTER),
    advancedRules = listOf(AllOpen)
)