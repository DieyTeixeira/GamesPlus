package com.dieyteixeira.gamesplus.games.game_snake

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dieyteixeira.gamesplus.ui.theme.GreenComp

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun BoardSnake(
    state: SnakeState,
    color: Color
) {
    BoxWithConstraints(
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp)
    ) {
        val tileSize = maxWidth / Game.BOARD_SIZE

        Box(
            modifier = Modifier
                .offset(x = tileSize * state.food.first, y = tileSize * state.food.second)
                .size(tileSize)
                .rotate(45f)
                .background(Color.Transparent)
        ) {
            Column {
                Row {
                    Box(
                        modifier = Modifier
                            .size(tileSize / 2)
                            .padding(0.8.dp)
                            .background(
                                GreenComp,
                                RoundedCornerShape(1.dp)
                            )
                    )
                    Box(
                        modifier = Modifier
                            .size(tileSize / 2)
                            .padding(0.8.dp)
                            .background(
                                GreenComp,
                                RoundedCornerShape(1.dp)
                            )
                    )
                }
                Row {
                    Box(
                        modifier = Modifier
                            .size(tileSize / 2)
                            .padding(0.8.dp)
                            .background(
                                GreenComp,
                                RoundedCornerShape(1.dp)
                            )
                    )
                    Box(
                        modifier = Modifier
                            .size(tileSize / 2)
                            .padding(0.8.dp)
                            .background(
                                GreenComp,
                                RoundedCornerShape(1.dp)
                            )
                    )
                }
            }
        }

        state.snake.forEach {
            Box(
                modifier = Modifier
                    .offset(x = tileSize * it.first, y = tileSize * it.second)
                    .size(tileSize)
                    .background(
                        GreenComp,
                        RoundedCornerShape(2.dp)
                    )
                    .border(0.dp, Color.White)
            ) {
                Box(
                    modifier = Modifier
                        .size(tileSize * 0.75f)
                        .align(Alignment.Center)
                        .border(
                            1.5.dp,
                            Color.White,
                            RoundedCornerShape(1.dp)
                        )
                )
            }
        }

        Box(
            modifier = Modifier
                .size(maxWidth)
                .border(2.dp, color)
        )
    }
}