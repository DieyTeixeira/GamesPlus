package com.dieyteixeira.gamesplus.games.game_velha

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
fun SettingsVelha(
    color: Color,
    player1Name: String,
    player2Name: String,
    player1Color: Color,
    player2Color: Color,
    onPlayer1NameChange: (String) -> Unit,
    onPlayer2NameChange: (String) -> Unit,
    onPlayer1ColorChange: (Color) -> Unit,
    onPlayer2ColorChange: (Color) -> Unit,
    onStartGame: (VelhaState) -> Unit
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

        GameSelectorVelha(
            player1Name = player1Name,
            player2Name = player2Name,
            player1Color = player1Color,
            player2Color = player2Color,
            onPlayer1NameChange = onPlayer1NameChange,
            onPlayer2NameChange = onPlayer2NameChange,
            onPlayer1ColorChange = onPlayer1ColorChange,
            onPlayer2ColorChange = onPlayer2ColorChange
        )

        Spacer(modifier = Modifier.height(25.dp))

        val validationResult =  {
            val isNameValid = player1Name.isNotBlank() && player2Name.isNotBlank()
            val isColorValid = player1Color != Color.LightGray && player2Color != Color.LightGray
            Pair(
                isNameValid && isColorValid,
                when {
                    !isNameValid -> "Preencha os nomes dos jogadores!"
                    !isColorValid -> "Escolha as cores dos jogadores!"
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
                        onStartGame(VelhaState())
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
fun GameSelectorVelha(
    player1Name: String,
    player2Name: String,
    player1Color: Color,
    player2Color: Color,
    onPlayer1NameChange: (String) -> Unit,
    onPlayer2NameChange: (String) -> Unit,
    onPlayer1ColorChange: (Color) -> Unit,
    onPlayer2ColorChange: (Color) -> Unit
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
                "Jogador 1  ",
                style = MaterialTheme.typography.displaySmall.copy(fontSize = 18.sp),
                modifier = Modifier.padding(bottom = 5.dp)
            )
            NameAndColorVelha(
                name = player1Name,
                color = player1Color,
                onNameChange = onPlayer1NameChange,
                onColorChange = onPlayer1ColorChange,
                unavailableColors = listOf(player2Color)
            )
        }
        Spacer(modifier = Modifier.width(25.dp))
        Column(
            modifier = Modifier.width(150.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Jogador 2  ",
                style = MaterialTheme.typography.displaySmall.copy(fontSize = 18.sp),
                modifier = Modifier.padding(bottom = 5.dp)
            )
            NameAndColorVelha(
                name = player2Name,
                color = player2Color,
                onNameChange = onPlayer2NameChange,
                onColorChange = onPlayer2ColorChange,
                unavailableColors = listOf(player1Color)
            )
        }
    }
}

@Composable
fun NameAndColorVelha(
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