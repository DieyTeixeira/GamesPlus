package com.dieyteixeira.gamesplus.games.game_tetris

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dieyteixeira.gamesplus.games.game_snake.ShowClearRecordSnake
import com.dieyteixeira.gamesplus.games.game_snake.clearPlayerRecordSnake
import com.dieyteixeira.gamesplus.games.game_snake.getPlayerRecordSnake

@Composable
fun ScoreTetris(
    color: Color,
    score: Int,
    speed: Long
) {
    val context = LocalContext.current
    val playerRecordTetris = getPlayerRecordTetris(context, "Tetris")
    var showClearRecordTetris by remember { mutableStateOf(false) }

    if (showClearRecordTetris) {
        ShowClearRecordTetris(
            onNo = { showClearRecordTetris = false },
            onYes = {
                showClearRecordTetris = false

                clearPlayerRecordTetris(context, "Tetris")
                Toast.makeText(context, "Recorde zerado!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    Row(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.9f),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {}
        Spacer(modifier = Modifier.width(5.dp))
        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Recorde",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 10.sp),
                color = Color.DarkGray
            )
        }
    }
    Spacer(modifier = Modifier.height(2.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth(0.9f),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .height(60.dp)
                .background(
                    color = color,
                    shape = RoundedCornerShape(10.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Dificult",
                style = MaterialTheme.typography.displaySmall.copy(fontSize = 12.sp),
                color = Color.White,
                modifier = Modifier
                    .height(20.dp)
                    .padding(3.dp)
            )
            Row(
                modifier = Modifier
                    .height(40.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "$speed",
                    style = MaterialTheme.typography.displaySmall.copy(fontSize = 25.sp),
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.width(5.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .height(60.dp)
                .background(
                    color = color,
                    shape = RoundedCornerShape(10.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Score",
                style = MaterialTheme.typography.displaySmall.copy(fontSize = 12.sp),
                color = Color.White,
                modifier = Modifier
                    .height(20.dp)
                    .padding(3.dp)
            )
            Text(
                text = "$score",
                style = MaterialTheme.typography.displaySmall.copy(fontSize = 25.sp),
                color = Color.White,
                modifier = Modifier.height(40.dp)
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        Row(
            modifier = Modifier
                .weight(2f)
                .height(60.dp)
                .background(
                    color = color.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable { showClearRecordTetris = true },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier
                    .weight(3f)
                    .height(60.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Score",
                    style = MaterialTheme.typography.displaySmall.copy(fontSize = 12.sp),
                    color = Color.White,
                    modifier = Modifier
                        .height(20.dp)
                        .padding(3.dp)
                )
                Text(
                    text = "${playerRecordTetris.first}",
                    style = MaterialTheme.typography.displaySmall.copy(fontSize = 25.sp),
                    color = Color.White,
                    modifier = Modifier.height(40.dp)
                )
            }
            Column(
                modifier = Modifier
                    .weight(4f)
                    .height(60.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Jogador",
                    style = MaterialTheme.typography.displaySmall.copy(fontSize = 12.sp),
                    color = Color.White,
                    modifier = Modifier
                        .height(20.dp)
                        .padding(3.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(top = 4.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        text = "${playerRecordTetris.second}",
                        style = MaterialTheme.typography.displaySmall.copy(fontSize = 18.sp),
                        color = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}