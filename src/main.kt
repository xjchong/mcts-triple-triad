import models.Card
import models.Player
import views.GameConsoleView

fun main() {
    val players = listOf(
        Player(0, setOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender)),
        Player(1, setOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender))
    )

    val gameEngine = GameEngine()
    val initialState = gameEngine.initialize(players)
    val gameConsoleView = GameConsoleView(initialState)

    gameConsoleView.bind(initialState)
    gameConsoleView.draw()
}