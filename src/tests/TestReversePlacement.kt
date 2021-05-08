package tests

import models.Card
import models.Reverse

object TestReversePlacement: Test(
    arrayOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender),
    arrayOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender),
    Move(2, TOP_RIGHT),
    Move(1, TOP),
    Move(1, TOP_LEFT),
    Move(2, BOTTOM_LEFT),
    Move(0, LEFT),
    advancedRules = listOf(Reverse),
    requirement = { states ->
        with(states.last().board) {
            listOf(
                playerCards[TOP_LEFT].isBlue(),
                playerCards[TOP].isRed(),
                playerCards[TOP_RIGHT].isBlue(),
                playerCards[LEFT].isBlue(),
                playerCards[BOTTOM_LEFT].isBlue()
            ).all { it }
        }
    }
)