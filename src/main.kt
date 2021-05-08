import models.Card
import models.Plus
import tests.Test
import tests.Testing

fun executePlaygroundTest() {
    Testing.execute(object : Test(
        arrayOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender),
        arrayOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender),
        Move(1, BOTTOM_LEFT),
        Move(0, BOTTOM_RIGHT),
        Move(2, LEFT),
        Move(3, RIGHT),
        Move(1, TOP_LEFT),
        Move(2, CENTER),
        advancedRules = listOf(Plus)
    ) {}, true)
}

fun main() {
//    executePlaygroundTest()
    Testing.executeUnitTests()
}