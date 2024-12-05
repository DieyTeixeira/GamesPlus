package com.dieyteixeira.gamesplus.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dieyteixeira.gamesplus.R

val Typography = Typography(

    // textos gerais jogos, fonte digital
    displayMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.font_digital)),
        letterSpacing = 1.2.sp
    ),

    // cabeçalhos memória
    headlineLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.font_memory1))
    ),
    // textos gerais memória
    displaySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.font_memory2)),
        letterSpacing = 1.2.sp
    ),
    // números dos cards
    titleLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.font_memory3))
    ),

    labelSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    )
)