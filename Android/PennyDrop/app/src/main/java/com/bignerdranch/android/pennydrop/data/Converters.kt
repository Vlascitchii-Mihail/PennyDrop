package com.bignerdranch.android.pennydrop.data

import androidx.room.TypeConverter
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class Converters {

    //DateTimeFormatter.ISO_OFFSET_DATE_TIME - parsing and printing OffsetDateTime to String
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @TypeConverter
    fun toOffsetDateTime(value: String?) = value?.let {

        //OffsetDateTime::from - convert to OffsetDateTime
        formatter.parse(it, OffsetDateTime::from)
    }

    //format() - Formats this date-time using the specified formatter.
    //This date-time will be passed to the formatter to produce a string.
    @TypeConverter
    fun fromOffsetDateTime(date: OffsetDateTime?) = date?.format(formatter)
}