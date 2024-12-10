package com.dieyteixeira.gamesplus.games.game_puzzle

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScorePuzzle(
    levelAtual: Int,
    playerColor: Color,
) {
    val level = when (levelAtual) {
        3 -> "Fácil"
        5 -> "Médio"
        else -> "Difícil"
    }

    Row(
        modifier = Modifier
            .fillMaxSize(0.85f),
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
            .fillMaxWidth(0.90f),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .height(60.dp)
                .background(
                    color = playerColor,
                    shape = RoundedCornerShape(10.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Nível",
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
                    text = level,
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
                    color = playerColor,
                    shape = RoundedCornerShape(10.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Movimentos",
                style = MaterialTheme.typography.displaySmall.copy(fontSize = 12.sp),
                color = Color.White,
                modifier = Modifier
                    .height(20.dp)
                    .padding(3.dp)
            )
            Text(
                text = "12",
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
                    color = playerColor.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable { /*showClearRecordDialog = true*/ },
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
                    text = "Movim.",
                    style = MaterialTheme.typography.displaySmall.copy(fontSize = 12.sp),
                    color = Color.White,
                    modifier = Modifier
                        .height(20.dp)
                        .padding(3.dp)
                )
                Text(
                    text = "10",
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
                        text = "Testador",
                        style = MaterialTheme.typography.displaySmall.copy(fontSize = 18.sp),
                        color = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}