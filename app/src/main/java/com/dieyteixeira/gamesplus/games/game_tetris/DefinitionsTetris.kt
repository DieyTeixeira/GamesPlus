package com.dieyteixeira.gamesplus.games.game_tetris

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.dieyteixeira.gamesplus.ui.theme.GreenComp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import java.util.Random

enum class Move {
    Left, Right, Drop
}

data class TetrisState(
    val playerName: String = "",
    val playerColor: Color = Color.LightGray,
    val iniciar: Boolean = false,
    val board: Array<Array<Color>>,
    val currentTetromino: Tetromino,
    val position: Pair<Int, Int>,
    val score: Int = 0,
    val speed: Long = 500L,
    val isPaused: Boolean = false,
    val isGameOver: Boolean = false
)

data class Tetromino(val shape: List<Pair<Int, Int>>, val color: Color)

class TetrisGame(
    private val scope: CoroutineScope,
    context: Context
) {

    private val mutex = Mutex()
    private val mutableState = MutableStateFlow(
        TetrisState(board = Array(20) { Array(10) { GreenComp.copy(alpha = 0.05f) } },
            currentTetromino = Tetromino(listOf(Pair(0, 0), Pair(1, 0), Pair(2, 0), Pair(3, 0)), DarkGreen1),
            position = Pair(0, 5))
    )

    val state: Flow<TetrisState> = mutableState

    private var gameJob: Job? = null
    private var speed: Long = 500L

    private fun startGame() {
        gameJob?.cancel()

        gameJob = scope.launch {
            while (true) {
                delay(speed)
                mutableState.update {

                    if (it.isPaused) {
                        return@update it
                    }

                    if (it.isGameOver) {
                        return@update it
                    }

                    val newPosition = Pair(it.position.first + 1, it.position.second)

                    // Verifique a colisão com o fundo ou peças fixadas
                    val collision = checkCollision(it.currentTetromino, newPosition, it.board)
                    if (!collision) {
                        // Atualize a posição
                        it.copy(position = newPosition)
                    } else {
                        // Fixe a peça atual no tabuleiro
                        val updatedBoard = placeTetromino(it.board, it.currentTetromino, it.position)
                        if (checkCollision(it.currentTetromino, Pair(0, 5), updatedBoard)) {
                            return@update it.copy(isGameOver = true) // Fim do jogo
                        }
                        val newTetromino = generateNewTetromino()
                        val (clearedBoard, linesCleared) = clearFullLines(updatedBoard)

                        // Atualize o estado e gere a nova peça
                        it.copy(
                            board = clearedBoard,
                            currentTetromino = newTetromino,
                            position = Pair(0, 5), // A nova peça começa no topo
                            score = it.score + (linesCleared * 5)
                        )
                    }
                }
            }
        }
    }

    fun resetGameTetris() {
        mutableState.update {
            TetrisState(board = Array(20) { Array(10) { GreenComp.copy(alpha = 0.05f) } },
                currentTetromino = generateNewTetromino(), position = Pair(0, 5))
        }
        speed = 500L
        startGame()
    }

    fun pauseGameTetris() {
        mutableState.update { it.copy(isPaused = !it.isPaused) }
    }

    fun stopGameTetris() {
        gameJob?.cancel()
        mutableState.update { it.copy(isPaused = false, isGameOver = false) }
    }

    private fun checkCollision(tetromino: Tetromino, position: Pair<Int, Int>, board: Array<Array<Color>>): Boolean {
        return tetromino.shape.any { (x, y) ->
            val newX = position.first + x
            val newY = position.second + y
            newX >= 20 || newY < 0 || newY >= 10 || board[newX][newY] != GreenComp.copy(alpha = 0.05f)
        }
    }

    private fun placeTetromino(board: Array<Array<Color>>, tetromino: Tetromino, position: Pair<Int, Int>): Array<Array<Color>> {
        val newBoard = board.map { it.clone() }.toTypedArray()
        tetromino.shape.forEach { (x, y) ->
            val newX = position.first + x
            val newY = position.second + y
            newBoard[newX][newY] = tetromino.color
        }
        return newBoard
    }

    private fun generateNewTetromino(): Tetromino {
        val tetrominoes = listOf(
            Tetromino(listOf(Pair(0, 0), Pair(1, 0), Pair(2, 0), Pair(3, 0)), DarkGreen1), // I-h
            Tetromino(listOf(Pair(0, 0), Pair(0, 1), Pair(0, 2), Pair(0, 3)), DarkGreen2), // I-v

            Tetromino(listOf(Pair(0, 0), Pair(1, 0), Pair(0, 1), Pair(1, 1)), DarkGreen3), // O

            Tetromino(listOf(Pair(0, 1), Pair(1, 1), Pair(1, 0), Pair(2, 1)), DarkGreen4), // T-hc
            Tetromino(listOf(Pair(0, 0), Pair(1, 0), Pair(1, 1), Pair(2, 0)), DarkGreen5), // T-hb
            Tetromino(listOf(Pair(0, 0), Pair(0, 1), Pair(1, 1), Pair(0, 2)), DarkGreen6), // T-vd
            Tetromino(listOf(Pair(1, 0), Pair(1, 1), Pair(0, 1), Pair(1, 2)), DarkGreen7), // T-ve

            Tetromino(listOf(Pair(0, 0), Pair(1, 0), Pair(2, 0), Pair(2, 1)), DarkGreen8), // L-h
            Tetromino(listOf(Pair(0, 0), Pair(1, 0), Pair(2, 0), Pair(0, 1)), DarkGreen9), // J-h
            Tetromino(listOf(Pair(0, 1), Pair(1, 1), Pair(2, 1), Pair(2, 0)), DarkGreen10), // L-h
            Tetromino(listOf(Pair(0, 0), Pair(0, 1), Pair(1, 1), Pair(2, 1)), DarkGreen11), // J-h

            Tetromino(listOf(Pair(0, 0), Pair(0, 1), Pair(0, 2), Pair(1, 2)), DarkGreen12), // L-v
            Tetromino(listOf(Pair(1, 0), Pair(1, 1), Pair(1, 2), Pair(0, 2)), DarkGreen13), // J-v
            Tetromino(listOf(Pair(1, 0), Pair(0, 0), Pair(0, 1), Pair(0, 2)), DarkGreen14), // L-v
            Tetromino(listOf(Pair(0, 0), Pair(1, 0), Pair(1, 1), Pair(1, 2)), DarkGreen15), // J-v

            Tetromino(listOf(Pair(0, 1), Pair(1, 1), Pair(1, 0), Pair(2, 0)), DarkGreen16), // S-h
            Tetromino(listOf(Pair(0, 0), Pair(1, 0), Pair(1, 1), Pair(2, 1)), DarkGreen17), // Z-h
            Tetromino(listOf(Pair(0, 0), Pair(0, 1), Pair(1, 1), Pair(1, 2)), DarkGreen18), // S-v
            Tetromino(listOf(Pair(1, 0), Pair(1, 1), Pair(0, 1), Pair(0, 2)), DarkGreen19)  // Z-v
        )
        return tetrominoes[Random().nextInt(tetrominoes.size)]
    }

    private fun clearFullLines(board: Array<Array<Color>>): Pair<Array<Array<Color>>, Int> {
        val newBoard = board.filterIndexed { index, row -> row.any { it == GreenComp.copy(alpha = 0.05f) } }.toMutableList()
        val linesCleared  = 20 - newBoard.size
        repeat(linesCleared) {
            newBoard.add(0, Array(10) { GreenComp.copy(alpha = 0.05f) })
        }
        return Pair(newBoard.toTypedArray(), linesCleared)
    }

    private fun rotateTetromino(tetromino: Tetromino): Tetromino {

        val centerX = tetromino.shape.sumOf { it.first } / tetromino.shape.size
        val centerY = tetromino.shape.sumOf { it.second } / tetromino.shape.size

        // Rotacione os blocos ao redor do centro
        val rotatedShape = tetromino.shape.map { (x, y) ->
            val relativeX = x - centerX
            val relativeY = y - centerY

            // Rotacione 90 graus no sentido horário
            val newX = -relativeY
            val newY = relativeX

            Pair(newX + centerX, newY + centerY)
        }

        return Tetromino(rotatedShape, tetromino.color)
    }

    fun move(move: Move) {
        mutableState.update { currentState ->
            if (currentState.isGameOver) return@update currentState

            val newPosition: Pair<Int, Int>
            val newTetromino: Tetromino

            when (move) {
                Move.Left -> {
                    newPosition = Pair(currentState.position.first, currentState.position.second - 1)
                    newTetromino = currentState.currentTetromino
                }
                Move.Right -> {
                    newPosition = Pair(currentState.position.first, currentState.position.second + 1)
                    newTetromino = currentState.currentTetromino
                }
                Move.Drop -> {
                    newPosition = Pair(currentState.position.first + 1, currentState.position.second)
                    newTetromino = currentState.currentTetromino
                }
            }

            val collision = checkCollision(newTetromino, newPosition, currentState.board)

            if (!collision) {
                currentState.copy(position = newPosition, currentTetromino = newTetromino)
            } else {
                // Caso de colisão ou no movimento de queda, fixar a peça e gerar uma nova
                if (move == Move.Drop) {
                    val updatedBoard = placeTetromino(currentState.board, currentState.currentTetromino, currentState.position)
                    val newTetrominoGenerated = generateNewTetromino()
                    val (clearedBoard, linesCleared) = clearFullLines(updatedBoard)
                    val newScore = currentState.score + (linesCleared * 5)
                    currentState.copy(
                        board = clearedBoard,
                        currentTetromino = newTetrominoGenerated,
                        position = Pair(0, 5),
                        score = newScore
                    )
                } else {
                    // Retornar sem alterar o estado para outros movimentos
                    currentState
                }
            }
        }
    }
}