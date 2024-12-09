package com.dieyteixeira.gamesplus.games.game_sudoku

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
fun InformationSudoku(
    color: Color,
    playerName: String,
    nivDific: String,
    nivAjuda: String
) {

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
                text = "Dificuldade",
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
                    text = nivDific,
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
                text = "Ajuda",
                style = MaterialTheme.typography.displaySmall.copy(fontSize = 12.sp),
                color = Color.White,
                modifier = Modifier
                    .height(20.dp)
                    .padding(3.dp)
            )
            Text(
                text = nivAjuda,
                style = MaterialTheme.typography.displaySmall.copy(fontSize = 25.sp),
                color = Color.White,
                modifier = Modifier.height(40.dp)
            )
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
                text = "Jogador",
                style = MaterialTheme.typography.displaySmall.copy(fontSize = 12.sp),
                color = Color.White,
                modifier = Modifier
                    .height(20.dp)
                    .padding(3.dp)
            )
            Text(
                text = playerName,
                style = MaterialTheme.typography.displaySmall.copy(fontSize = 25.sp),
                color = Color.White,
                modifier = Modifier.height(40.dp)
            )
        }
    }
}