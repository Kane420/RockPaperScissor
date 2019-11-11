package com.example.CoKanerockpaperscissor.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.CoKanerockpaperscissor.model.Gesture
import java.util.*

@Entity(tableName = "gameTable")
data class Game(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Long? = null,
    @ColumnInfo(name = "result") var result: String,
    @ColumnInfo(name = "date") var date: Date,
    @ColumnInfo(name = "userAction") var userAction: Gesture,
    @ColumnInfo(name = "computerAction") var computerAction: Gesture
)