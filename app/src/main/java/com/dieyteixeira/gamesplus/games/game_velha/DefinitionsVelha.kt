package com.dieyteixeira.gamesplus.games.game_velha

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

data class VelhaState(
    val player1Name: String = "",
    val player2Name: String = "",
    val player1Color: Color = Color.LightGray,
    val player2Color: Color = Color.LightGray,
    val board: List<String> = List(9) { "" },
    val currentPlayer: String = "X",
    val isGameOver: Boolean = false,
    val winner: String? = null
)

class TicTacToeGame(private val scope: CoroutineScope) {

    private val mutableState = MutableStateFlow(VelhaState())
    val state: Flow<VelhaState> = mutableState

    fun playMove(index: Int) {
        mutableState.update { state ->
            // Verifica se a casa está vazia e o jogo não acabou
            if (state.board[index].isEmpty() && !state.isGameOver) {
                // Atualiza a casa com o símbolo do jogador atual
                val newBoard = state.board.toMutableList()
                newBoard[index] = state.currentPlayer

                // Verifica se alguém ganhou
                val winner = checkWinner(newBoard)

                // Alterna o jogador
                val nextPlayer = if (state.currentPlayer == "X") "O" else "X"

                // Verifica se o jogo acabou
                val isGameOver = winner != null || newBoard.none { it.isEmpty() }

                VelhaState(
                    player1Name = state.player1Name,
                    player2Name = state.player2Name,
                    player1Color = state.player1Color,
                    player2Color = state.player2Color,
                    board = newBoard,
                    currentPlayer = nextPlayer,
                    isGameOver = isGameOver,
                    winner = winner
                )
            } else {
                state
            }
        }
    }

    private fun checkWinner(board: List<String>): String? {
        // Verifica as combinações possíveis de vitória
        val winningPatterns = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8), // linhas
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8), // colunas
            listOf(0, 4, 8), listOf(2, 4, 6) // diagonais
        )

        for (pattern in winningPatterns) {
            val (a, b, c) = pattern
            if (board[a] == board[b] && board[b] == board[c] && board[a].isNotEmpty()) {
                return board[a]  // Retorna o vencedor ("X" ou "O")
            }
        }
        return null
    }

    fun reset() {
        mutableState.update {
            VelhaState()
        }
    }
}