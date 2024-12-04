package com.dieyteixeira.gamesplus.games.game_tetris

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ShowClearRecordTetris(
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
                    text = "Deseja excluir os dados de\nrecorde para esse jogo?",
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

@Composable
fun ShowReturnSettingsTetris(
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

@Composable
fun ShowRestartGameTetris(
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
                    text = "Deseja reiniciar essa partida?",
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

@Composable
fun EndGameTetris(
    playerName: String,
    playerColor: Color,
    playerScore: Int,
    recordName: String,
    recordScore: Int,
    onNewGame: () -> Unit,
    onRestartGame: () -> Unit
) {
    val bestScore = if (playerScore > recordScore) 1 else 0
    val scalePlayer by animateFloatAsState(
        targetValue = if (bestScore == 1) 1f else 0.8f,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
    )
    val scaleRecord by animateFloatAsState(
        targetValue = if (bestScore == 1) 0.8f else 1f,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
    )

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
                // Título do Diálogo
                Text(
                    text = "Fim do Jogo",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = Color.DarkGray,
                        fontSize = 20.sp
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))

                // Recorde
                Column(
                    modifier = Modifier
                        .scale(scaleRecord),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Recorde",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                color = Color.DarkGray,
                                fontSize = 18.sp
                            )
                        )
                    }
                    Row(
                        modifier = Modifier
                            .width(280.dp)
                            .height(80.dp)
                            .background(
                                color = playerColor.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(15.dp)
                            ),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(4f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "jogador",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    color = Color.White,
                                    fontSize = 16.sp
                                ),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = recordName,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    color = Color.White,
                                    fontSize = 30.sp
                                ),
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(2f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "$recordScore",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    color = Color.White,
                                    fontSize = 50.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "pontos",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    color = Color.White,
                                    fontSize = 8.sp
                                )
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))

                // Pontuação
                Column(
                    modifier = Modifier
                        .scale(scalePlayer),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Pontuação atual",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                color = Color.DarkGray,
                                fontSize = 18.sp
                            )
                        )
                    }
                    Row(
                        modifier = Modifier
                            .width(280.dp)
                            .height(80.dp)
                            .background(
                                color = playerColor,
                                shape = RoundedCornerShape(15.dp)
                            ),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(4f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "jogador",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    color = Color.White,
                                    fontSize = 16.sp
                                ),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = playerName,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    color = Color.White,
                                    fontSize = 30.sp
                                ),
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(2f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "$playerScore",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    color = Color.White,
                                    fontSize = 50.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "pontos",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    color = Color.White,
                                    fontSize = 8.sp
                                )
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))

                // Botões
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .width(130.dp)
                            .height(35.dp)
                            .background(Color.LightGray, shape = RoundedCornerShape(100))
                            .clickable { onNewGame() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Novo jogo",
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
                            .clickable { onRestartGame() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Reiniciar",
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