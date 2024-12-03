package com.dieyteixeira.gamesplus.games.game_snake

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.sp
import com.dieyteixeira.gamesplus.R
import com.dieyteixeira.gamesplus.games.game_memory.EndTwoGameDialog
import com.dieyteixeira.gamesplus.games.game_memory.ShowReturnSettingsDialog
import com.dieyteixeira.gamesplus.games.game_memory.clearOnePlayerRecord
import com.dieyteixeira.gamesplus.games.game_memory.generateGrid
import com.dieyteixeira.gamesplus.games.game_memory.getTwoPlayersVictories
import com.dieyteixeira.gamesplus.ui.theme.DarkBlue
import com.dieyteixeira.gamesplus.ui.theme.GreenComp
import com.dieyteixeira.gamesplus.ui.theme.GreenScreen

@Composable
fun GameSnake() {
    val color = DarkBlue

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val game = remember { Game(scope, context) }
    val state = game.state.collectAsState(initial = null)
    var playerName by remember { mutableStateOf("") }
    var playerColor by remember { mutableStateOf(Color.LightGray) }
    var showClearRecordSnake by remember { mutableStateOf(false) }
    var showReturnSettingsDialog by remember { mutableStateOf(false) }
    var showEndGameSnake by remember { mutableStateOf(false) }

    var showSettings by remember { mutableStateOf(true) }

    val onStartGame: (State) -> Unit = { config ->
        showSettings = false
    }

    if (showClearRecordSnake) {
        ShowClearRecordSnake(
            onNo = { showClearRecordSnake = false },
            onYes = {
                showClearRecordSnake = false

                clearPlayerRecordSnake(
                    context,
                    "Snake"
                )
                Toast.makeText(context, "Recorde zerado!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    if (showReturnSettingsDialog) {
        ShowReturnSettingsDialog(
            onNo = { showReturnSettingsDialog = false },
            onYes = {
                showReturnSettingsDialog = false
                showSettings = true
            }
        )
    }

    if (showEndGameSnake) {
        EndGameSnake(
            playerName = playerName,
            playerColor = playerColor,
            playerScore = state.value?.score ?: 0,
            onNewGame = {
                showEndGameSnake = false
                game.reset()
                showSettings = true
            },
            onRestartGame = {
                showEndGameSnake = false
                game.reset()
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
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
                    .padding(10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_new_game),
                    contentDescription = "Novo Jogo",
                    colorFilter = ColorFilter.tint(Color.Gray),
                    modifier = Modifier
                        .size(23.dp)
                        .clickable { showReturnSettingsDialog = true }
                )
            }
            state.value?.let {
                if (it.isGameOver) {

                    savePlayerRecordSnake(
                        context = context,
                        playerName = playerName,
                        gameScore = it.score,
                        gameName = "Snake"
                    )

                    showEndGameSnake = true

//                    GameOverSnake(score = it.score, highScore = game.highScoreSnake) {
//                        game.reset()
//                    }

//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(30.dp)
//                            .background(GreenScreen),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Text(
//                            text = "",
//                            style = MaterialTheme.typography.displayMedium.copy(
//                                fontSize = 20.sp
//                            ),
//                            color = GreenComp
//                        )
//                    }
                } else {
                    ScoreSnake(
                        color = playerColor,
                        score = it.score,
                        speed = it.speed,
                        onClearRecord = { showClearRecordSnake = true }
                    )
                    BoardSnake(it)
                }
            }
            Spacer(modifier = Modifier.size(15.dp))
            ButtonsSnake {
                game.move = it
            }
        }
    }
}