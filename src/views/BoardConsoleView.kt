package views

import models.Board
import models.Position

class BoardConsoleView(private var board: Board): ConsoleLayout(HEIGHT, WIDTH) {

    private val cardViews: Map<Position, CardConsoleView>

    init {
        add(BoardFrameConsoleView(), Position(0, 0))

        cardViews = board.playerCards.keys.associateWith { CardConsoleView(board.playerCards[it]) }

        cardViews.forEach { (position, cardView) ->
            add(cardView, position.withRelative(
                position.row * CardConsoleView.HEIGHT + 1,
                position.column * CardConsoleView.WIDTH + 1
            ))
        }
    }

    fun bind(board: Board) {
        this.board = board

        cardViews.forEach { (position, cardView) ->
            cardView.bind(board.playerCards[position])
        }
    }

    inner class BoardFrameConsoleView(): ConsoleView(HEIGHT, WIDTH) {
        override fun getString(): String {
            return BOARD_STRING
        }
    }

    companion object {
        const val HEIGHT = 31
        const val WIDTH = 31
        private const val BOARD_STRING =
            "o=========o=========o=========o" +
            "|         |         |         |" +
            "|         |         |         |" +
            "|         |         |         |" +
            "|         |         |         |" +
            "|         |         |         |" +
            "|         |         |         |" +
            "|         |         |         |" +
            "o=========o=========o=========o" +
            "|         |         |         |" +
            "|         |         |         |" +
            "|         |         |         |" +
            "|         |         |         |" +
            "|         |         |         |" +
            "|         |         |         |" +
            "|         |         |         |" +
            "o=========o=========o=========o" +
            "|         |         |         |" +
            "|         |         |         |" +
            "|         |         |         |" +
            "|         |         |         |" +
            "|         |         |         |" +
            "|         |         |         |" +
            "|         |         |         |" +
            "o=========o=========o=========o"
    }
}