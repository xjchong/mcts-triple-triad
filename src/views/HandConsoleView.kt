package views

import models.Player
import models.Position

class HandConsoleView(private val initialPlayer: Player): ConsoleLayout(15, 29) {

    private var currentPlayer: Player = initialPlayer
    private val cardViews = listOf(
        CardConsoleView(currentPlayer.cards.getOrNull(0)),
        CardConsoleView(currentPlayer.cards.getOrNull(1)),
        CardConsoleView(currentPlayer.cards.getOrNull(2)),
        CardConsoleView(currentPlayer.cards.getOrNull(3)),
        CardConsoleView(currentPlayer.cards.getOrNull(4))
    )

    init {
        add(cardViews[0], Position.ORIGIN)
        add(cardViews[1], Position.ORIGIN.withRelativeColumn(10))
        add(cardViews[2], Position.ORIGIN.withRelativeColumn(20))
        add(cardViews[3], Position.ORIGIN.withRelative(8, 5))
        add(cardViews[4], Position.ORIGIN.withRelative(8, 15))
    }

    fun bind(player: Player) {
        for ((index, initialCard) in initialPlayer.cards.withIndex()) {
            val currentCard = player.cards.find { it.id == initialCard.id }

            cardViews[index].bind(currentCard)
        }
    }
}