package com.dieyteixeira.gamesplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.dieyteixeira.gamesplus.ui.screen.AppScreen
import com.dieyteixeira.gamesplus.ui.theme.GamesPlusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GamesPlusTheme {
                AppScreen()
            }
        }
    }
}