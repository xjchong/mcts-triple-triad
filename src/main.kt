import models.*
import tests.Test
import tests.TestSame
import tests.Testing

fun executePlaygroundTest() {
    Testing.execute(object : Test(
        arrayOf(Card.AlexanderPrime, Card.Ahriman, Card.DUD, Card.Mandragora, Card.Sabotender),
        arrayOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Adrammelech),
        Move(0, TOP_LEFT),
        Move(0, BOTTOM_LEFT),
        Move(0, TOP),
        Move(0, BOTTOM_RIGHT),
        Move(0, RIGHT),
        Move(0, LEFT),
        Move(0, CENTER),
        Move(0, TOP_RIGHT),
        Move(0, BOTTOM),
        advancedRules = listOf(Same, Order)
    ) {}, true)
}

fun main() {
//    executePlaygroundTest()
    Testing.execute(TestSameCombo, true)
//    Testing.executeUnitTests()
}