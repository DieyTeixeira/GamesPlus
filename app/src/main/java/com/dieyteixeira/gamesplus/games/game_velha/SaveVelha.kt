package com.dieyteixeira.gamesplus.games.game_velha

import android.content.Context
import android.content.SharedPreferences

fun saveVictoryVelha(
    context: Context,
    winnerName: String,
    opponentName: String,
    gameName: String,
) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("game_scores", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    // Cria a chave
    val key = "${gameName}_${winnerName}vs${opponentName}"

    // Recupera o número de vitórias do jogador e incrementa
    val currentWins = sharedPreferences.getInt(key, 0)
    val newWins = currentWins + 1

    // Salva o novo número de vitórias
    editor.putInt(key, newWins)
    editor.apply()
}

fun getVictoryVelha(
    context: Context,
    winnerName: String,
    opponentName: String,
    gameName: String,
): Int {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("game_scores", Context.MODE_PRIVATE)

    // Cria a chave
    val key = "${gameName}_${winnerName}vs${opponentName}"

    // Recupera o número de vitórias
    return sharedPreferences.getInt(key, 0)
}