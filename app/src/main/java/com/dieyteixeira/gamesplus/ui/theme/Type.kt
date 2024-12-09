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
        fontFamily = FontFamily(Font(R.font.font_liquid_crystal_ri)),
        letterSpacing = 1.2.sp
    ),

    // cabeçalhos memória
    headlineLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.font_baron_neue_b))
    ),
    // textos gerais memória
    displaySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.font_alwyn_b)),
        letterSpacing = 1.2.sp
    ),

    // números dos cards
    titleLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.font_klavika_bi))
    ),

    // números sudoku
    titleMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.font_klavika_ri))
    ),
    // grid sudoku
    headlineMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.font_klavika_r))
    ),

    labelSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    )
)