package com.dieyteixeira.gamesplus.games.game_pacman

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dieyteixeira.gamesplus.games.game_pacman.PacmanGame.Companion.generateBestFood
import com.dieyteixeira.gamesplus.games.game_pacman.PacmanGame.Companion.generateEntry
import com.dieyteixeira.gamesplus.games.game_pacman.PacmanGame.Companion.generateFood
import com.dieyteixeira.gamesplus.games.game_pacman.PacmanGame.Companion.generateHome
import com.dieyteixeira.gamesplus.games.game_pacman.PacmanGame.Companion.generateWalls
import com.dieyteixeira.gamesplus.games.game_snake.GameSelectorSnake
import com.dieyteixeira.gamesplus.ui.theme.Yellow

@Composable
fun SettingsPacman(
    color: Color,
    playerName: String,
    playerColor: Color,
    onPlayerNameChange: (String) -> Unit,
    onPlayerColorChange: (Color) -> Unit,
    onStartGame: (PacmanState) -> Unit
) {
    val context = LocalContext.current
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Text(
            "CoNFIGuRAçõES Do JoGo",
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 20.sp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        GameSelectorSnake(
            playerName = playerName,
            playerColor = playerColor,
            onPlayerNameChange = onPlayerNameChange,
            onPlayerColorChange = onPlayerColorChange
        )

        Spacer(modifier = Modifier.height(25.dp))

        val validationResult =  {
            val isNameValid = playerName.isNotBlank()
            val isColorValid = playerColor != Color.LightGray // Verifique se uma cor foi escolhida
            Pair(
                isNameValid && isColorValid,
                when {
                    !isNameValid -> "Preencha o nome do jogador!"
                    !isColorValid -> "Escolha a cor do jogador!"
                    else -> ""
                }
            )
        }()

        val isValid = validationResult.first
        val errorMessage = validationResult.second

        Box(
            modifier = Modifier
                .width(100.dp)
                .height(35.dp)
                .background(color, shape = RoundedCornerShape(100))
                .clickable {
                    if (isValid) {
                        onStartGame(PacmanState(
                            pacman = Pair(12, 18),
                            food = generateFood(),
                            bestFood = generateBestFood(),
                            ghosts = listOf(Pair(0, 0), Pair(23, 0), Pair(0, 23), Pair(23, 23)),
                            walls = generateWalls(),
                            home = generateHome(),
                            entry = generateEntry()
                        ))
                    } else {
                        showError = true
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Iniciar Jogo",
                style = MaterialTheme.typography.displaySmall.copy(fontSize = 14.sp),
                color = if (color == Yellow) Color.Black else Color.White
            )
        }

        if (showError && !isValid) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            showError = false
        }
    }
}