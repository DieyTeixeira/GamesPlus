package com.dieyteixeira.gamesplus.games.game_tetris

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dieyteixeira.gamesplus.ui.theme.Blue
import com.dieyteixeira.gamesplus.ui.theme.DarkYellow
import com.dieyteixeira.gamesplus.ui.theme.Green
import com.dieyteixeira.gamesplus.ui.theme.Lime
import com.dieyteixeira.gamesplus.ui.theme.Purple
import com.dieyteixeira.gamesplus.ui.theme.Red
import com.dieyteixeira.gamesplus.ui.theme.Yellow

@Composable
fun SettingsTetris(
    color: Color,
    playerName: String,
    playerColor: Color,
    onPlayerNameChange: (String) -> Unit,
    onPlayerColorChange: (Color) -> Unit,
    onStartGame: (TetrisState) -> Unit
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

        GameSelectorTetris(
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
                        onStartGame(
                            TetrisState(
                                iniciar = true,
                                board = Array(20) { Array(10) { Green.copy(alpha = 0.05f) } },
                                currentTetromino = Tetromino(listOf(Pair(0, 0), Pair(1, 0), Pair(2, 0), Pair(3, 0)), DarkGreen1),
                                position = Pair(0, 5),
                                score = 0
                            )
                        )
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

@Composable
fun GameSelectorTetris(
    playerName: String,
    playerColor: Color,
    onPlayerNameChange: (String) -> Unit,
    onPlayerColorChange: (Color) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.width(150.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Jogador",
                style = MaterialTheme.typography.displaySmall.copy(fontSize = 18.sp),
                modifier = Modifier.padding(bottom = 5.dp)
            )
            NameAndColorTetris(
                name = playerName,
                color = playerColor,
                onNameChange = onPlayerNameChange,
                onColorChange = onPlayerColorChange,
                unavailableColors = emptyList()
            )
        }
    }
}

@Composable
fun NameAndColorTetris(
    name: String,
    color: Color,
    onNameChange: (String) -> Unit,
    onColorChange: (Color) -> Unit,
    unavailableColors: List<Color>
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Nome do jogador
        Box(
            modifier = Modifier
                .height(34.dp)
                .border(1.dp, color, RoundedCornerShape(5.dp))
                .padding(horizontal = 10.dp, vertical = 7.dp)
                .fillMaxWidth()
        ) {
            BasicTextField(
                value = name,
                onValueChange = onNameChange,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.Black,
                    fontSize = 16.sp
                ),
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Seletor de cor
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            val listOfColors = listOf(
                Blue,
                Purple,
                Red,
                DarkYellow,
                Lime,
                Green
            )
            listOfColors.forEach { selectableColor ->
                val isUnavailable = selectableColor in unavailableColors

                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .background(Color.White, shape = RoundedCornerShape(20.dp))
                        .border(
                            width = if (selectableColor == color) 1.dp else 0.dp,
                            color = if (selectableColor == color) selectableColor else Color.Transparent,
                            shape = RoundedCornerShape(20.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(17.dp)
                            .background(if (isUnavailable) selectableColor.copy(alpha = 0.5f) else selectableColor, shape = RoundedCornerShape(20.dp))
                            .clickable(enabled = !isUnavailable) { onColorChange(selectableColor) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectableColor == color) {
                            Image(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Check",
                                colorFilter = ColorFilter.tint(Color.White),
                                modifier = Modifier.size(14.dp)
                            )
                        } else if (isUnavailable) {
                            Image(
                                imageVector = Icons.Default.Block,
                                contentDescription = "Block",
                                colorFilter = ColorFilter.tint(Color.White),
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}