package com.dieyteixeira.gamesplus.games.game_pacman

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.unit.dp
import com.dieyteixeira.gamesplus.R
import com.dieyteixeira.gamesplus.ui.theme.DarkBlue
import com.dieyteixeira.gamesplus.ui.theme.GreenComp
import kotlinx.coroutines.delay

@Composable
fun GamePacman(
    navigateClick: Boolean
) {
    val color = DarkBlue

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val game = remember { PacmanGame(scope, context) }
    val state = game.state.collectAsState(initial = null)
    var playerName by remember { mutableStateOf("") }
    var playerColor by remember { mutableStateOf(Color.LightGray) }
    var showReturnSettingsPacman by remember { mutableStateOf(false) }
    var showEndGamePacman by remember { mutableStateOf(false) }

    var showSettings by remember { mutableStateOf(true) }

    val onStartGame: (PacmanState) -> Unit = { config ->
        showSettings = false
        game.resetGamePacman()
    }

    LaunchedEffect(navigateClick) {
        if (navigateClick) {
            game.pauseGamePacman()
        } else {
            game.pauseGamePacman()
        }
    }

    if (showReturnSettingsPacman) {
        ShowReturnSettingsPacman(
            onNo = { showReturnSettingsPacman = false },
            onYes = {
                showReturnSettingsPacman = false
                showSettings = true
                game.stopGamePacman()
            }
        )
    }

    if (showEndGamePacman) {
        val playerRecordPacman = getPlayerRecordPacman(context, "Pacman")
        EndGamePacman(
            playerName = playerName,
            playerColor = playerColor,
            playerScore = state.value?.score ?: 0,
            recordName = playerRecordPacman.second.toString(),
            recordScore = playerRecordPacman.first,
            onNewGame = {
                showEndGamePacman = false
                showSettings = true
                game.stopGamePacman()
            },
            onRestartGame = {
                showEndGamePacman = false
                game.resetGamePacman()
            }
        )
    }

    LaunchedEffect(state.value?.isGameOver) {
        if (state.value?.isGameOver == true) {
            showEndGamePacman = true
            delay(500)
            savePlayerRecordPacman(
                context = context,
                playerName = playerName,
                gameScore = state.value?.score!!,
                gameName = "Pacman"
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showSettings) {
            SettingsPacman(
                color = color,
                playerName = playerName,
                playerColor = playerColor,
                onPlayerNameChange = { playerName = it },
                onPlayerColorChange = { playerColor = it },
                onStartGame = onStartGame
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 20.dp, 10.dp, 10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_new_game),
                    contentDescription = "Novo Jogo",
                    colorFilter = ColorFilter.tint(Color.Gray),
                    modifier = Modifier
                        .size(23.dp)
                        .clickable { showReturnSettingsPacman = true }
                )
            }
            state.value?.let {
                ScorePacman(
                    color = playerColor,
                    score = it.score,
                    playerName = playerName
                )
                Spacer(modifier = Modifier.size(2.dp))
                BoardPacMan(it)
            }
            ButtonsPacman {
                game.move = it
            }
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun BoardPacMan(state: PacmanState) {
    BoxWithConstraints(
        Modifier
            .background(Color.White)
            .padding(16.dp)
    ) {
        val tileSize = maxWidth / PacmanGame.BOARD_SIZE_PACMAN

        // Desenhar Pac-Man
        Canvas(
            modifier = Modifier
                .offset(x = tileSize * state.pacman.first, y = tileSize * state.pacman.second)
                .size(tileSize)
        ) {
            val (startAngle, sweepAngle) = when (state.direction) {
                "right" -> 30f to 300f  // Boca voltada para a direita
                "left" -> 210f to 300f  // Boca voltada para a esquerda
                "up" -> 120f to 300f    // Boca voltada para cima
                "down" -> -60f to 300f  // Boca voltada para baixo
                else -> 30f to 300f     // Padrão (direita)
            }

            drawArc(
                color = Color.Yellow,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                size = size
            )
        }


        // Desenhar comida
        state.food.forEach {
            Box(
                Modifier
                    .offset(x = tileSize * it.first, y = tileSize * it.second)
                    .size(tileSize)
                    .background(Food, RoundedCornerShape(2.dp))
                    .border(4.5.dp, Color.White)
            )
        }

        // Desenhar comida especial
        state.bestFood.forEach {
            Box(
                Modifier
                    .offset(x = tileSize * it.first, y = tileSize * it.second)
                    .size(tileSize)
                    .background(Food, RoundedCornerShape(2.dp))
                    .border(3.dp, Color.White)
            )
        }

        val ghostColors = listOf(
            Color(0xFFE43C2F), // Blinky - Fantasma vermelho
            Color(0xFFCC8BE4), // Pinky - Fantasma rosa
            Color(0xFF0080D8), // Inky - Fantasma azul
            Color(0xFFFFA500)  // Clyde - Fantasma laranja
        )

        // Desenhar fantasmas
        state.ghosts.forEachIndexed { index, ghost ->
            Box(
                Modifier
                    .offset(x = tileSize * ghost.first, y = tileSize * ghost.second)
                    .size(tileSize)
                    .background(
                        if (state.ghostVulnerabilities.getOrNull(index) == true) GhostVulnerable else ghostColors.getOrElse(index) { Color.Gray },
                        RoundedCornerShape(20.dp, 20.dp, 10.dp, 10.dp)
                    ),
                contentAlignment = Alignment.TopCenter
            ) {
                Spacer(modifier = Modifier.size(tileSize / 3))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(tileSize / 3)
                            .background(
                                if (state.ghostVulnerabilities.getOrNull(index) == true) Color.Gray else Color.White,
                                RoundedCornerShape(100)
                            ),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Box(
                            modifier = Modifier
                                .size(tileSize / 6)
                                .background(
                                    if (state.ghostVulnerabilities.getOrNull(index) == true) Color.White else Color.Black,
                                    RoundedCornerShape(100)
                                )
                        )
                    }
                    Spacer(modifier = Modifier.size(tileSize / 6))
                    Box(
                        modifier = Modifier
                            .size(tileSize / 3)
                            .background(
                                if (state.ghostVulnerabilities.getOrNull(index) == true) Color.Gray else Color.White,
                                RoundedCornerShape(100)
                            ),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Box(
                            modifier = Modifier
                                .size(tileSize / 6)
                                .background(
                                    if (state.ghostVulnerabilities.getOrNull(index) == true) Color.White else Color.Black,
                                    RoundedCornerShape(100)
                                )
                        )
                    }
                }
            }
        }

        // Desenhar paredes
        state.walls.forEach {
            Box(
                Modifier
                    .offset(x = tileSize * it.first, y = tileSize * it.second)
                    .size(tileSize)
                    .background(GreenComp, RoundedCornerShape(2.dp))
                    .border(0.dp, Color.White)
            )
        }

        Box(
            Modifier
                .size(maxWidth)
                .border(1.dp, GreenComp)
        )
    }
}