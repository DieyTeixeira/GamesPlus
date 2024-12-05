package com.dieyteixeira.gamesplus.games.game_snake

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
fun GameSnake(
    navigateClick: Boolean
) {
    val color = DarkBlue

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val game = remember { Game(scope, context) }
    val state = game.state.collectAsState(initial = null)
    var playerName by remember { mutableStateOf("") }
    var playerColor by remember { mutableStateOf(Color.LightGray) }
    var showReturnSettingsSnake by remember { mutableStateOf(false) }
    var showEndGameSnake by remember { mutableStateOf(false) }

    var showSettings by remember { mutableStateOf(true) }

    val onStartGame: (SnakeState) -> Unit = { config ->
        showSettings = false
        game.resetGameSnake()
    }

    LaunchedEffect(navigateClick) {
        if (navigateClick) {
            game.pauseGameSnake()
        } else {
            game.pauseGameSnake()
        }
    }

    if (showReturnSettingsSnake) {
        ShowReturnSettingsSnake(
            onNo = { showReturnSettingsSnake = false },
            onYes = {
                showReturnSettingsSnake = false
                showSettings = true
                game.stopGameSnake()
            }
        )
    }

    if (showEndGameSnake) {
        val playerRecordSnake = getPlayerRecordSnake(context, "Snake")
        EndGameSnake(
            playerName = playerName,
            playerColor = playerColor,
            playerScore = state.value?.score ?: 0,
            recordName = playerRecordSnake.second.toString(),
            recordScore = playerRecordSnake.first,
            onNewGame = {
                showEndGameSnake = false
                showSettings = true
                game.stopGameSnake()
            },
            onRestartGame = {
                showEndGameSnake = false
                game.resetGameSnake()
            }
        )
    }

    LaunchedEffect(state.value?.isGameOver) {
        if (state.value?.isGameOver == true) {
            showEndGameSnake = true
            delay(500)
            savePlayerRecordSnake(
                context = context,
                playerName = playerName,
                gameScore = state.value?.score!!,
                gameName = "Snake"
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showSettings) {
            SettingsSnake(
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
                        .clickable { showReturnSettingsSnake = true }
                )
            }
            state.value?.let {
                ScoreSnake(
                    color = playerColor,
                    score = it.score,
                    speed = it.speed
                )
                Spacer(modifier = Modifier.size(2.dp))
                BoardSnake(
                    state = it,
                    color = playerColor
                )
            }
            ButtonsSnake {
                game.move = it
            }
        }
    }
}