import models.Card
import models.Plus
import models.Same
import tests.Test
import tests.TestSame
import tests.Testing

fun executePlaygroundTest() {
    Testing.execute(object : Test(
        arrayOf(Card.AlexanderPrime, Card.Ahriman, Card.DUD, Card.Mandragora, Card.Sabotender),
        arrayOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Adrammelech),
        Move(4, TOP_LEFT),
        Move(3, BOTTOM_LEFT),
        Move(3, TOP),
        Move(1, BOTTOM_RIGHT),
        Move(0, RIGHT),
        Move(1, LEFT),
        Move(1, CENTER),
        Move(1, TOP_RIGHT),
        Move(0, BOTTOM),
        advancedRules = listOf(Same)
    ) {}, true)
}

fun main() {
    executePlaygroundTest()
//    Testing.execute(TestSame, true)
//    Testing.executeUnitTests()
}