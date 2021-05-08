package tests

import models.Card
import models.Same

object TestSame: Test(
    arrayOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender),
    arrayOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender),
    Move(0, CENTER),
    Move(0, BOTTOM_LEFT),
    Move(3, LEFT),
    Move(2, BOTTOM),
    Move(1, BOTTOM_RIGHT),
    Move(1, RIGHT),
    advancedRules = listOf(Same),
    requirement = { states ->
        with(states.last().board) {
            listOf(
                playerCards[LEFT].isBlue(),
                playerCards[CENTER].isRed(),
                playerCards[RIGHT].isRed(),
                playerCards[BOTTOM_LEFT].isBlue(),
                playerCards[BOTTOM].isBlue(),
                playerCards[BOTTOM_RIGHT].isBlue()
            ).all { it }
        }
    }
)