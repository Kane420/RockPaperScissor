package com.example.CoKanerockpaperscissor.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.CoKanerockpaperscissor.model.Game

@Dao
interface GameDao {

    @Query("SELECT * FROM gameTable ORDER BY date DESC")
    suspend fun getAllGames(): List<Game>

    @Insert
    suspend fun insertGame(game: Game)

    @Query("DELETE FROM gameTable")
    suspend fun deleteAllGames()

    @Query("SELECT COUNT() FROM gameTable WHERE result = 'You win!!!!!!!'")
    suspend fun getWins(): Int

    @Query("SELECT COUNT() FROM gameTable WHERE result = 'Draw'")
    suspend fun getDraws(): Int

    @Query("SELECT COUNT() FROM gameTable WHERE result = 'Computer wins!'")
    suspend fun getLosses(): Int
}
