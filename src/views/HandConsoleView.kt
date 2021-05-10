package views

import models.Player
import models.Position

class HandConsoleView: ConsoleLayout(15, 29) {

    private var initialPlayer: Player? = null
    private val cardViews= listOf(
        CardConsoleView(null),
        CardConsoleView(null),
        CardConsoleView(null),
        CardConsoleView(null),
        CardConsoleView(null)
    )

    init {
        add(cardViews[0], Position.ORIGIN)
        add(cardViews[1], Position.ORIGIN.withRelativeColumn(10))
        add(cardViews[2], Position.ORIGIN.withRelativeColumn(20))
        add(cardViews[3], Position.ORIGIN.withRelative(8, 5))
        add(cardViews[4], Position.ORIGIN.withRelative(8, 15))
    }

    fun bind(player: Player) {
        if (player.cards.size == 5 || initialPlayer == null) {
            initialPlayer = player
        }

        initialPlayer?.let {
            for ((index, initialCard) in it.cards.withIndex()) {
                val currentCard = player.cards.find { card -> card.id == initialCard.id }

                cardViews[index].bind(currentCard)
            }
        }
    }
}