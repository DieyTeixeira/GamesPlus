package com.dieyteixeira.gamesplus.games.game_tetris

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import kotlinx.coroutines.delay

@Composable
fun GameTetris(
    navigateClick: Boolean
) {
    val color = DarkBlue

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val game = remember { TetrisGame(scope, context) }
    val state = game.state.collectAsState(initial = null)
    var playerName by remember { mutableStateOf("") }
    var playerColor by remember { mutableStateOf(Color.LightGray) }
    var showReturnSettingsTetris by remember { mutableStateOf(false) }
    var showEndGameTetris by remember { mutableStateOf(false) }
    var showRestartGameTetris by remember { mutableStateOf(false) }

    var showSettings by remember { mutableStateOf(true) }

    val onStartGame: (TetrisState) -> Unit = { config ->
        showSettings = false
        game.resetGameTetris()
    }

    LaunchedEffect(navigateClick) {
        if (navigateClick) {
            game.pauseGameTetris()
        } else {
            game.pauseGameTetris()
        }
    }

    if (showReturnSettingsTetris) {
        ShowReturnSettingsTetris(
            onNo = { showReturnSettingsTetris = false },
            onYes = {
                showReturnSettingsTetris = false
                showSettings = true
                game.stopGameTetris()
            }
        )
    }

    if (showRestartGameTetris) {
        ShowRestartGameTetris(
            onNo = { showRestartGameTetris = false },
            onYes = {
                showRestartGameTetris = false
                game.resetGameTetris()
            }
        )
    }

    if (showEndGameTetris) {
        val playerRecordTetris = getPlayerRecordTetris(context, "Tetris")
        EndGameTetris(
            playerName = playerName,
            playerColor = playerColor,
            playerScore = state.value?.score ?: 0,
            recordName = playerRecordTetris.second.toString(),
            recordScore = playerRecordTetris.first,
            onNewGame = {
                showEndGameTetris = false
                showSettings = true
                game.stopGameTetris()
            },
            onRestartGame = {
                showEndGameTetris = false
                game.resetGameTetris()
            }
        )
    }

    LaunchedEffect(state.value?.isGameOver) {
        if (state.value?.isGameOver == true) {
            showEndGameTetris = true
            delay(500)
            savePlayerRecordTetris(
                context = context,
                playerName = playerName,
                gameScore = state.value?.score!!,
                gameName = "Tetris"
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showSettings) {
            SettingsTetris(
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
                        .clickable { showReturnSettingsTetris = true }
                )
                Spacer(modifier = Modifier.width(25.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_restart),
                    contentDescription = "Reiniciar",
                    colorFilter = ColorFilter.tint(Color.Gray),
                    modifier = Modifier
                        .size(23.dp)
                        .clickable { showRestartGameTetris = true }
                )
            }
            state.value?.let {
                ScoreTetris(
                    color = playerColor,
                    score = it.score,
                    speed = it.speed
                )
                Spacer(modifier = Modifier.size(2.dp))
                BoardTetris(it)
            }
            ButtonsTetris { move ->
                game.move(
                    when (move) {
                        Move.Left -> Move.Left
                        Move.Right -> Move.Right
                        Move.Drop -> Move.Drop
                    }
                )
            }
        }
    }
}