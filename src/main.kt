import models.Card
import models.Player
import models.Position
import views.GameConsoleView

fun main() {
    var players = listOf(
        Player(0, setOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender)),
        Player(1, setOf(Card.Bomb, Card.Coeurl, Card.Dodo, Card.Mandragora, Card.Sabotender))
    )

    val gameEngine = GameEngine()
    val initialState = gameEngine.initialize(players)
    val gameConsoleView = GameConsoleView(initialState)

    players = initialState.players

    val nextState = gameEngine.makeMove(
        players.first(), players.first().cards[4], Position(1, 1)
    )

    gameConsoleView.bind(nextState)
    gameConsoleView.draw()
}