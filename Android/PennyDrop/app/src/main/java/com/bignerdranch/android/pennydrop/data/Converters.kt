package com.bignerdranch.android.pennydrop.data

import android.text.TextUtils
import androidx.room.TypeConverter
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class Converters {

    //DateTimeFormatter.ISO_OFFSET_DATE_TIME - parsing and printing OffsetDateTime to String
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    /**
     * @since toOffsetDateTime() - converts the endTime: OffsetDateTime?
     * and startTime: OffsetDateTime? to String from Game.class
     */
    @TypeConverter
    fun toOffsetDateTime(value: String?) = value?.let {

        //OffsetDateTime::from - convert to OffsetDateTime
        formatter.parse(it, OffsetDateTime::from)
    }

    /**
     * @since fromOffsetDateTime() - converts String to the endTime: OffsetDateTime?
     * and startTime: OffsetDateTime? from Game.class
     */
    //format() - Formats this date-time using the specified formatter.
    //This date-time will be passed to the formatter to produce a string.
    @TypeConverter
    fun fromOffsetDateTime(date: OffsetDateTime?) = date?.format(formatter)

    /**
     * @since fromGameStateToInt() - converts gameState: GameState from Game.class
     * to Int
     */
    @TypeConverter
    fun fromGameStateToInt(gameState: GameState?) =

        //@since ordinal - Возвращает порядковый номер этой константы
        // перечисления (ее положение в объявлении перечисления, где
        // исходной константе присваивается порядковый номер нуля).
        (gameState ?: GameState.Unknown).ordinal

    /**
     * @since fromIntToGameState() - converts Int to gameState: GameState from
     * Game.class
     */
    @TypeConverter
    fun fromIntToGameState(gameStateInt: Int?) =

        //values() - Returns an array containing the constants of this enum type, in the order they're declared.
        GameState.values().let { gameStateValues ->

            //.any() - Returns true if at least one element matches the given predicate.
            if (gameStateInt != null && gameStateValues.any { it.ordinal == gameStateInt}) {
                GameState.values()[gameStateInt]
            } else GameState.Unknown
        }

    /**
     * @since fromGameStateToInt() - converts String to filledSlots: List<Int>
     * from Game.class
     */
    @TypeConverter
    fun toIntList(value: String?) = value?.split(",")?.let {
        it.filter { numberString -> !TextUtils.isEmpty(numberString) }
            .map { numberString -> numberString.toInt() }
    } ?: emptyList()

    /**
     * @since fromIntToGameState() - filledSlots: List<Int> from Game.class
     * to String
     */
    @TypeConverter
    fun fromListOfIntToString(numbers: List<Int>?) =

        //joinToString() - Creates a string from all the elements separated
        //using separator and using the given prefix and postfix if supplied.
        //If the collection could be huge, you can specify a non-negative value of limit
        numbers?.joinToString(",") ?: ""
}





















