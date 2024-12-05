package com.dieyteixeira.gamesplus.games.game_velha

import android.R.attr.maxWidth
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dieyteixeira.gamesplus.R
import com.dieyteixeira.gamesplus.games.game_snake.ScoreSnake
import com.dieyteixeira.gamesplus.games.game_snake.savePlayerRecordSnake
import com.dieyteixeira.gamesplus.ui.theme.DarkBlue
import com.dieyteixeira.gamesplus.ui.theme.GreenComp
import kotlinx.coroutines.delay

@Composable
fun GameVelha(
    navigateClick: Boolean
) {
    val color = DarkBlue

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val game = remember { TicTacToeGame(scope) }
    val state = game.state.collectAsState(initial = null)
    var player1Name by remember { mutableStateOf("") }
    var player2Name by remember { mutableStateOf("") }
    var player1Color by remember { mutableStateOf(Color.LightGray) }
    var player2Color by remember { mutableStateOf(Color.LightGray) }
    var showReturnSettingsVelha by remember { mutableStateOf(false) }
    var showEndGameVelha by remember { mutableStateOf(false) }

    var showSettings by remember { mutableStateOf(true) }

    val onStartGame: (VelhaState) -> Unit = { config ->
        showSettings = false
    }

    val player1Victories = getVictoryVelha(context, player1Name, player2Name, "Velha")
    val player2Victories = getVictoryVelha(context, player2Name, player1Name, "Velha")

    if (showReturnSettingsVelha) {
        ShowReturnSettingsVelha(
            onNo = { showReturnSettingsVelha = false },
            onYes = {
                showReturnSettingsVelha = false
                showSettings = true
            }
        )
    }

    if (showEndGameVelha) {
        state.value?.let {

            val victorious = if (it.winner != null) {
                if (it.winner == "X") {
                    Pair(player1Name, player1Color)
                } else {
                    Pair(player2Name, player2Color)
                }
            } else {
                Pair("Empate", Color.Gray)
            }

            EndGameVelha(
                playerName = victorious.first,
                playerColor = victorious.second,
                onNewGame = {
                    showEndGameVelha = false
                    game.reset()
                    showSettings = true
                },
                onRestartGame = {
                    showEndGameVelha = false
                    game.reset()
                }
            )
        }
    }

    LaunchedEffect(state.value?.isGameOver) {
        if (state.value?.isGameOver == true) {
            showEndGameVelha = true
            delay(500)

            if (state.value?.winner != null) {
                saveVictoryVelha(
                    context = context,
                    winnerName = if (state.value?.winner == "X") player1Name else player2Name,
                    opponentName = if (state.value?.winner == "X") player2Name else player1Name,
                    gameName = "Velha"
                )
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showSettings) {
            SettingsVelha(
                color = color,
                player1Name = player1Name,
                player2Name = player2Name,
                player1Color = player1Color,
                player2Color = player2Color,
                onPlayer1NameChange = { player1Name = it },
                onPlayer2NameChange = { player2Name = it },
                onPlayer1ColorChange = { player1Color = it },
                onPlayer2ColorChange = { player2Color = it },
                onStartGame = onStartGame
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_new_game),
                    contentDescription = "Novo Jogo",
                    colorFilter = ColorFilter.tint(Color.Gray),
                    modifier = Modifier
                        .size(23.dp)
                        .clickable { showReturnSettingsVelha = true }
                )
            }
            state.value?.let {
                ScoreVelha(
                    player1Name = player1Name,
                    player2Name = player2Name,
                    player1Color = player1Color,
                    player2Color = player2Color,
                    player1Victories = player1Victories,
                    player2Victories = player2Victories,
                    currentPlayer = it.currentPlayer
                )
                Spacer(modifier = Modifier.size(15.dp))
                BoardVelha(
                    state = it,
                    player1Color = player1Color,
                    player2Color = player2Color,
                    currentPlayer = it.currentPlayer
                ) { index ->
                    game.playMove(index)
                }
            }
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun BoardVelha(
    state: VelhaState,
    player1Color: Color,
    player2Color: Color,
    currentPlayer: String,
    onCellClick: (Int) -> Unit
) {

    BoxWithConstraints(
        modifier = Modifier
            .background(Color.White)
            .padding(15.dp)
    ) {
        Box(
            modifier = Modifier
                .size(maxWidth)
                .border(2.dp, GreenComp),
            contentAlignment = Alignment.Center
        ) {
            val size = this@BoxWithConstraints.maxWidth / 3

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                for (row in 0..2) {
                    Row {
                        for (col in 0..2) {
                            val index = row * 3 + col
                            val symbol = state.board[index]
                            val color = when (symbol) {
                                "X" -> player1Color
                                "O" -> player2Color
                                else -> Color.Transparent
                            }
                            Box(
                                modifier = Modifier
                                    .size(size)
                                    .background(
                                        Color.White,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .border(1.dp, GreenComp)
                                    .clickable { onCellClick(index) },
                                contentAlignment = Alignment.Center
                            ) {
                                DrawSymbol(
                                    symbol = state.board[index],
                                    color = color
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DrawSymbol(
    symbol: String,
    color: Color
) {
    val iconRes = when (symbol) {
        "X" -> {
            Pair(R.drawable.ic_x_velha, 68.dp)
        }
        "O" -> {
            Pair(R.drawable.ic_o_velha, 72.dp)
        }
        else -> {
            Pair(null, 0.dp)
        }
    }

    if (iconRes.first != null) {
        Image(
            painter = painterResource(id = iconRes.first!!),
            contentDescription = symbol,
            colorFilter = ColorFilter.tint(color),
            modifier = Modifier.size(iconRes.second)
        )
    }
}

@Composable
fun DrawSymbol2(
    symbol: String,
    pixelSize: Dp,
    color: Color
) {

    val xShape = listOf(
        listOf(true, false, false, false, true),
        listOf(false, true, false, true, false),
        listOf(false, false, true, false, false),
        listOf(false, true, false, true, false),
        listOf(true, false, false, false, true)
    )

    val oShape = listOf(
        listOf(false, true, true, true, false),
        listOf(true, false, false, false, true),
        listOf(true, false, false, false, true),
        listOf(true, false, false, false, true),
        listOf(false, true, true, true, false)
    )

    val shape = if (symbol == "X") xShape else if (symbol == "O") oShape else emptyList()

    Column {
        for (row in shape) {
            Row {
                for (cell in row) {
                    Box(
                        modifier = Modifier
                            .size(pixelSize)
                            .background(if (cell) color else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        if (pixelSize > 10.dp) {
                            Box(
                                modifier = Modifier
                                    .size(pixelSize * 0.75f) // Tamanho menor, 50% do tamanho do tile
                                    .align(Alignment.Center) // Centralizado dentro do tile
                                    .border(
                                        1.2.dp,
                                        if (cell) Color.White else Color.Transparent,
                                        RoundedCornerShape(1.dp)
                                    )// Cor do Box menor
                            )
                        }
                    }
                }
            }
        }
    }
}