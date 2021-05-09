package tests

import models.AllOpen
import models.Card
import models.Move

object TestBasicPlacement: Test(
    arrayOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender),
    arrayOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender),
    Move(4, LEFT),
    Move(2, CENTER),
    advancedRules = listOf(AllOpen),
    requirement = { states ->
        with(states.last().board) {
            playerCards[LEFT].isRed() && playerCards[CENTER].isRed()
        }
    }
)