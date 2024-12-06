package com.dieyteixeira.gamesplus.games.game_sudoku

import android.R.attr.maxWidth
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dieyteixeira.gamesplus.ui.theme.Blue
import com.dieyteixeira.gamesplus.ui.theme.DarkYellow
import com.dieyteixeira.gamesplus.ui.theme.Green
import com.dieyteixeira.gamesplus.ui.theme.Red

@Composable
fun GameSudoku(
    navigateClick: Boolean
) {
    val boardState = remember { mutableStateOf(Array(9) { Array(9) { mutableSetOf<Int>() } }) }
    val selectedNumber = remember { mutableStateOf<Int?>(null) }
    val highlightedCells = remember { mutableStateOf(setOf<Pair<Int, Int>>()) }
    val difficulty = remember { mutableStateOf("Fácil") } // "Fácil", "Médio", "Difícil"
    val level = remember { mutableStateOf("Nível 1") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LevelSelector(level = level)

        DifficultySelector(difficulty = difficulty)

        LaunchedEffect(level.value) {
            boardState.value = generateSudokuBoard(level.value)
        }

        SudokuBoard(
            board = boardState.value,
            difficulty = difficulty.value,
            highlightedCells = highlightedCells.value,
            onCellClick = { row, col ->
                selectedNumber.value?.let { number ->
                    val cell = boardState.value[row][col]
                    if (cell.contains(number)) {
                        cell.remove(number)
                    } else {
                        cell.add(number)
                    }
                    highlightedCells.value = highlightOccurrences(boardState.value, number)
                }
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        NumberSelector(
            selectedNumber = selectedNumber,
            boardState = boardState.value,
            difficulty = difficulty.value,
            onNumberClick = { number ->
                highlightedCells.value = highlightOccurrences(boardState.value, number)
            }
        )
    }
}

@Composable
fun SudokuBoard(
    board: Array<Array<MutableSet<Int>>>,
    difficulty: String,
    highlightedCells: Set<Pair<Int, Int>>,
    onCellClick: (Int, Int) -> Unit
) {

    val duplicates = if (difficulty == "Fácil") findDuplicates(board) else emptySet()
    val correctRows = if (difficulty == "Fácil") (0..8).filter { isRowValid(board, it) } else emptyList()
    val correctCols = if (difficulty == "Fácil") (0..8).filter { isColumnValid(board, it) } else emptyList()
    val correctBoxes = mutableSetOf<Pair<Int, Int>>()

    if (difficulty == "Fácil") {
        for (boxRow in 0 until 3) {
            for (boxCol in 0 until 3) {
                if (isBoxValid(board, boxRow, boxCol)) {
                    correctBoxes.add(boxRow to boxCol)
                }
            }
        }
    }

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
                    val isRowCorrect = correctRows.contains(row)
                    val isColCorrect = correctCols.contains(col)
                    val isBoxCorrect = correctBoxes.contains(row / 3 to col / 3)

                    SudokuCell(
                        value = board[row][col],
                        row = row,
                        col = col,
                        isHighlighted = highlightedCells.contains(row to col),
                        isDuplicate = difficulty == "Fácil" && duplicates.contains(row to col),
                        isCorrect = difficulty == "Fácil" && (correctRows.contains(row) || correctCols.contains(col) || correctBoxes.contains(row / 3 to col / 3)),
                        onClick = { onCellClick(row, col) }
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
}

@Composable
fun SudokuCell(
    value: Set<Int>,
    row: Int,
    col: Int,
    isHighlighted: Boolean,
    isDuplicate: Boolean,
    isCorrect: Boolean,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(if (value.size == 1) 33.dp else 36.dp)
                        .background(
                            color = when {
                                value.isEmpty() -> Color.Transparent
                                isHighlighted && isCorrect -> Green.copy(alpha = 0.8f)
                                isCorrect -> Green.copy(alpha = 0.2f)
                                isDuplicate -> Red
                                isHighlighted -> DarkYellow.copy(alpha = 0.8f)
                                else -> DarkYellow.copy(alpha = 0.2f)
                            },
                            shape = when {
                                value.size == 1 -> RoundedCornerShape(100)
                                else -> RoundedCornerShape(20)
                            }
                        )
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onClick() },
                    contentAlignment = Alignment.Center
                ) {}
                Box(
                    modifier = Modifier
                        .height(38.dp)
                        .width(35.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onClick() },
                    contentAlignment = Alignment.Center
                ) {
                    if (value.isNotEmpty()) {
                        if (value.size == 1) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = value.first().toString(),
                                    fontSize = 16.sp,
                                    color = if (isHighlighted || isDuplicate) Color.White else Color.Black
                                )
                            }
                        } else {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                for (i in 0..2) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        for (j in 0..2) {
                                            val numberIndex = i * 3 + j
                                            val number = value.sorted().getOrNull(numberIndex)
                                            if (number != null) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(12.dp),
                                                    contentAlignment = Alignment.TopCenter
                                                ) {
                                                    Text(
                                                        text = number.toString(),
                                                        fontSize = 8.sp,
                                                        color = if (isHighlighted || isDuplicate) Color.White else Color.Black
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Linha vertical mais grossa entre blocos
            if (col >= 0 && col != 2 && col != 5 && col != 8) {
                Spacer(
                    modifier = Modifier
                        .width(1.dp)
                        .height(30.dp)
                        .background(Color.LightGray)
                )
            }
        }

        // Linha horizontal mais grossa entre blocos
        if (row >= 0 && row != 2 && row != 5 && row != 8) {
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .width(30.dp)
                    .background(Color.LightGray)
            )
        }
    }
}

