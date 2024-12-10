package com.dieyteixeira.gamesplus.games.game_puzzle

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ShowReturnSettingsPuzzle(
    onNo: () -> Unit = {},
    onYes: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = {},
        buttons = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Deseja configurar uma nova partida?",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = Color.DarkGray,
                        fontSize = 20.sp
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .width(130.dp)
                            .height(35.dp)
                            .background(Color.LightGray, shape = RoundedCornerShape(100))
                            .clickable { onNo() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Não",
                            style = MaterialTheme.typography.displaySmall.copy(fontSize = 14.sp),
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    Box(
                        modifier = Modifier
                            .width(130.dp)
                            .height(35.dp)
                            .background(Color.LightGray, shape = RoundedCornerShape(100))
                            .clickable { onYes() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Sim",
                            style = MaterialTheme.typography.displaySmall.copy(fontSize = 14.sp),
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    )
}