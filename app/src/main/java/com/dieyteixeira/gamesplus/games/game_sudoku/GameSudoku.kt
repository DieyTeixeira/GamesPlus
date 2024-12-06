package com.dieyteixeira.gamesplus.games.game_sudoku

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dieyteixeira.gamesplus.R
import com.dieyteixeira.gamesplus.games.game_memory.GameConfig
import com.dieyteixeira.gamesplus.games.game_memory.SettingsMemory
import com.dieyteixeira.gamesplus.games.game_snake.ShowReturnSettingsSnake
import com.dieyteixeira.gamesplus.ui.theme.Blue
import com.dieyteixeira.gamesplus.ui.theme.DarkBlue
import com.dieyteixeira.gamesplus.ui.theme.DarkYellow
import com.dieyteixeira.gamesplus.ui.theme.Green
import com.dieyteixeira.gamesplus.ui.theme.Red
import kotlin.collections.get
import kotlin.collections.remove

@Composable
fun GameSudoku(
    navigateClick: Boolean
) {
    val color = DarkBlue

    var playerName by remember { mutableStateOf("") }
    var playerColor by remember { mutableStateOf(Color.LightGray) }
    val difficulty = remember { mutableStateOf("Fácil") }
    val level = remember { mutableStateOf("Nível 1") }
    var showSettings by remember { mutableStateOf(true) }
    var showReturnSettingsSudoku by remember { mutableStateOf(false) }

    val onStartGame: (SudokuState) -> Unit = { config ->
        showSettings = false
    }

    if (showReturnSettingsSudoku) {
        ShowReturnSettingsSudoku(
            onNo = { showReturnSettingsSudoku = false },
            onYes = {
                showReturnSettingsSudoku = false
                showSettings = true
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (showSettings) {
                SettingsSudoku(
                    color = color,
                    playerName = playerName,
                    playerColor = playerColor,
                    onPlayerNameChange = { playerName = it },
                    onPlayerColorChange = { playerColor = it },
                    level = level.toString(),
                    onLevelChange = { level.value = it },
                    difficulty = difficulty.toString(),
                    onDifficultyChange = { difficulty.value = it },
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
                            .clickable { showReturnSettingsSudoku = true }
                    )
                }
                InformationSudoku(
                    color = playerColor,
                    playerName = playerName,
                    nivel = level.value,
                    dificuldade = difficulty.value
                )
                Spacer(modifier = Modifier.size(5.dp))
                SudokuBoard(
                    colorPlayer = playerColor,
                    level = level.value,
                    difficulty = difficulty.value
                )
            }
        }
    }
}

@Composable
fun SudokuBoard(
    colorPlayer: Color,
    level: String,
    difficulty: String
) {

    val boardState = remember { mutableStateOf(Array(9) { Array(9) { mutableSetOf<Int>() } }) }
    val boardLocked = remember { mutableStateOf(Array(9) { Array(9) { false } }) }
    val selectedNumber = remember { mutableStateOf<Int?>(null) }

    val highlightedCells = remember { mutableStateOf(setOf<Pair<Int, Int>>()) }
    val duplicates = if (difficulty == "Fácil") findDuplicates(boardState.value) else emptySet()
    val correctRows = if (difficulty == "Fácil") (0..8).filter { isRowValid(boardState.value, it) } else emptyList()
    val correctCols = if (difficulty == "Fácil") (0..8).filter { isColumnValid(boardState.value, it) } else emptyList()
    val correctBoxes = mutableSetOf<Pair<Int, Int>>()

    if (difficulty == "Fácil") {
        for (boxRow in 0 until 3) {
            for (boxCol in 0 until 3) {
                if (isBoxValid(boardState.value, boxRow, boxCol)) {
                    correctBoxes.add(boxRow to boxCol)
                }
            }
        }
    }

    LaunchedEffect(level) {
        val (generatedBoard, lockedCells) = generateCompleteSudokuBoard(level)
        boardState.value = generatedBoard
        boardLocked.value = lockedCells
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Black, RoundedCornerShape(20.dp))
                .background(Color.White, RoundedCornerShape(20.dp))
                .padding(3.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (row in 0..8) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (col in 0..8) {
                        val isHighlighted =
                            difficulty != "Difícil" && highlightedCells.value.contains(row to col)
                        val isDuplicate = difficulty == "Fácil" && duplicates.contains(row to col)
                        val isCorrect =
                            difficulty == "Fácil" && (correctRows.contains(row) || correctCols.contains(
                                col
                            ) || correctBoxes.contains(row / 3 to col / 3))

                        SudokuCell(
                            colorPlayer = colorPlayer,
                            value = boardState.value[row][col],
                            row = row,
                            col = col,
                            isHighlighted = isHighlighted,
                            isDuplicate = isDuplicate,
                            isCorrect = isCorrect,
                            isLocked = boardLocked.value[row][col],
                            onClick = {
                                selectedNumber.value?.let { number ->
                                    val cell = boardState.value[row][col]
                                    val locked = boardLocked.value[row][col]
                                    if (!locked) {
                                        if (cell.contains(number)) {
                                            cell.remove(number)
                                        } else {
                                            cell.add(number)
                                        }
                                    }
                                    highlightedCells.value = highlightOccurrences(boardState.value, number)
                                }
                            }
                        )

                        // Linha vertical mais grossa entre blocos
                        if (col % 3 == 2 && col != 8) {
                            Spacer(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(39.5.dp)
                                    .background(Color.Black)
                            )
                        }
                    }
                }

                // Linha horizontal mais grossa entre blocos
                if (row % 3 == 2 && row != 8) {
                    Spacer(
                        modifier = Modifier
                            .height(2.dp)
                            .fillMaxWidth()
                            .background(Color.Black)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        NumberSelector(
            colorPlayer = colorPlayer,
            selectedNumber = selectedNumber,
            boardState = boardState.value,
            difficulty = difficulty,
            onNumberClick = { number ->
                highlightedCells.value = highlightOccurrences(boardState.value, number)
            }
        )
    }
}