package com.dieyteixeira.gamesplus.games.game_tetris

import android.content.Context
import android.content.SharedPreferences

fun savePlayerRecordTetris(
    context: Context,
    playerName: String,
    gameScore: Int,
    gameName: String
) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("game_scores", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    // Cria a chave
    val recordScoreKey = "${gameName}_record_score"
    val recordPlayerKey = "${gameName}_record_player"

    // Recupera o recorde atual de movimentos
    val currentRecord = sharedPreferences.getInt(recordScoreKey, 0)

    // Verifica se o novo número de movimentos é menor que o recorde atual
    if (gameScore > 0 && gameScore > currentRecord) {
        // Salva o novo recorde de movimentos
        editor.putInt(recordScoreKey, gameScore)
        // Salva o nome do jogador que alcançou o recorde
        editor.putString(recordPlayerKey, playerName)
    }

    // Aplica as mudanças
    editor.apply()
}

fun getPlayerRecordTetris(
    context: Context,
    gameName: String
): Pair<Int, String?> {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("game_scores", Context.MODE_PRIVATE)

    // Cria as chaves
    val recordScoreKey = "${gameName}_record_score"
    val recordPlayerKey = "${gameName}_record_player"

    // Recupera os dados
    val recordScore = sharedPreferences.getInt(recordScoreKey, 0) // Valor padrão: Int.MAX_VALUE (se nenhum recorde)
    val recordPlayer = sharedPreferences.getString(recordPlayerKey, "-") // Valor padrão: "No record"

    // Retorna o recorde como um par de valores (movimentos, jogador)
    return Pair(recordScore, recordPlayer)
}

fun clearPlayerRecordTetris(context: Context, gameName: String) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("game_scores", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    // Cria as chaves para os registros
    val recordScoreKey = "${gameName}_record_score"
    val recordPlayerKey = "${gameName}_record_player"

    // Remove as chaves de recorde
    editor.remove(recordScoreKey)
    editor.remove(recordPlayerKey)

    // Aplica as mudanças
    editor.apply()
}