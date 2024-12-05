package com.dieyteixeira.gamesplus.games.game_pacman

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dieyteixeira.gamesplus.ui.theme.GreenComp

@Composable
fun ButtonsPacman(onDirectionChange: (Pair<Int, Int>) -> Unit) {
    val buttonSize = 70
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
        Box(
            modifier = Modifier
                .height(buttonSize.dp)
                .width(buttonSize.dp)
                .background(
                    GreenComp,
                    RoundedCornerShape(10.dp)
                )
                .clickable { onDirectionChange(Pair(0, -1)) },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size((buttonSize - 20).dp)
            )
        }
        Row {
            Box(
                modifier = Modifier
                    .height(buttonSize.dp)
                    .width(buttonSize.dp)
                    .background(
                        GreenComp,
                        RoundedCornerShape(10.dp)
                    )
                    .clickable { onDirectionChange(Pair(-1, 0)) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size((buttonSize - 20).dp)
                )
            }
            Spacer(modifier = Modifier.size(buttonSize.dp))
            Box(
                modifier = Modifier
                    .height(buttonSize.dp)
                    .width(buttonSize.dp)
                    .background(
                        GreenComp,
                        RoundedCornerShape(10.dp)
                    )
                    .clickable { onDirectionChange(Pair(1, 0)) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size((buttonSize - 20).dp)
                )
            }
        }
        Box(
            modifier = Modifier
                .height(buttonSize.dp)
                .width(buttonSize.dp)
                .background(
                    GreenComp,
                    RoundedCornerShape(10.dp)
                )
                .clickable { onDirectionChange(Pair(0, 1)) },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size((buttonSize - 20).dp)
            )
        }
    }
}