package com.dieyteixeira.gamesplus.ui.screen

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dieyteixeira.gamesplus.R
import com.dieyteixeira.gamesplus.games.game_memory.GameMemory
import com.dieyteixeira.gamesplus.games.game_memory.clickable
import com.dieyteixeira.gamesplus.games.game_pacman.GamePacman
import com.dieyteixeira.gamesplus.games.game_snake.GameSnake
import com.dieyteixeira.gamesplus.games.game_sudoku.GameSudoku
import com.dieyteixeira.gamesplus.games.game_tetris.GameTetris
import com.dieyteixeira.gamesplus.games.game_velha.GameVelha
import com.dieyteixeira.gamesplus.ui.theme.Blue
import com.dieyteixeira.gamesplus.ui.theme.DarkBlue

@Composable
fun AppScreen() {
    val colorList = Blue
    val colorIntern = DarkBlue

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorList
    ) {
        var currentIndex by remember { mutableStateOf(0) }
        var navigateClick by remember { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxSize()) {
            NavigationDrawer(
                color = colorList,
                onItemClicked = { index ->
                    currentIndex = index
                    navigateClick = false
                }
            )
            BodyContent(
                currentIndex = currentIndex,
                colorIntern = colorIntern,
                navigateClick = navigateClick,
                onMenuToggle = {
                    navigateClick = !navigateClick
                }
            )
        }
    }
}

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun BodyContent(
    currentIndex: Int,
    colorIntern: Color,
    navigateClick: Boolean = false,
    onMenuToggle: () -> Unit
) {

    val offSetAnim by animateDpAsState(targetValue = if (navigateClick) 300.dp else 0.dp)
    val scaleAnim by animateFloatAsState(targetValue = if (navigateClick) 0.6f else 1.0f)
    val clipAnim by animateDpAsState(targetValue = if (navigateClick) 25.dp else 0.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .scale(scaleAnim)
            .offset(x = offSetAnim)
            .clip(RoundedCornerShape(clipAnim))
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(colorIntern)
                    .padding(15.dp, 0.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_double_arrow_left),
                        contentDescription = "Lista",
                        modifier = Modifier
                            .size(23.dp)
                            .clickable { onMenuToggle() }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        textsGames[currentIndex],
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 30.sp),
                        color = Color.White
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White, shape = RoundedCornerShape(15.dp)),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                screensGames[currentIndex](navigateClick)
            }
        }
    }
}

val screensGames = listOf<@Composable (Boolean) -> Unit>(
    { navigateClick -> MenuGames(navigateClick) },
    { navigateClick -> GameMemory(navigateClick) },
    { navigateClick -> GameSnake(navigateClick) },
    { navigateClick -> GameVelha(navigateClick) },
    { navigateClick -> GameTetris(navigateClick) },
    { navigateClick -> GamePacman(navigateClick) },
    { navigateClick -> GameSudoku(navigateClick) }
)

val textsGames = listOf(
    "Games Plus",
    "Memória",
    "Snake",
    "Velha",
    "Tetris",
    "Pacman",
    "Sudoku"
)

@Composable
fun NavigationDrawer(
    color: Color,
    onItemClicked: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
    ) {
        NavigationItem(
            icon = null,
            box = false,
            text = "lista de jogos",
            index = 0,
            topPadding = 100.dp
        ) { onItemClicked(it) }
        NavigationItem(
            icon = painterResource(id = R.drawable.ic_game_memory2),
            box = true,
            text = "Memória",
            index = 1
        ) { onItemClicked(it) }
        NavigationItem(
            icon = painterResource(id = R.drawable.ic_game_snake),
            box = true,
            text = "Snake",
            index = 2
        ) { onItemClicked(it) }
        NavigationItem(
            icon = painterResource(id = R.drawable.ic_game_velha2),
            box = true,
            text = "Velha",
            index = 3
        ) { onItemClicked(it) }
        NavigationItem(
            icon = painterResource(id = R.drawable.ic_game_tetris),
            box = true,
            text = "Tetris",
            index = 4
        ) { onItemClicked(it) }
        NavigationItem(
            icon = painterResource(id = R.drawable.ic_game_pacman),
            box = true,
            text = "Pacman",
            index = 5
        ) { onItemClicked(it) }
        NavigationItem(
            icon = painterResource(id = R.drawable.ic_game_sudoku),
            box = true,
            text = "Sudoku",
            index = 6
        ) { onItemClicked(it) }
    }
}

@Composable
fun NavigationItem(
    icon: Painter?,
    box: Boolean,
    text: String,
    index: Int,
    topPadding: Dp = 8.dp,
    bottomPadding: Dp = 8.dp,
    onItemClicked: (Int) -> Unit
) {
    val spacer = if (box) 20.dp else 50.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = spacer, top = topPadding, bottom = bottomPadding)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onItemClicked(index) }
    ) {
        Box(
            contentAlignment = Alignment.CenterStart
        ) {
            if (box) {
                Box(
                    modifier = Modifier
                        .size(220.dp, 35.dp)
                        .background(
                            Color.White.copy(alpha = 0.2f),
                            CutCornerShape(35.dp, 0.dp, 35.dp, 0.dp)
                        )
                )
            }
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon?.let {
                    Spacer(modifier = Modifier.width(10.dp))
                    Image(
                        painter = icon,
                        contentDescription = "Item Image",
                        colorFilter = ColorFilter.tint(Color.White),
                        modifier = Modifier.size(45.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = text,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = Color.White,
                        fontSize = 20.sp
                    )
                )
            }
        }
    }
}

@Composable
fun MenuGames(
    navigateClick: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Selecione seu jogo!"
        )
    }
}

@Preview
@Composable
private fun PreviewApp() {
    AppScreen()
}