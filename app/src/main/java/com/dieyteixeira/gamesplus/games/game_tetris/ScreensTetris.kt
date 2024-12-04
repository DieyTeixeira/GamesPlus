package com.dieyteixeira.gamesplus.games.game_tetris

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dieyteixeira.gamesplus.ui.theme.GreenComp

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun BoardTetris(state: TetrisState) {

    val size = 20

    BoxWithConstraints(
        Modifier
            .background(Color.White)
            .padding(16.dp)
    ) {

//        Box(
//            Modifier
//                .size(maxWidth)
//                .border(2.dp, GreenComp)
//        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box {
                    // Renderiza o tabuleiro
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        for (rowIndex in 0 until state.board.size) {
                            Row(
                                horizontalArrangement = Arrangement.Center
                            ) {
                                for (colIndex in 0 until state.board[rowIndex].size) {
                                    val color = state.board[rowIndex][colIndex]

                                    // Renderiza a célula atual
                                    Box(
                                        modifier = Modifier
                                            .size(size.dp)
                                            .background(color, RoundedCornerShape(2.dp)) // Cor do bloco
                                            .border(0.dp, Color.White)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(size.dp * 0.75f)
                                                .align(Alignment.Center)
                                                .border(1.5.dp, Color.White, RoundedCornerShape(1.dp))
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Adiciona a peça em movimento (não fixada) ao tabuleiro
                    state.currentTetromino.shape.forEach { (x, y) ->
                        val (pieceX, pieceY) = state.position
                        val boardX = pieceX + x
                        val boardY = pieceY + y

                        // Verifica se a posição está dentro dos limites do tabuleiro
                        if (boardX in 0 until state.board.size && boardY in 0 until state.board[0].size) {
                            Box(
                                modifier = Modifier
                                    .offset(x = (boardY * size).dp, y = (boardX * size).dp)
                                    .size(size.dp)
                                    .background(state.currentTetromino.color, RoundedCornerShape(2.dp))
                                    .border(0.dp, Color.White)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(size.dp * 0.75f)
                                        .align(Alignment.Center)
                                        .border(1.5.dp, Color.White, RoundedCornerShape(1.dp))
                                )
                            }
                        }
                    }
                }
            }
//        }
    }
}