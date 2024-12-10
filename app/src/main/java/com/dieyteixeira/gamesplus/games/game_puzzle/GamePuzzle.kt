package com.dieyteixeira.gamesplus.games.game_puzzle

import android.icu.text.ListFormatter
import android.icu.text.ListFormatter.Width
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dieyteixeira.gamesplus.R
import com.dieyteixeira.gamesplus.games.game_memory.GameConfig
import com.dieyteixeira.gamesplus.games.game_memory.calculateCardSize
import com.dieyteixeira.gamesplus.games.game_snake.ScoreSnake
import com.dieyteixeira.gamesplus.games.game_sudoku.SettingsSudoku
import com.dieyteixeira.gamesplus.games.game_sudoku.ShowReturnSettingsSudoku
import com.dieyteixeira.gamesplus.games.game_sudoku.SudokuState
import com.dieyteixeira.gamesplus.ui.theme.DarkBlue

@Composable
fun GamePuzzle(
    navigateClick: Boolean
) {
    val color = DarkBlue

    var playerName by remember { mutableStateOf("") }
    var playerColor by remember { mutableStateOf(Color.LightGray) }
    var gridSize by remember { mutableStateOf(3) }
    var grid by remember { mutableStateOf(generateGrid(gridSize)) }
    var emptyPosition by remember { mutableStateOf(findEmptyPosition(grid)) }
    var showSettings by remember { mutableStateOf(true) }
    var showReturnSettingsPuzzle by remember { mutableStateOf(false) }

    val onStartGame: (PuzzleState) -> Unit = { config ->
        showSettings = false
    }

    if (showReturnSettingsPuzzle) {
        ShowReturnSettingsPuzzle(
            onNo = { showReturnSettingsPuzzle = false },
            onYes = {
                showReturnSettingsPuzzle = false
                showSettings = true
            }
        )
    }

    LaunchedEffect(gridSize) {
        grid = generateGrid(gridSize)
        emptyPosition = findEmptyPosition(grid)
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (showSettings) {
            SettingsPuzzle(
                color = color,
                playerName = playerName,
                playerColor = playerColor,
                onPlayerNameChange = { playerName = it },
                onPlayerColorChange = { playerColor = it },
                level = gridSize,
                onLevelChange = { selectedLevel -> gridSize = selectedLevel },
                onStartGame = onStartGame
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 20.dp, 10.dp, 10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_new_game),
                    contentDescription = "Novo Jogo",
                    colorFilter = ColorFilter.tint(Color.Gray),
                    modifier = Modifier
                        .size(23.dp)
                        .clickable { showReturnSettingsPuzzle = true }
                )
            }
            ScorePuzzle(
                levelAtual = gridSize,
                playerColor = playerColor
            )
            Spacer(modifier = Modifier.size(2.dp))
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(20.dp)
                    .background(
                        Color.LightGray.copy(alpha = 0.5f),
                        RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragEnd = { },
                                onDragCancel = { },
                                onDrag = { change, dragAmount ->
                                    val direction = getDragDirection(dragAmount)
                                    if (direction != null) {
                                        val touchedBox =
                                            findTouchedBox(change.position, grid.size, size.width / 3f)
                                        if (touchedBox != null) {
                                            val (newGrid, newEmptyPosition) = grid.tryMove(
                                                direction,
                                                emptyPosition,
                                                touchedBox
                                            )
                                            grid = newGrid
                                            emptyPosition = newEmptyPosition
                                        }
                                    }
                                }
                            )
                        }
                ) {
                    PuzzleGrid(
                        grid = grid,
                        gridSize = gridSize,
                        playerColor = playerColor
                    )
                }
            }
        }
    }
}

@Composable
fun PuzzleGrid(
    grid: List<List<Int>>,
    gridSize: Int,
    playerColor: Color
) {
    val puzzleSize = calculateCardSizePuzzle(gridSize)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        grid.forEachIndexed { y, row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                row.forEachIndexed { x, number ->
                    if (number != 0) {
                        GridCell(
                            number = number,
                            gridSize = gridSize,
                            cellSize = puzzleSize,
                            playerColor = playerColor
                        )
                    } else {
                        Spacer(
                            modifier = Modifier.size(puzzleSize)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GridCell(
    number: Int,
    gridSize: Int,
    cellSize: Dp,
    playerColor: Color
) {
    val fontSize = with(LocalDensity.current) {
        when (gridSize) {
            3 -> (cellSize.toPx() * 0.75f).toSp()
            5 -> (cellSize.toPx() * 0.70f).toSp()
            else -> (cellSize.toPx() * 0.60f).toSp()
        }
    }

    Box(
        modifier = Modifier
            .size(cellSize)
            .background(
                color = playerColor,
                shape = RoundedCornerShape(
                    if (gridSize == 7) 10.dp else 16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            color = Color.White,
            style = MaterialTheme.typography.titleLarge.copy(fontSize = fontSize)
        )
    }
}