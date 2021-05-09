package tests

import models.Card
import models.Same

object TestSame: Test(
    arrayOf(Card.AlexanderPrime, Card.Coeurl, Card.Ahriman, Card.Mandragora, Card.Sabotender),
    arrayOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Adrammelech),
    Move(4, TOP_LEFT),
    Move(3, BOTTOM_LEFT),
    Move(3, TOP),
    Move(1, BOTTOM_RIGHT),
    Move(0, TOP_RIGHT),
    Move(2, CENTER),
    Move(1, BOTTOM),
    advancedRules = listOf(Same),
    requirement = { states ->
        with(states.last().board) {
            listOf(
                playerCards[TOP_LEFT].isBlue(),
                playerCards[TOP].isRed(),
                playerCards[TOP_RIGHT].isBlue(),
                playerCards[CENTER].isRed(),
                playerCards[BOTTOM_LEFT].isBlue(),
                playerCards[BOTTOM].isBlue(),
                playerCards[BOTTOM_RIGHT].isBlue()
            ).all { it }
        }
    }
)