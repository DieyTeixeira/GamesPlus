package com.dieyteixeira.gamesplus.games.game_snake

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dieyteixeira.gamesplus.ui.theme.GreenComp
import com.dieyteixeira.gamesplus.ui.theme.GreenScreen

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun GameOverSnake(score: Int, highScore: Int, onRestart: () -> Unit) {
    BoxWithConstraints(
        Modifier
            .background(GreenScreen)
            .padding(16.dp)
    ) {

        Box(
            Modifier
                .size(maxWidth)
                .border(2.dp, GreenComp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(GreenScreen),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                Text(
                    text = "game over",
                    style = MaterialTheme.typography.displayMedium.copy(fontSize = 32.sp),
                    color = GreenComp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "final score: $score",
                    style = MaterialTheme.typography.displayMedium.copy(fontSize = 24.sp),
                    color = GreenComp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "high score: $highScore",
                    style = MaterialTheme.typography.displayMedium.copy(fontSize = 24.sp),
                    color = GreenComp
                )
                Spacer(modifier = Modifier.height(32.dp))
                Box(
                    modifier = Modifier
                        .background(GreenComp, RoundedCornerShape(10.dp))
                        .clickable {
                            onRestart()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Restart",
                        style = MaterialTheme.typography.displayMedium.copy(fontSize = 24.sp),
                        color = GreenScreen,
                        modifier = Modifier.padding(15.dp, 5.dp, 15.dp, 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(70.dp))
                Text(
                    text = "criado por Diey Teixeira",
                    style = MaterialTheme.typography.displayMedium.copy(fontSize = 20.sp),
                    color = GreenComp
                )
            }
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun BoardSnake(state: State) {
    BoxWithConstraints(
        Modifier
            .background(GreenScreen)
            .padding(16.dp)
    ) {
        val tileSize = maxWidth / Game.BOARD_SIZE

        Box(
            Modifier
                .size(maxWidth)
                .border(2.dp, GreenComp)
        )

        Box(
            Modifier
                .offset(x = tileSize * state.food.first, y = tileSize * state.food.second)
                .size(tileSize)
                .rotate(45f)
                .background(Color.Transparent)
        ) {
            Column {
                Row {
                    Box(
                        Modifier
                            .size(tileSize / 2)
                            .padding(0.8.dp)
                            .background(
                                GreenComp,
                                androidx.compose.foundation.shape.RoundedCornerShape(1.dp)
                            )
                    )
                    Box(
                        Modifier
                            .size(tileSize / 2)
                            .padding(0.8.dp)
                            .background(
                                GreenComp,
                                androidx.compose.foundation.shape.RoundedCornerShape(1.dp)
                            )
                    )
                }
                Row {
                    Box(
                        Modifier
                            .size(tileSize / 2)
                            .padding(0.8.dp)
                            .background(
                                GreenComp,
                                androidx.compose.foundation.shape.RoundedCornerShape(1.dp)
                            )
                    )
                    Box(
                        Modifier
                            .size(tileSize / 2)
                            .padding(0.8.dp)
                            .background(
                                GreenComp,
                                androidx.compose.foundation.shape.RoundedCornerShape(1.dp)
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
                        androidx.compose.foundation.shape.RoundedCornerShape(2.dp)
                    )
                    .border(0.dp, GreenScreen)
            ) {
                Box(
                    modifier = Modifier
                        .size(tileSize * 0.75f)
                        .align(Alignment.Center)
                        .border(
                            1.5.dp,
                            GreenScreen,
                            androidx.compose.foundation.shape.RoundedCornerShape(1.dp)
                        )
                )
            }
        }
    }
}