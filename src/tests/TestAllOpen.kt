package tests

import models.AllOpen
import models.Card
import models.ThreeOpen

object TestAllOpen: Test(
    arrayOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender),
    arrayOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender),
    advancedRules = listOf(AllOpen, ThreeOpen),
    requirement = { states ->
        states.last().players.flatMap { it.cards }.all { !it.isHidden }
    }
)