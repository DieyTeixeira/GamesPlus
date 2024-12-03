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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Gamepad
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dieyteixeira.gamesplus.R
import com.dieyteixeira.gamesplus.games.game_memory.GameMemory
import com.dieyteixeira.gamesplus.games.game_snake.GameSnake
import com.dieyteixeira.gamesplus.ui.theme.Blue
import com.dieyteixeira.gamesplus.ui.theme.DarkBlue
import com.dieyteixeira.gamesplus.ui.theme.DarkYellow
import com.dieyteixeira.gamesplus.ui.theme.Orange

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
                onMenuToggle = { navigateClick = !navigateClick }
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
                screensGames[currentIndex]()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
val screensGames = listOf<@Composable () -> Unit>(
    { MenuGames() },
    { GameMemory() },
    { GameSnake() }
)

val textsGames = listOf(
    "Games Plus",
    "Jogo da Memória",
    "Jogo da Cobrinha"
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
            icon = Icons.AutoMirrored.Filled.List,
            text = "Lista",
            index = 0,
            topPadding = 100.dp
        ) { onItemClicked(it) }
        NavigationItem(
            icon = Icons.Filled.Gamepad,
            text = "Jogo da Memória",
            index = 1
        ) { onItemClicked(it) }
        NavigationItem(
            icon = Icons.Filled.Gamepad,
            text = "Jogo da Cobrinha",
            index = 2
        ) { onItemClicked(it) }
    }
}

@Composable
fun NavigationItem(
    icon: ImageVector,
    text: String,
    index: Int,
    topPadding: Dp = 8.dp,
    bottomPadding: Dp = 8.dp,
    onItemClicked: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp, top = topPadding, bottom = bottomPadding)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onItemClicked(index) }
    ) {
        Row(
            modifier = Modifier
                .size(200.dp, 35.dp)
                .background(
                    Color.White.copy(alpha = 0.2f),
                    CutCornerShape(35.dp, 0.dp, 35.dp, 0.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 5.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    imageVector = icon,
                    contentDescription = "Item Image",
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier.size(30.dp)
                )
                Text(
                    text = text,
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
fun MenuGames() {
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