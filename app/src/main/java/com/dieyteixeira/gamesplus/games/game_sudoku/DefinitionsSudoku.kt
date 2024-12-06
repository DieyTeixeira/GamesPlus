package com.dieyteixeira.gamesplus.games.game_sudoku

import androidx.compose.ui.graphics.Color
import com.dieyteixeira.gamesplus.games.game_memory.GameMode
import com.dieyteixeira.gamesplus.games.game_memory.GridSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SudokuState(
    val playerName: String = "",
    val playerColor: Color = Color.LightGray,
    val selectedNivel: String = "Nível 1",
    val selectedAuxilio: String = "Fácil",
    val isGameOver: Boolean = false
)

fun generateCompleteSudokuBoard(level: String): Pair<Array<Array<MutableSet<Int>>>, Array<Array<Boolean>>> {
    // Inicializa o tabuleiro 9x9 com conjuntos vazios
    val board = Array(9) { Array(9) { mutableSetOf<Int>() } }
    val boardLocked = Array(9) { Array(9) { false } }

    // Função para verificar se o número pode ser colocado na posição (row, col)
    fun isValidPlacement(board: Array<Array<MutableSet<Int>>>, row: Int, col: Int, num: Int): Boolean {
        // Verifica a linha
        for (c in 0 until 9) {
            if (board[row][c].contains(num)) return false
        }

        // Verifica a coluna
        for (r in 0 until 9) {
            if (board[r][col].contains(num)) return false
        }

        // Verifica o bloco 3x3
        val startRow = (row / 3) * 3
        val startCol = (col / 3) * 3
        for (r in startRow until startRow + 3) {
            for (c in startCol until startCol + 3) {
                if (board[r][c].contains(num)) return false
            }
        }

        return true
    }

    // Função recursiva para preencher o tabuleiro
    fun fillBoard(row: Int, col: Int): Boolean {
        if (row == 9) return true // Tabuleiro completo

        // Calcula próxima célula
        val nextRow = if (col == 8) row + 1 else row
        val nextCol = if (col == 8) 0 else col + 1

        // Tenta preencher a célula atual com um número válido
        val numbers = (1..9).shuffled() // Embaralha os números
        for (num in numbers) {
            if (isValidPlacement(board, row, col, num)) {
                board[row][col].add(num) // Coloca o número na célula
                if (fillBoard(nextRow, nextCol)) {
                    return true
                }
                board[row][col].clear() // Remove o número se não puder continuar
            }
        }
        return false // Não foi possível preencher a célula
    }

    // Função para remover números com base no nível
    fun removeNumbersForLevel(level: String) {
        val random = java.util.Random()

        // Define a porcentagem de preenchimento para cada nível
        val fillPercentage = when (level) {
            "Nível 1" -> 0.63 // 30 células em branco
            "Nível 2" -> 0.51 // 40 células em branco
            "Nível 3" -> 0.39 // 50 células em branco
            "Nível 4" -> 0.27 // 60 células em branco
            "Nível 5" -> 0.14 // 70 células em branco
            else -> 0.0
        }

        // Calcula o número de células que devem permanecer preenchidas
        val totalCells = 81
        val cellsToKeep = (totalCells * fillPercentage).toInt()

        // Lista todas as células do tabuleiro
        val allCells = mutableListOf<Pair<Int, Int>>()
        for (row in 0 until 9) {
            for (col in 0 until 9) {
                allCells.add(row to col)
            }
        }

        // Embaralha as células
        allCells.shuffle(random)

        // Escolhe as células que devem permanecer preenchidas
        val cellsToKeepSet = allCells.take(cellsToKeep).toSet()

        // Remove números das células que não estão na lista de células a manter
        for (row in 0 until 9) {
            for (col in 0 until 9) {
                if (row to col !in cellsToKeepSet) {
                    board[row][col].clear() // Remove o número da célula
                } else {
                    boardLocked[row][col] = true // Marca a célula como bloqueada
                }
            }
        }
    }

    // Preenche o tabuleiro completamente
    fillBoard(0, 0)

    // Remove números com base no nível
    removeNumbersForLevel(level)

    // Retorna o tabuleiro e o estado bloqueado
    return Pair(board, boardLocked)
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