package com.dieyteixeira.gamesplus.games.game_memory

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dieyteixeira.gamesplus.R
import com.dieyteixeira.gamesplus.ui.theme.DarkBlue
import kotlinx.coroutines.delay

@Composable
fun GameMemory() {
    val color = DarkBlue

    var gridSize by remember { mutableStateOf(GridSize(4, 3)) }
    var gameMode by remember { mutableStateOf(GameMode.OnePlayer) }
    var player1Name by remember { mutableStateOf("") }
    var player2Name by remember { mutableStateOf("") }
    var Color_Player_1 by remember { mutableStateOf(Color.LightGray) }
    var Color_Player_2 by remember { mutableStateOf(Color.LightGray) }
    var showSettings by remember { mutableStateOf(true) }

    val onStartGame: (GameConfig) -> Unit = { config ->
        showSettings = false
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (showSettings) {
                SettingsMemory(
                    color = color,
                    gridSize = gridSize,
                    gameMode = gameMode,
                    onGridSizeSelected = { selectedSize -> gridSize = selectedSize },
                    onGameModeSelected = { selectedMode -> gameMode = selectedMode },
                    player1Name = player1Name,
                    player2Name = player2Name,
                    player1Color = Color_Player_1,
                    player2Color = Color_Player_2,
                    onPlayer1NameChange = { player1Name = it },
                    onPlayer2NameChange = { player2Name = it },
                    onPlayer1ColorChange = { Color_Player_1 = it },
                    onPlayer2ColorChange = { Color_Player_2 = it },
                    onStartGame = onStartGame
                )
            } else {
                MemoryGame(
                    config = GameConfig(gridSize, gameMode),
                    isTwoPlayers = gameMode == GameMode.TwoPlayers,
                    player1Name = player1Name,
                    player2Name = player2Name,
                    player1Color = Color_Player_1,
                    player2Color = Color_Player_2,
                    onNewGame = { showSettings = true }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MemoryGame(
    config: GameConfig,
    isTwoPlayers: Boolean,
    player1Name: String,
    player2Name: String,
    player1Color: Color,
    player2Color: Color,
    onNewGame: () -> Unit
) {
    val context = LocalContext.current
    var isWaitingForFlip by remember { mutableStateOf(false) }
    var showEndOneGameDialog by remember { mutableStateOf(false) }
    var showEndTwoGameDialog by remember { mutableStateOf(false) }
    var showClearRecordDialog by remember { mutableStateOf(false) }
    var showRestartGameDialog by remember { mutableStateOf(false) }
    var showReturnSettingsDialog by remember { mutableStateOf(false) }

    var winnerName by remember { mutableStateOf("") }
    var winnerScore by remember { mutableStateOf(0) }
    var winnerColor by remember { mutableStateOf(Color.White) }
    var loserName by remember { mutableStateOf("") }
    var loserScore by remember { mutableStateOf(0) }
    var loserColor by remember { mutableStateOf(Color.White) }
    var isDraw by remember { mutableStateOf(false) }
    var gridSize by remember { mutableStateOf(config.gridSize) }
    var gridSizeAtual by remember { mutableStateOf(GridSize(4, 3)) }
    var levelAtual by remember { mutableStateOf(1) }

    var gameState by remember {
        mutableStateOf(
            MemoryGameState(
                grid = generateGrid(config.gridSize),
                revealed = MutableList(config.gridSize.rows * config.gridSize.columns) { false },
                matched = MutableList(config.gridSize.rows * config.gridSize.columns) { false },
                firstChoice = null,
                secondChoice = null,
                movesPlayer = 0,
                currentPlayer = 1,
                player1Name = player1Name,
                player2Name = player2Name,
                rows = config.gridSize.rows,
                columns = config.gridSize.columns,
                matchedPairs = mutableMapOf()
            )
        )
    }

    if (showEndOneGameDialog) {
        EndOneGameDialog(
            winnerName = winnerName,
            gridSize = gridSizeAtual,
            onDismiss = {
                showEndOneGameDialog = false
            },
            onNewGame = {
                showEndOneGameDialog = false
                onNewGame()
            },
            onNextLevel = {
                Log.d("GameState", "GridSize inicial: ${config.gridSize}")
                Log.d("GameState", "Nível atual: $gridSizeAtual")

                val nextGridSize = when (gridSizeAtual) {
                    GridSize(4, 3) -> GridSize(4, 4) // 1 -> 2
                    GridSize(4, 4) -> GridSize(5, 4) // 2 -> 3
                    GridSize(5, 4) -> GridSize(6, 4) // 3 -> 4
                    GridSize(6, 4) -> GridSize(5, 5) // 4 -> 5
                    GridSize(5, 5) -> GridSize(6, 5) // 5 -> 6
                    GridSize(6, 5) -> GridSize(6, 6) // 6 -> 7
                    GridSize(6, 6) -> GridSize(7, 6) // 7 -> 8
                    GridSize(7, 6) -> GridSize(8, 6) // 8 -> 9
                    GridSize(8, 6) -> GridSize(9, 6) // 9 -> 10
                    else -> gridSizeAtual
                }

                val level = when (nextGridSize) {
                    GridSize(4, 3) -> 1
                    GridSize(4, 4) -> 2
                    GridSize(5, 4) -> 3
                    GridSize(6, 4) -> 4
                    GridSize(5, 5) -> 5
                    GridSize(6, 5) -> 6
                    GridSize(6, 6) -> 7
                    GridSize(7, 6) -> 8
                    GridSize(8, 6) -> 9
                    else -> 10
                }

                Log.d("GameState", "Atualizando para o próximo gridSize: $nextGridSize")

                gameState = gameState.copy(
                    grid = generateGrid(nextGridSize),
                    revealed = MutableList(nextGridSize.rows * nextGridSize.columns) { false },
                    matched = MutableList(nextGridSize.rows * nextGridSize.columns) { false },
                    firstChoice = null,
                    secondChoice = null,
                    movesPlayer = 0,
                    currentPlayer = 3,
                    player1Name = player1Name,
                    player2Name = player2Name,
                    rows = nextGridSize.rows,
                    columns = nextGridSize.columns,
                    matchedPairs = mutableMapOf()
                )

                GameMode.OnePlayer

                gridSizeAtual = nextGridSize
                levelAtual = level

                Log.d("GameState", "GridSize atualizada para: $gridSizeAtual")
            }
        )

        Log.d("GameState", "ShowEndOneGameDialog: $showEndOneGameDialog")
    }

    if (showEndTwoGameDialog) {
        EndTwoGameDialog(
            winnerName = winnerName,
            winnerScore = winnerScore,
            winnerColor = winnerColor,
            loserName = loserName,
            loserScore = loserScore,
            loserColor = loserColor,
            isDraw = isDraw,
            onNewGame = {
                showEndTwoGameDialog = false
                onNewGame()
            },
            onRestartGame = {
                showEndTwoGameDialog = false
                gameState = gameState.copy(
                    grid = generateGrid(config.gridSize),
                    revealed = MutableList(config.gridSize.rows * config.gridSize.columns) { false },
                    matched = MutableList(config.gridSize.rows * config.gridSize.columns) { false },
                    firstChoice = null,
                    secondChoice = null,
                    movesPlayer = 0,
                    currentPlayer = 1,
                    player1Name = player1Name,
                    player2Name = player2Name,
                    rows = config.gridSize.rows,
                    columns = config.gridSize.columns,
                    matchedPairs = mutableMapOf()
                )
            }
        )
    }

    if (showClearRecordDialog) {
        ShowClearRecordDialog(
            onNo = { showClearRecordDialog = false },
            onYes = {
                showClearRecordDialog = false

                clearOnePlayerRecord(context, "${gridSizeAtual.rows}x${gridSizeAtual.columns}", "Memoria")
                Toast.makeText(context, "Recorde zerado!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    if (showReturnSettingsDialog) {
        ShowReturnSettingsMemory(
            onNo = { showReturnSettingsDialog = false },
            onYes = {
                showReturnSettingsDialog = false
                onNewGame()
            }
        )
    }

    if (showRestartGameDialog) {
        ShowRestartGameDialog(
            onNo = { showRestartGameDialog = false },
            onYes = {
                showRestartGameDialog = false
                gameState = gameState.copy(
                    grid = generateGrid(config.gridSize),
                    revealed = MutableList(config.gridSize.rows * config.gridSize.columns) { false },
                    matched = MutableList(config.gridSize.rows * config.gridSize.columns) { false },
                    firstChoice = null,
                    secondChoice = null,
                    movesPlayer = 0,
                    currentPlayer = 1,
                    player1Name = player1Name,
                    player2Name = player2Name,
                    rows = config.gridSize.rows,
                    columns = config.gridSize.columns,
                    matchedPairs = mutableMapOf()
                )
            }
        )
    }

    LaunchedEffect(gameState.firstChoice, gameState.secondChoice) {

        if (gameState.firstChoice != null && gameState.secondChoice != null) {
            val first = gameState.firstChoice!!
            val second = gameState.secondChoice!!

            if (gameState.grid[first] == gameState.grid[second]) {
                isWaitingForFlip = true
                delay(400)

                val newMatched = gameState.matched.toMutableList()
                newMatched[first] = true
                newMatched[second] = true

                val newMatchedPairs = gameState.matchedPairs.toMutableMap()
                newMatchedPairs[first] = gameState.currentPlayer
                newMatchedPairs[second] = gameState.currentPlayer

                gameState = gameState.copy(
                    matched = newMatched,
                    matchedPairs = newMatchedPairs
                )

                isWaitingForFlip = false
            } else {
                isWaitingForFlip = true
                delay(800)
                val newRevealed = gameState.revealed.toMutableList()
                newRevealed[first] = false
                newRevealed[second] = false

                gameState = gameState.copy(revealed = newRevealed)

                if (isTwoPlayers) {
                    gameState = gameState.copy(currentPlayer = if (gameState.currentPlayer == 1) 2 else 1)
                } else {
                    gameState = gameState.copy(currentPlayer = 3)
                }

                isWaitingForFlip = false
            }

            gameState = gameState.copy(firstChoice = null, secondChoice = null)

            if (!isTwoPlayers && gameState.currentPlayer == 3) {
                gameState = gameState.copy(movesPlayer = gameState.movesPlayer + 1)
            }
        }
    }

    LaunchedEffect(gameState.matched) {
        if (gameState.matched.all { it }) {
            val player1Score = countPairsByPlayer(gameState.matchedPairs, 1)
            val player2Score = countPairsByPlayer(gameState.matchedPairs, 2)

            delay(1000)
            if (isTwoPlayers) {
                if (player1Score > player2Score) {
                    winnerName = gameState.player1Name
                    winnerScore = player1Score
                    winnerColor = player1Color
                    loserName = gameState.player2Name
                    loserScore = player2Score
                    loserColor = player2Color

                    saveTwoPlayersVictory(
                        context,
                        gameState.player1Name,
                        gameState.player2Name,
                        "Memoria"
                    )
                } else if (player2Score > player1Score) {
                    winnerName = gameState.player2Name
                    winnerScore = player2Score
                    winnerColor = player2Color
                    loserName = gameState.player1Name
                    loserScore = player1Score
                    loserColor = player1Color

                    saveTwoPlayersVictory(
                        context,
                        gameState.player2Name,
                        gameState.player1Name,
                        "Memoria"
                    )
                } else {
                    isDraw = true
                    winnerScore = player1Score
                }

                showEndTwoGameDialog = true
            } else {
                winnerName = gameState.player1Name
                gridSize = gridSizeAtual

                saveOnePlayerRecord(
                    context,
                    gameState.player1Name,
                    gameState.movesPlayer,
                    "${gridSizeAtual.rows}x${gridSizeAtual.columns}",
                    "Memoria"
                )

                showEndOneGameDialog = true
            }
        }
    }

    val player1Victories = getTwoPlayersVictories(context, player1Name, player2Name, "Memoria")
    val player2Victories = getTwoPlayersVictories(context, player2Name, player1Name, "Memoria")
    val player1Record = getOnePlayerRecord(context, "${gridSizeAtual.rows}x${gridSizeAtual.columns}", "Memoria")
    val scale1 by animateFloatAsState(
        targetValue = if (gameState.currentPlayer == 1) 1f else 0.8f,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
    )
    val scale2 by animateFloatAsState(
        targetValue = if (gameState.currentPlayer == 1) 0.8f else 1f,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
    )

    // Desenho do tabuleiro
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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
            if (isTwoPlayers) {
                Spacer(modifier = Modifier.width(25.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_restart),
                    contentDescription = "Reiniciar",
                    colorFilter = ColorFilter.tint(Color.Gray),
                    modifier = Modifier
                        .size(23.dp)
                        .clickable { showRestartGameDialog = true }
                )
            }
        }
        if (isTwoPlayers) {
            Column (
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Placar total",
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 10.sp),
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    modifier = Modifier
                        .width(150.dp)
                        .height(30.dp)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .width(75.dp)
                            .height(30.dp)
                            .background(
                                color = player1Color,
                                shape = CutCornerShape(0, 50, 50, 0)
                            )
                            .padding(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$player1Victories",
                            style = MaterialTheme.typography.displaySmall.copy(fontSize = 22.sp),
                            color = Color.White
                        )
                    }
                    Row(
                        modifier = Modifier
                            .width(75.dp)
                            .height(30.dp)
                            .background(
                                color = player2Color,
                                shape = CutCornerShape(50, 0, 0, 50)
                            )
                            .padding(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$player2Victories",
                            style = MaterialTheme.typography.displaySmall.copy(fontSize = 22.sp),
                            color = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(150.dp)
                            .height(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier
                                .width(150.dp)
                                .height(40.dp)
                                .scale(scale1)
                                .background(
                                    color = if (gameState.currentPlayer == 1) player1Color else player1Color.copy(
                                        alpha = 0.5f
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                ),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = player1Name,
                                    style = MaterialTheme.typography.displaySmall.copy(fontSize = 18.sp),
                                    color = Color.White
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(40.dp)
                                    .padding(2.dp)
                                    .background(
                                        Color.White,
                                        shape = RoundedCornerShape(0.dp, 8.dp, 8.dp, 0.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${countPairsByPlayer(gameState.matchedPairs, 1)}",
                                    style = MaterialTheme.typography.displaySmall.copy(fontSize = 20.sp),
                                    color = if (gameState.currentPlayer == 1) player1Color else player1Color.copy(
                                        alpha = 0.5f
                                    )
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(5.dp))

                    FlipPlayerMemory(
                        flipCard = if (gameState.currentPlayer == 1) FlipCardMemory.Previous else FlipCardMemory.Forward,
                    )

                    Spacer(modifier = Modifier.width(5.dp))
                    Box(
                        modifier = Modifier
                            .width(150.dp)
                            .height(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier
                                .width(150.dp)
                                .height(40.dp)
                                .scale(scale2)
                                .background(
                                    color = if (gameState.currentPlayer == 2) player2Color else player2Color.copy(
                                        alpha = 0.5f
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                ),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(40.dp)
                                    .padding(2.dp)
                                    .background(
                                        Color.White,
                                        shape = RoundedCornerShape(8.dp, 0.dp, 0.dp, 8.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${countPairsByPlayer(gameState.matchedPairs, 2)}",
                                    style = MaterialTheme.typography.displaySmall.copy(fontSize = 20.sp),
                                    color = if (gameState.currentPlayer == 2) player2Color else player2Color.copy(
                                        alpha = 0.5f
                                    )
                                )
                            }
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = player2Name,
                                    style = MaterialTheme.typography.displaySmall.copy(fontSize = 18.sp),
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {}
                Spacer(modifier = Modifier.width(5.dp))
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Recorde",
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 10.sp),
                        color = Color.DarkGray
                    )
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.95f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .background(
                            color = player1Color,
                            shape = RoundedCornerShape(10.dp)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Nível",
                        style = MaterialTheme.typography.displaySmall.copy(fontSize = 12.sp),
                        color = Color.White,
                        modifier = Modifier
                            .height(20.dp)
                            .padding(3.dp)
                    )
                    Row(
                        modifier = Modifier
                            .height(40.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "$levelAtual",
                                style = MaterialTheme.typography.displaySmall.copy(fontSize = 25.sp),
                                color = Color.White
                            )
                            Text(
                                text = "/10",
                                style = MaterialTheme.typography.displaySmall.copy(fontSize = 15.sp),
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(5.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .background(
                            color = player1Color,
                            shape = RoundedCornerShape(10.dp)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Grid",
                        style = MaterialTheme.typography.displaySmall.copy(fontSize = 12.sp),
                        color = Color.White,
                        modifier = Modifier
                            .height(20.dp)
                            .padding(3.dp)
                    )
                    Text(
                        text = "${gridSizeAtual.rows}x${gridSizeAtual.columns}",
                        style = MaterialTheme.typography.displaySmall.copy(fontSize = 25.sp),
                        color = Color.White,
                        modifier = Modifier.height(40.dp)
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                Row(
                    modifier = Modifier
                        .weight(2f)
                        .height(60.dp)
                        .background(
                            color = player1Color.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable { showClearRecordDialog = true },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(
                        modifier = Modifier
                            .weight(3f)
                            .height(60.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Movim.",
                            style = MaterialTheme.typography.displaySmall.copy(fontSize = 12.sp),
                            color = Color.White,
                            modifier = Modifier
                                .height(20.dp)
                                .padding(3.dp)
                        )
                        Text(
                            text = "${player1Record.first}",
                            style = MaterialTheme.typography.displaySmall.copy(fontSize = 25.sp),
                            color = Color.White,
                            modifier = Modifier.height(40.dp)
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(4f)
                            .height(60.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Jogador",
                            style = MaterialTheme.typography.displaySmall.copy(fontSize = 12.sp),
                            color = Color.White,
                            modifier = Modifier
                                .height(20.dp)
                                .padding(3.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .padding(top = 4.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Text(
                                text = "${player1Record.second}",
                                style = MaterialTheme.typography.displaySmall.copy(fontSize = 18.sp),
                                color = Color.White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Movimentos: ${gameState.movesPlayer}",
                    style = MaterialTheme.typography.displaySmall.copy(fontSize = 20.sp),
                    color = Color.DarkGray
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        val cardSize = calculateCardSize(gameState.rows, gameState.columns)

        if (showEndOneGameDialog || showEndTwoGameDialog || showRestartGameDialog) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {}
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                GridBoard(gameState, cardSize, player1Color, player2Color) { index ->
                    if (!gameState.revealed[index] && !gameState.matched[index] && !isWaitingForFlip) {
                        val newRevealed = gameState.revealed.toMutableList()
                        newRevealed[index] = true

                        if (gameState.firstChoice == null) {
                            gameState = gameState.copy(firstChoice = index, revealed = newRevealed)
                        } else {
                            gameState = gameState.copy(secondChoice = index, revealed = newRevealed)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GridBoard(
    gameState: MemoryGameState,
    cardSize: Dp,
    player1Color: Color,
    player2Color: Color,
    onItemClick: (Int) -> Unit
) {

    val fontSize = with(LocalDensity.current) {
        (cardSize.toPx() * 0.75f).toSp()
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        for (row in 0 until gameState.rows) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                for (col in 0 until gameState.columns) {
                    val index = row * gameState.columns + col
                    var showExplosion by remember { mutableStateOf(false) }
                    LaunchedEffect(gameState.matched[index]) {
                        if (gameState.matched[index]) {
                            delay(350)
                            showExplosion = true
                        }
                    }

                    FlipRotateMemory(
                        flipCard = if (gameState.revealed[index] || gameState.matched[index]) FlipCardMemory.Previous else FlipCardMemory.Forward,
                        player1Color = player1Color,
                        player2Color = player2Color,
                        onClick = { onItemClick(index) },
                        isMatched = gameState.matched[index],
                        matchPlayer = gameState.matchedPairs[index] ?: 0,
                        modifier = Modifier
                            .padding(4.dp)
                            .size(cardSize),
                        forward = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Gray),
                                contentAlignment = Alignment.Center
                            ) {}
                        },
                        previous = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        if (showExplosion) {
                                            when (gameState.matchedPairs[index]) {
                                                1 -> player1Color
                                                2 -> player2Color
                                                3 -> player1Color
                                                else -> Color.LightGray
                                            }
                                        } else Color.LightGray
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = gameState.grid[index].toString(),
                                    style = MaterialTheme.typography.titleLarge.copy(fontSize = fontSize),
                                    color = if (gameState.matched[index]) Color.White else Color.Black
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}