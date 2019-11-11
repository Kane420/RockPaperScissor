package com.example.CoKanerockpaperscissor.model

import androidx.room.TypeConverter
import java.util.*


class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun gestureToInt(gesture: Gesture): Int? {
        return gesture.value
    }

    @TypeConverter
    fun intToGesture(value: Int): Gesture {
        return when (value) {
            0 -> Gesture.ROCK
            1 -> Gesture.PAPER
            2 -> Gesture.SCISSOR
            else -> Gesture.ROCK
        }
    }
}