@Composable
fun NumberSelector(
    selectedNumber: MutableState<Int?>,
    boardState: Array<Array<MutableSet<Int>>>,
    difficulty: String,
    onNumberClick: (Int) -> Unit
) {
    val numberCounts = if (difficulty != "Difícil") countOccurrences(boardState) else emptyMap()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp), // Espaço entre linhas
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Primeira linha com 5 números (1 a 5)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            (1..5).forEach { number ->
                NumberBox(number, selectedNumber, numberCounts, onNumberClick)
                if (number < 5) {
                    Spacer(modifier = Modifier.width(5.dp))
                }
            }
        }

        // Segunda linha com 4 números (6 a 9)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            (6..9).forEach { number ->
                NumberBox(number, selectedNumber, numberCounts, onNumberClick)
                if (number < 9) {
                    Spacer(modifier = Modifier.width(5.dp))
                }
            }
        }
    }
}

@Composable
fun NumberBox(
    number: Int,
    selectedNumber: MutableState<Int?>,
    numberCounts: Map<Int, Int>,
    onNumberClick: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .width(60.dp)
            .height(70.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                selectedNumber.value = number
                onNumberClick(number)
            }
            .background(
                color = if (selectedNumber.value == number) DarkYellow else Color.LightGray,
                shape = RoundedCornerShape(10.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = number.toString(),
                fontSize = 22.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = numberCounts[number]?.toString() ?: "0",
                fontSize = 16.sp,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
fun LevelSelector(
    level: MutableState<String>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        listOf("Nível 1", "Nível 2", "Nível 3", "Nível 4", "Nível 5").forEach { lvl ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (level.value == lvl) Blue else Color.LightGray)
                    .clickable { level.value = lvl }
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = lvl,
                    fontSize = 16.sp,
                    color = if (level.value == lvl) Color.White else Color.Black
                )
            }
        }
    }
}

@Composable
fun DifficultySelector(
    difficulty: MutableState<String>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        listOf("Fácil", "Médio", "Difícil").forEach { level ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (difficulty.value == level) Blue else Color.LightGray)
                    .clickable { difficulty.value = level }
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = level,
                    fontSize = 16.sp,
                    color = if (difficulty.value == level) Color.White else Color.Black
                )
            }
        }
    }
}

fun generateSudokuBoard(level: String): Array<Array<MutableSet<Int>>> {
    val board = Array(9) { Array(9) { mutableSetOf<Int>() } }
    val random = java.util.Random()

    val fillPercentage = when (level) {
        "Nível 1" -> 0.80 // 35 células em branco
        "Nível 2" -> 0.50 // 44 células em branco
        "Nível 3" -> 0.35 // 53 células em branco
        "Nível 4" -> 0.25 // 61 células em branco
        "Nível 5" -> 0.18 // 67 células em branco
        else -> 0.0
    }

    val totalCells = 81
    val cellsToFill = (totalCells * fillPercentage).toInt()

    fun isValidPlacement(board: Array<Array<MutableSet<Int>>>, row: Int, col: Int, number: Int): Boolean {
        if (board[row].any { it.contains(number) }) return false
        if (board.any { it[col].contains(number) }) return false
        val startRow = (row / 3) * 3
        val startCol = (col / 3) * 3
        for (r in startRow until startRow + 3) {
            for (c in startCol until startCol + 3) {
                if (board[r][c].contains(number)) return false
            }
        }

        return true
    }

    var attemptsOverall = 0
    repeat(cellsToFill) {
        var row: Int
        var col: Int
        var number: Int
        var isValid: Boolean
        var attempts = 0

        while (attempts < 10) {
            row = random.nextInt(9)
            col = random.nextInt(9)

            if (board[row][col].isNotEmpty()) continue
            number = random.nextInt(9) + 1
            isValid = isValidPlacement(board, row, col, number)
            if (isValid) {
                board[row][col].add(number)
                break
            }

            attempts++
        }

        attemptsOverall++
        if (attemptsOverall > 50) {
            return board
        }
    }

    return board
}

