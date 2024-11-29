package com.dieyteixeira.gamesplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dieyteixeira.gamesplus.ui.screen.AppScreen
import com.dieyteixeira.gamesplus.ui.screen.GamesScreen
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