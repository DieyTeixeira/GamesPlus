package com.dieyteixeira.gamesplus.games.game_sudoku

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dieyteixeira.gamesplus.ui.theme.DarkYellow
import com.dieyteixeira.gamesplus.ui.theme.Gray
import com.dieyteixeira.gamesplus.ui.theme.Green
import com.dieyteixeira.gamesplus.ui.theme.Red

@Composable
fun SudokuCell(
    colorPlayer: Color,
    value: Set<Int>,
    row: Int,
    col: Int,
    isHighlighted: Boolean,
    isDuplicate: Boolean,
    isCorrect: Boolean,
    isLocked: Boolean,
    onClick: () -> Unit,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(if (value.size == 1) 33.dp else 36.dp)
                        .background(
                            color = when {
                                value.isEmpty() -> Color.Transparent
                                isHighlighted && isCorrect && !isLocked -> Green.copy(alpha = 0.8f)
                                isHighlighted && isLocked -> Gray
                                isLocked -> Gray.copy(alpha = 0.2f)
                                isCorrect -> Green.copy(alpha = 0.4f)
                                isHighlighted -> colorPlayer.copy(alpha = 0.8f)
                                else -> colorPlayer.copy(alpha = 0.2f)
                            },
                            shape = when {
                                value.size == 1 -> RoundedCornerShape(100)
                                else -> RoundedCornerShape(20)
                            }
                        )
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onClick() },
                    contentAlignment = Alignment.Center
                ) {}
                Box(
                    modifier = Modifier
                        .height(38.dp)
                        .width(35.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onClick() },
                    contentAlignment = Alignment.Center
                ) {
                    if (value.isNotEmpty()) {
                        if (value.size == 1) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = value.first().toString(),
                                    fontSize = 16.sp,
                                    color = when {
                                        isDuplicate && !isLocked -> Red
                                        isHighlighted -> Color.White
                                        else -> Color.Black

                                    }
                                )
                            }
                        } else {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                for (i in 0..2) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        for (j in 0..2) {
                                            val numberIndex = i * 3 + j
                                            val number = value.sorted().getOrNull(numberIndex)
                                            if (number != null) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(12.dp),
                                                    contentAlignment = Alignment.TopCenter
                                                ) {
                                                    Text(
                                                        text = number.toString(),
                                                        fontSize = 8.sp,
                                                        color = when {
                                                            isDuplicate && !isLocked -> Red
                                                            isHighlighted -> Color.White
                                                            else -> Color.Black

                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Linha vertical mais grossa entre blocos
            if (col >= 0 && col != 2 && col != 5 && col != 8) {
                Spacer(
                    modifier = Modifier
                        .width(1.dp)
                        .height(30.dp)
                        .background(Color.LightGray)
                )
            }
        }

        // Linha horizontal mais grossa entre blocos
        if (row >= 0 && row != 2 && row != 5 && row != 8) {
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .width(30.dp)
                    .background(Color.LightGray)
            )
        }
    }
}

@Composable
fun NumberSelector(
    colorPlayer: Color,
    selectedNumber: MutableState<Int?>,
    boardState: Array<Array<MutableSet<Int>>>,
    difficulty: String,
    onNumberClick: (Int) -> Unit
) {
    val numberCounts = if (difficulty != "Difícil") countOccurrences(boardState) else emptyMap()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp), // Espaço entre linhas
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Primeira linha com 5 números (1 a 5)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            (1..5).forEach { number ->
                NumberBox(
                    colorPlayer = colorPlayer,
                    number = number,
                    selectedNumber = selectedNumber,
                    numberCounts = numberCounts,
                    onNumberClick = onNumberClick
                )
                if (number < 5) {
                    Spacer(modifier = Modifier.width(5.dp))
                }
            }
        }

        // Segunda linha com 4 números (6 a 9)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            (6..9).forEach { number ->
                NumberBox(
                    colorPlayer = colorPlayer,
                    number = number,
                    selectedNumber = selectedNumber,
                    numberCounts = numberCounts,
                    onNumberClick = onNumberClick
                )
                if (number < 9) {
                    Spacer(modifier = Modifier.width(5.dp))
                }
            }
        }
    }
}

@Composable
fun NumberBox(
    colorPlayer: Color,
    number: Int,
    selectedNumber: MutableState<Int?>,
    numberCounts: Map<Int, Int>,
    onNumberClick: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .width(60.dp)
            .height(70.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                selectedNumber.value = number
                onNumberClick(number)
            }
            .background(
                color = if (selectedNumber.value == number) colorPlayer else Gray.copy(alpha = 0.4f),
                shape = RoundedCornerShape(10.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = number.toString(),
                fontSize = 28.sp,
                color = if (selectedNumber.value == number) Color.White else Color.Black
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = numberCounts[number]?.toString() ?: "0",
                fontSize = 14.sp,
                color = (if (selectedNumber.value == number) Color.White else Color.Black).copy(alpha = 0.6f)
            )
        }
    }
}