package com.dieyteixeira.gamesplus.games.game_memory

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

enum class FlipCard(val angle: Float) {
    Forward(0f) {
        override val next: FlipCard get() = Previous
    },
    Previous(180f) {
        override val next: FlipCard get() = Forward
    };

    abstract val next: FlipCard
}

@ExperimentalMaterialApi
@Composable
fun FlipRotate(
    flipCard: FlipCard,
    player1Color: Color,
    player2Color: Color,
    onClick: () -> Unit,
    isMatched: Boolean,
    matchPlayer: Int,
    modifier: Modifier = Modifier,
    forward: @Composable () -> Unit,
    previous: @Composable () -> Unit
) {
    var showExplosion  by remember { mutableStateOf(false) }

    LaunchedEffect(isMatched) {
        if (isMatched) {
            showExplosion  = true
            delay(1000)
            showExplosion  = false
        }
    }

    val rotation = animateFloatAsState(
        targetValue = flipCard.angle,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        )
    )

    val scale = animateFloatAsState(
        targetValue = if (isMatched) 1f else 1f,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
    )

    Card(
        onClick = { onClick() },
        modifier = modifier
            .graphicsLayer {
                rotationY = rotation.value
                cameraDistance = 12f * density
                scaleX = scale.value
                scaleY = scale.value
            },
        shape = RoundedCornerShape(15.dp),
        elevation = 4.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (rotation.value <= 90f) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    forward()
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            rotationY = 180f
                        }
                ) {
                    previous()

                    if (showExplosion) {
                        SparksEffect(
                            modifier,
                            player1Color,
                            player2Color,
                            matchPlayer)
                    }
                }
            }
        }
    }
}

@Composable
fun SparksEffect(
    modifier: Modifier,
    player1Color: Color,
    player2Color: Color,
    matchPlayer: Int
) {
    val explosionColor = when (matchPlayer) {
        1 -> player1Color
        2 -> player2Color
        3 -> player1Color
        else -> Color.LightGray
    }

    val infiniteTransition = rememberInfiniteTransition()

    val radius = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
    )

    val alpha = infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val centerX = size.width / 2
        val centerY = size.height / 2

        // Gerando faíscas com dispersão e alpha
        drawCircle(
            color = explosionColor,
            radius = radius.value * size.minDimension, // expanding circle
            center = Offset(centerX, centerY),
            alpha = alpha.value // fade out the explosion
        )
    }
}