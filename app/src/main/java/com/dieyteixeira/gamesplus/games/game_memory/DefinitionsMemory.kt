package com.dieyteixeira.gamesplus.games.game_memory

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min

enum class GameMode(val displayName: String) {
    OnePlayer("1 Player"),
    TwoPlayers("2 Players")
}

data class GridSize(val rows: Int, val columns: Int)

data class GameConfig(val gridSize: GridSize, val gameMode: GameMode)

data class MemoryGameState(
    val grid: List<Int>,
    val revealed: MutableList<Boolean>,
    val matched: MutableList<Boolean>,
    var firstChoice: Int?,
    var secondChoice: Int?,
    var movesPlayer: Int,
    var currentPlayer: Int,
    val player1Name: String,
    val player2Name: String,
    val rows: Int,
    val columns: Int,
    val matchedPairs: MutableMap<Int, Int>
)

fun countPairsByPlayer(matchedPairs: MutableMap<Int, Int>, playerId: Int): Int {
    return matchedPairs.values.count { it == playerId } / 2
}

@Composable
fun calculateCardSize(rows: Int, columns: Int): Dp {
    val screenWidth = 320.dp
    val screenHeight = 600.dp

    val availableWidth = screenWidth / columns
    val availableHeight = screenHeight / rows

    return min(availableWidth, availableHeight)
}

fun generateGrid(size: GridSize): List<Int> {
    val numbers = mutableListOf<Int>()
    for (i in 1..(size.rows * size.columns / 2)) {
        numbers.add(i)
        numbers.add(i)
    }
    numbers.shuffle()
    return numbers
    Log.d("GameState", "Gerando grid com tamanho ${size.rows} x ${size.columns}")
}

fun Modifier.clickable(onClick: () -> Unit): Modifier = this.pointerInput(Unit) {
    detectTapGestures(onTap = { onClick() })
}