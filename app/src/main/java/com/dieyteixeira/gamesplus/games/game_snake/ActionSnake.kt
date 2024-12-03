package com.dieyteixeira.gamesplus.games.game_snake

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.Random

data class State(
    val playerName: String = "",
    val playerColor: Color = Color.LightGray,
    val food: Pair<Int, Int> = Pair(5, 5),
    val snake: List<Pair<Int, Int>> = listOf(Pair(7, 7)),
    val score: Int = 0,
    val speed: Long = 150L,
    val isGameOver: Boolean = false
)

class Game(private val scope: CoroutineScope, context: Context) {

    private val mutex = Mutex()
    private val mutableState =
        MutableStateFlow(State(food = Pair(5, 5), snake = listOf(Pair(7, 7))))
    val state: Flow<State> = mutableState

    var move = Pair(1, 0)
        set(value) {
            scope.launch {
                mutex.withLock {
                    if (value.first != -move.first || value.second != -move.second) {
                        field = value // Se não for oposta, atualiza a direção
                    }
                }
            }
        }

    private var gameJob: Job? = null
    private var speed: Long = 150L
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("game_preferences", Context.MODE_PRIVATE)

    var highScoreSnake: Int = sharedPreferences.getInt("high_score_snake", 0)

    init {
        startGame()
    }

    private fun startGame() {

        gameJob?.cancel()

        gameJob = scope.launch {
            var snakeLength = 4

            while (true) {
                delay(speed)
                mutableState.update {
                    if (it.isGameOver) {
                        if (it.score > highScoreSnake) {
                            highScoreSnake = it.score
                            saveHighScore(highScoreSnake) // Salva o novo recorde
                        }
                        return@update it // Pausa o jogo se estiver em "Game Over"
                    }

                    val newPosition = it.snake.first().let { poz ->
                        mutex.withLock {
                            Pair(
                                (poz.first + move.first + BOARD_SIZE) % BOARD_SIZE,
                                (poz.second + move.second + BOARD_SIZE) % BOARD_SIZE
                            )
                        }
                    }

                    val ateFood = newPosition == it.food

                    if (ateFood) {
                        snakeLength++
                        speed = (speed * 0.99935).toLong().coerceAtLeast(50L)
                    }

                    val isCollision = it.snake.contains(newPosition)

                    it.copy(
                        food = if (ateFood) Pair(
                            Random().nextInt(BOARD_SIZE),
                            Random().nextInt(BOARD_SIZE)
                        ) else it.food,
                        snake = if (isCollision) it.snake else listOf(newPosition) + it.snake.take(snakeLength - 1),
                        score = if (ateFood) it.score + 5 else it.score,
                        speed = 150 - speed,
                        isGameOver = isCollision // Define "Game Over" em caso de colisão
                    )
                }
            }
        }
    }

    fun reset() {
        mutableState.update {
            State(food = Pair(5, 5), snake = listOf(Pair(7, 7)))
        }
        move = Pair(1, 0)
        speed = 150L
        startGame()
    }

    private fun saveHighScore(score: Int) {
        sharedPreferences.edit().putInt("high_score_snake", score).apply()
    }

    companion object {
        const val BOARD_SIZE = 24
    }
}