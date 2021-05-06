package models

data class GameState(
    val board: Board,
    val players: List<Player>, // The order of player implicitly indicates the starting player and turn order.
    val advancedRules: List<AdvancedRule> = listOf()) {

    fun getTurn(): Int {
        // The first turn is 0.
        return board.playerCards.values.sumBy { if (it != null) 1 else 0 }
    }

    fun nextPlayer(): Player {
        return players[getTurn() % players.size]
    }
}