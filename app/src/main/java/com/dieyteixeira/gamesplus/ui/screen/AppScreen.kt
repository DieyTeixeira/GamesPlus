package com.dieyteixeira.gamesplus.ui.screen

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dieyteixeira.gamesplus.games.game_memory.GameMemory
import com.dieyteixeira.gamesplus.ui.theme.Green500

@Composable
fun AppScreen() {
    Surface(color = Green500) {
        var currentIndex by remember { mutableStateOf(0) }
        var navigateClick by remember { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxSize()) {
            NavigationDrawer(
                color = Green500,
                onItemClicked = { index ->
                    currentIndex = index
                    navigateClick = false
                }
            )
            BodyContent(
                currentIndex = currentIndex,
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
    navigateClick: Boolean = false,
    onMenuToggle: () -> Unit
) {

    val offSetAnim by animateDpAsState(targetValue = if (navigateClick) 253.dp else 0.dp)
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row{
                    Image(
                        imageVector = Icons.Filled.List,
                        contentDescription = "Menu",
                        modifier = Modifier
                            .clickable { onMenuToggle() }
                    )
                    Text(text = textsGames2[currentIndex])
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray, shape = RoundedCornerShape(15.dp))
                        .clip(RoundedCornerShape(15.dp)),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    screensGames2[currentIndex]()
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
val screensGames2 = listOf<@Composable () -> Unit>(
    { MenuGames() },
    { GameMemory(color = getRandomColorGames()) }
)

val textsGames2 = listOf(
    "Menu Games",
    "Game Memory"
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
            icon = Icons.Filled.List,
            text = "Menu",
            index = 0,
            topPadding = 100.dp
        ) { onItemClicked(it) }
        NavigationItem(
            icon = Icons.Filled.Gamepad,
            text = "Jogo da Memória",
            index = 1
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
            .clickable { onItemClicked(index) }
    ) {
        Row(
            modifier = Modifier
                .size(175.dp, 35.dp)
                .background(Color.White.copy(alpha = 0.2f), CutCornerShape(35.dp, 0.dp, 35.dp, 0.dp))
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