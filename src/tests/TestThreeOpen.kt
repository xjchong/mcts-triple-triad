package tests

import models.Card
import models.ThreeOpen

object TestThreeOpen: Test(
    arrayOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender),
    arrayOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender),
    advancedRules = listOf(ThreeOpen),
    requirement = { states ->
        states.last().players.flatMap { it.cards }.sumBy { if (it.isHidden) 0 else 1 } == 6
    }
)