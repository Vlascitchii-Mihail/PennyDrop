package data_base

import androidx.room.TypeConverter
import java.util.Date
import java.util.UUID

//converts complex data to primitive data
class CrimeTypeConverters {

    //converts Data to int
    @TypeConverter fun fromDate(date: Date?) : Long? {
        return date?.time
    }

    //converts unt to Data
    @TypeConverter fun toDate(millisSinceEpoch: Long?): Date? {
        return millisSinceEpoch?.let {
            Date(it)
        }
    }

    //converts String to UUID
    @TypeConverter fun toUUID(uuid: String?) :UUID? {
        return UUID.fromString(uuid)
    }

    //converts UUID to String
    @TypeConverter fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }
}