package com.dieyteixeira.gamesplus.games.game_memory

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
import com.dieyteixeira.gamesplus.ui.theme.Gray
import com.dieyteixeira.gamesplus.ui.theme.Green
import com.dieyteixeira.gamesplus.ui.theme.Lime
import com.dieyteixeira.gamesplus.ui.theme.Purple
import com.dieyteixeira.gamesplus.ui.theme.Red
import com.dieyteixeira.gamesplus.ui.theme.Yellow

@Composable
fun SettingsMemory(
    color: Color,
    gridSize: GridSize,
    gameMode: GameMode,
    onGridSizeSelected: (GridSize) -> Unit,
    onGameModeSelected: (GameMode) -> Unit,
    player1Name: String,
    player2Name: String,
    player1Color: Color,
    player2Color: Color,
    onPlayer1NameChange: (String) -> Unit,
    onPlayer2NameChange: (String) -> Unit,
    onPlayer1ColorChange: (Color) -> Unit,
    onPlayer2ColorChange: (Color) -> Unit,
    onStartGame: (GameConfig) -> Unit
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

        GameModeSelector(
            selectedMode = gameMode,
            onModeSelected = {
                onGameModeSelected(it)
                if (it == GameMode.OnePlayer) {
                    onGridSizeSelected(GridSize(4, 3))
                }
            },
            player1Name = player1Name,
            player2Name = player2Name,
            player1Color = player1Color,
            player2Color = player2Color,
            onPlayer1NameChange = onPlayer1NameChange,
            onPlayer2NameChange = onPlayer2NameChange,
            onPlayer1ColorChange = onPlayer1ColorChange,
            onPlayer2ColorChange = onPlayer2ColorChange
        )

        Spacer(modifier = Modifier.height(16.dp))

        GridSizeSelector(
            selectedSize = gridSize,
            isSelectable = gameMode != GameMode.OnePlayer,
            onSizeSelected = onGridSizeSelected
        )

        Spacer(modifier = Modifier.height(25.dp))

        val validationResult = when (gameMode) {
            GameMode.OnePlayer -> {
                val isNameValid = player1Name.isNotBlank()
                val isColorValid = player1Color != Color.LightGray // Verifique se uma cor foi escolhida
                Pair(
                    isNameValid && isColorValid,
                    when {
                        !isNameValid -> "Preencha o nome do jogador!"
                        !isColorValid -> "Escolha a cor do jogador!"
                        else -> ""
                    }
                )
            }
            GameMode.TwoPlayers -> {
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
            }
        }

        val isNameValid = validationResult.first
        val textToast = validationResult.second

        Box(
            modifier = Modifier
                .width(100.dp)
                .height(35.dp)
                .background(color, shape = RoundedCornerShape(100))
                .clickable {
                    if (isNameValid) {
                        onStartGame(GameConfig(gridSize, gameMode))
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

        if (showError && !isNameValid) {
            Toast.makeText(context, textToast, Toast.LENGTH_SHORT).show()
            showError = false
        }
    }
}

@Composable
fun GridSizeSelector(
    selectedSize: GridSize,
    isSelectable: Boolean,
    onSizeSelected: (GridSize) -> Unit
) {
    val sizes = listOf(
        GridSize(4, 3), // 12
        GridSize(4, 4), // 16
        GridSize(5, 4), // 20
        GridSize(6, 4), // 24
        GridSize(5, 5), // 25
        GridSize(6, 5), // 30
        GridSize(6, 6), // 36
        GridSize(7, 6), // 42
        GridSize(8, 6), // 48
        GridSize(9, 6)  // 54
    )

    Text(
        "Tamanho do jogo:",
        style = MaterialTheme.typography.displaySmall.copy(fontSize = 18.sp)
    )

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            sizes.take(5).forEach { size ->
                val grid = "Grid ${size.rows}x${size.columns}"
                Box(
                    modifier = Modifier
                        .width(130.dp)
                        .height(30.dp)
                        .background(if (size == selectedSize) Gray else Color.LightGray, shape = RoundedCornerShape(10.dp))
                        .let {
                            if (isSelectable) it.clickable { onSizeSelected(size) } else it
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = grid,
                        style = MaterialTheme.typography.displaySmall.copy(fontSize = 16.sp),
                        color = if (size == selectedSize) Color.White else (if (isSelectable) Color.Black else Color.Gray)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            sizes.drop(5).forEach { size ->
                val grid = "Grid ${size.rows}x${size.columns}"
                Box(
                    modifier = Modifier
                        .width(130.dp)
                        .height(30.dp)
                        .background(if (size == selectedSize) Gray else Color.LightGray, shape = RoundedCornerShape(10.dp))
                        .let {
                            if (isSelectable) it.clickable { onSizeSelected(size) } else it
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = grid,
                        style = MaterialTheme.typography.displaySmall.copy(fontSize = 16.sp),
                        color = if (size == selectedSize) Color.White else (if (isSelectable) Color.Black else Color.Gray)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun GameModeSelector(
    selectedMode: GameMode,
    onModeSelected: (GameMode) -> Unit,
    player1Name: String,
    player2Name: String,
    player1Color: Color,
    player2Color: Color,
    onPlayer1NameChange: (String) -> Unit,
    onPlayer2NameChange: (String) -> Unit,
    onPlayer1ColorChange: (Color) -> Unit,
    onPlayer2ColorChange: (Color) -> Unit
) {
    Text(
        "Modo de jogo:",
        style = MaterialTheme.typography.displaySmall.copy(fontSize = 18.sp)
    )

    Spacer(modifier = Modifier.height(8.dp))

    if (selectedMode == GameMode.OnePlayer) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GameMode.entries.forEach { mode ->
                Box(
                    modifier = Modifier
                        .width(130.dp)
                        .height(30.dp)
                        .background(if (mode == selectedMode) Gray else Color.LightGray, shape = RoundedCornerShape(10.dp))
                        .clickable{onModeSelected(mode)},
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = mode.displayName,
                        style = MaterialTheme.typography.displaySmall.copy(fontSize = 16.sp),
                        color = if (mode == selectedMode) Color.White else Color.Black
                    )
                }
            }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GameMode.entries.forEach { mode ->
                Box(
                    modifier = Modifier
                        .width(130.dp)
                        .height(30.dp)
                        .background(if (mode == selectedMode) Gray else Color.LightGray, shape = RoundedCornerShape(10.dp))
                        .clickable{onModeSelected(mode)},
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = mode.displayName,
                        style = MaterialTheme.typography.displaySmall.copy(fontSize = 16.sp),
                        color = if (mode == selectedMode) Color.White else Color.Black
                    )
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(5.dp))

    if (selectedMode == GameMode.TwoPlayers) {
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
                NameAndColorSelector(
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
                NameAndColorSelector(
                    name = player2Name,
                    color = player2Color,
                    onNameChange = onPlayer2NameChange,
                    onColorChange = onPlayer2ColorChange,
                    unavailableColors = listOf(player1Color)
                )
            }
        }
    } else if (selectedMode == GameMode.OnePlayer) {
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
                NameAndColorSelector(
                    name = player1Name,
                    color = player1Color,
                    onNameChange = onPlayer1NameChange,
                    onColorChange = onPlayer1ColorChange,
                    unavailableColors = emptyList()
                )
            }
        }
    }
}

@Composable
fun NameAndColorSelector(
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