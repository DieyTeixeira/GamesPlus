package com.dieyteixeira.gamesplus.games.game_velha

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dieyteixeira.gamesplus.games.game_memory.FlipCardMemory
import com.dieyteixeira.gamesplus.games.game_memory.FlipPlayerMemory
import com.dieyteixeira.gamesplus.games.game_memory.countPairsByPlayer
import com.dieyteixeira.gamesplus.games.game_memory.getTwoPlayersVictories

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScoreVelha(
    player1Name: String,
    player2Name: String,
    player1Color: Color,
    player2Color: Color,
    player1Victories: Int,
    player2Victories: Int,
    currentPlayer: String
) {
    val context = LocalContext.current

    val scale1 by animateFloatAsState(
        targetValue = if (currentPlayer == "X") 1f else 0.8f,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
    )
    val scale2 by animateFloatAsState(
        targetValue = if (currentPlayer == "X") 0.8f else 1f,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
    )

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(150.dp)
                .height(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .width(150.dp)
                    .height(40.dp)
                    .scale(scale1)
                    .background(
                        color = if (currentPlayer == "X") player1Color else player1Color.copy(
                            alpha = 0.5f
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = player1Name,
                        style = MaterialTheme.typography.displaySmall.copy(fontSize = 18.sp),
                        color = Color.White
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(40.dp)
                        .padding(2.dp)
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(0.dp, 8.dp, 8.dp, 0.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$player1Victories",
                        style = MaterialTheme.typography.displaySmall.copy(fontSize = 20.sp),
                        color = if (currentPlayer == "X") player1Color else player1Color.copy(
                            alpha = 0.5f
                        )
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(5.dp))

        FlipPlayerVelha(
            flipCard = if (currentPlayer == "X") FlipCardVelha.Previous else FlipCardVelha.Forward,
        )

        Spacer(modifier = Modifier.width(5.dp))
        Box(
            modifier = Modifier
                .width(150.dp)
                .height(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .width(150.dp)
                    .height(40.dp)
                    .scale(scale2)
                    .background(
                        color = if (currentPlayer != "X") player2Color else player2Color.copy(
                            alpha = 0.5f
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(40.dp)
                        .padding(2.dp)
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(8.dp, 0.dp, 0.dp, 8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$player2Victories",
                        style = MaterialTheme.typography.displaySmall.copy(fontSize = 20.sp),
                        color = if (currentPlayer != "X") player2Color else player2Color.copy(
                            alpha = 0.5f
                        )
                    )
                }
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = player2Name,
                        style = MaterialTheme.typography.displaySmall.copy(fontSize = 18.sp),
                        color = Color.White
                    )
                }
            }
        }
    }
}