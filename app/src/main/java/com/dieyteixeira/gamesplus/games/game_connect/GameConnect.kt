package com.dieyteixeira.gamesplus.games.game_connect

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.dieyteixeira.gamesplus.games.game_memory.clickable
import com.dieyteixeira.gamesplus.ui.theme.Blue
import com.dieyteixeira.gamesplus.ui.theme.Green
import com.dieyteixeira.gamesplus.ui.theme.Red
import com.dieyteixeira.gamesplus.ui.theme.Yellow

@Composable
fun GameConnect(
    navigateClick: Boolean
) {
    val gridSize = 5
    val colors = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow)
    val grid = remember { List(gridSize) { List(gridSize) { colors.random() } } }
    val selectedPoints = remember { mutableStateListOf<Pair<Int, Int>>() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFEF))
            .padding(16.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        val point = getPointFromOffset(offset, gridSize)
                        if (point != null) selectedPoints.add(point)
                    },
                    onDragEnd = {
                        selectedPoints.clear()
                    },
                    onDrag = { change, _ ->
                        val point = getPointFromOffset(change.position, gridSize)
                        if (point != null && point !in selectedPoints) selectedPoints.add(point)
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            selectedPoints.windowed(2, 1) { (start, end) ->
                val startOffset = getOffsetFromPoint(start, gridSize, size)
                val endOffset = getOffsetFromPoint(end, gridSize, size)
                drawLine(
                    color = Color.Black,
                    start = startOffset,
                    end = endOffset,
                    strokeWidth = 8f
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            grid.forEachIndexed { rowIndex, row ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    row.forEachIndexed { colIndex, color ->
                        Dot(
                            color = color,
                            isSelected = (rowIndex to colIndex) in selectedPoints
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Dot(color: Color, isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(if (isSelected) Color.Gray else color)
    )
}

fun getPointFromOffset(offset: Offset, gridSize: Int): Pair<Int, Int>? {
    val x = (offset.x / (gridSize * 100)).toInt() // Ajuste baseado na escala
    val y = (offset.y / (gridSize * 100)).toInt()
    return if (x in 0 until gridSize && y in 0 until gridSize) x to y else null
}

fun getOffsetFromPoint(
    point: Pair<Int, Int>,
    gridSize: Int,
    canvasSize: Size
): Offset {
    val stepX = canvasSize.width / gridSize
    val stepY = canvasSize.height / gridSize
    return Offset(point.first * stepX + stepX / 2, point.second * stepY + stepY / 2)
}