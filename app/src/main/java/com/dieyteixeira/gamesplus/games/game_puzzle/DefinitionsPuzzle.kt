package com.dieyteixeira.gamesplus.games.game_puzzle

import android.R.attr.resource
import android.annotation.SuppressLint
import android.graphics.fonts.FontFamily
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import com.dieyteixeira.gamesplus.R
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import kotlin.math.abs

enum class Direction { UP, DOWN, LEFT, RIGHT }

data class PuzzleState(
    val playerColor: Color,
    val playerName: String = "",
    val isGameOver: Boolean = false
)

fun getDragDirection(dragAmount: Offset): Direction? {
    return when {
        abs(dragAmount.x) > abs(dragAmount.y) -> {
            if (dragAmount.x > 0) Direction.RIGHT else Direction.LEFT
        }
        abs(dragAmount.y) > abs(dragAmount.x) -> {
            if (dragAmount.y > 0) Direction.DOWN else Direction.UP
        }
        else -> null
    }
}

fun findTouchedBox(position: Offset, gridSize: Int, cellSize: Float): Pair<Int, Int>? {
    val x = (position.x / cellSize).toInt()
    val y = (position.y / cellSize).toInt()
    return if (x in 0 until gridSize && y in 0 until gridSize) x to y else null
}

fun List<List<Int>>.tryMove(
    direction: Direction,
    emptyPosition: Pair<Int, Int>,
    touchedBox: Pair<Int, Int>
): Pair<List<List<Int>>, Pair<Int, Int>> {
    val (emptyX, emptyY) = emptyPosition
    val (touchedX, touchedY) = touchedBox
    val newGrid = this.map { it.toMutableList() }

    return when (direction) {
        Direction.UP -> if (touchedX == emptyX && touchedY == emptyY + 1) {
            newGrid[emptyY][emptyX] = newGrid[touchedY][touchedX]
            newGrid[touchedY][touchedX] = 0
            newGrid to (touchedX to touchedY)
        } else this to emptyPosition

        Direction.DOWN -> if (touchedX == emptyX && touchedY == emptyY - 1) {
            newGrid[emptyY][emptyX] = newGrid[touchedY][touchedX]
            newGrid[touchedY][touchedX] = 0
            newGrid to (touchedX to touchedY)
        } else this to emptyPosition

        Direction.LEFT -> if (touchedY == emptyY && touchedX == emptyX + 1) {
            newGrid[emptyY][emptyX] = newGrid[touchedY][touchedX]
            newGrid[touchedY][touchedX] = 0
            newGrid to (touchedX to touchedY)
        } else this to emptyPosition

        Direction.RIGHT -> if (touchedY == emptyY && touchedX == emptyX - 1) {
            newGrid[emptyY][emptyX] = newGrid[touchedY][touchedX]
            newGrid[touchedY][touchedX] = 0
            newGrid to (touchedX to touchedY)
        } else this to emptyPosition
    }
}

fun generateGrid(gridSize: Int): List<List<Int>> {
    var grid: List<Int>
    grid = (0 until gridSize * gridSize).toMutableList()
    do {
        grid.shuffle()
    } while (!isSolvable(grid, gridSize))

    return grid.chunked(gridSize)
}

fun isSolvable(grid: List<Int>, gridSize: Int): Boolean {
    val flatGrid = grid.filter { it != 0 }
    val inversions = countInversions(flatGrid)

    return inversions % 2 == 0
}

fun countInversions(grid: List<Int>): Int {
    var inversions = 0
    for (i in grid.indices) {
        for (j in i + 1 until grid.size) {
            if (grid[i] > grid[j]) {
                inversions++
            }
        }
    }
    return inversions
}

fun findEmptyPosition(grid: List<List<Int>>): Pair<Int, Int> {
    grid.forEachIndexed { y, row ->
        row.forEachIndexed { x, number ->
            if (number == 0) return x to y
        }
    }
    throw IllegalStateException("No empty space found in the grid")
}

@Composable
fun calculateCardSizePuzzle(grid: Int): Dp {
    val screenWidth = when (grid) {
        3 -> 310.dp
        5 -> 300.dp
        else -> 295.dp
    }

    val availableWidth = screenWidth / grid

    return availableWidth
}