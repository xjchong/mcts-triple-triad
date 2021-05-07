package views

import models.Board

class BoardConsoleView(): ConsoleView(HEIGHT, WIDTH) {

    override fun getBitString(): String {
        return BOARD_STRING
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