fun highlightOccurrences(board: Array<Array<MutableSet<Int>>>, number: Int): Set<Pair<Int, Int>> {
    val highlightedCells = mutableSetOf<Pair<Int, Int>>()
    for (row in board.indices) {
        for (col in board[row].indices) {
            if (board[row][col].contains(number)) {
                highlightedCells.add(row to col)
            }
        }
    }
    return highlightedCells
}

fun countOccurrences(board: Array<Array<MutableSet<Int>>>): Map<Int, Int> {
    val counts = mutableMapOf<Int, Int>()
    for (row in board) {
        for (cell in row) {
            for (number in cell) {
                counts[number] = counts.getOrDefault(number, 0) + 1
            }
        }
    }
    return counts
}

fun findDuplicates(board: Array<Array<MutableSet<Int>>>): Set<Pair<Int, Int>> {
    val duplicates = mutableSetOf<Pair<Int, Int>>()

    // Verificar duplicados em linhas
    for (row in board.indices) {
        val seen = mutableMapOf<Int, MutableList<Int>>()
        for (col in board[row].indices) {
            for (number in board[row][col]) {
                seen[number]?.add(col) ?: seen.put(number, mutableListOf(col))
            }
        }
        seen.values.filter { it.size > 1 }.forEach { cols ->
            cols.forEach { col -> duplicates.add(row to col) }
        }
    }

    // Verificar duplicados em colunas
    for (col in board[0].indices) {
        val seen = mutableMapOf<Int, MutableList<Int>>()
        for (row in board.indices) {
            for (number in board[row][col]) {
                seen[number]?.add(row) ?: seen.put(number, mutableListOf(row))
            }
        }
        seen.values.filter { it.size > 1 }.forEach { rows ->
            rows.forEach { row -> duplicates.add(row to col) }
        }
    }

    // Verificar duplicados em quadrantes
    for (boxRow in 0 until 3) {
        for (boxCol in 0 until 3) {
            val seen = mutableMapOf<Int, MutableList<Pair<Int, Int>>>()
            for (row in 0 until 3) {
                for (col in 0 until 3) {
                    val currentRow = boxRow * 3 + row
                    val currentCol = boxCol * 3 + col
                    for (number in board[currentRow][currentCol]) {
                        seen[number]?.add(currentRow to currentCol) ?: seen.put(
                            number,
                            mutableListOf(currentRow to currentCol)
                        )
                    }
                }
            }
            seen.values.filter { it.size > 1 }.forEach { cells ->
                duplicates.addAll(cells)
            }
        }
    }

    return duplicates
}

fun isRowValid(board: Array<Array<MutableSet<Int>>>, row: Int): Boolean {
    val seen = mutableSetOf<Int>()
    for (col in board[row].indices) {
        val cell = board[row][col]
        for (number in cell) {
            if (number in seen) return false
            seen.add(number)
        }
    }
    return seen.size == 9 && (1..9).all { it in seen }
}

fun isColumnValid(board: Array<Array<MutableSet<Int>>>, col: Int): Boolean {
    val seen = mutableSetOf<Int>()
    for (row in board.indices) {
        val cell = board[row][col]
        for (number in cell) {
            if (number in seen) return false
            seen.add(number)
        }
    }
    return seen.size == 9 && (1..9).all { it in seen }
}

fun isBoxValid(board: Array<Array<MutableSet<Int>>>, boxRow: Int, boxCol: Int): Boolean {
    val seen = mutableSetOf<Int>()
    for (row in 0 until 3) {
        for (col in 0 until 3) {
            val currentRow = boxRow * 3 + row
            val currentCol = boxCol * 3 + col
            val cell = board[currentRow][currentCol]
            for (number in cell) {
                if (number in seen) return false
                seen.add(number)
            }
        }
    }
    return seen.size == 9 && (1..9).all { it in seen }
}