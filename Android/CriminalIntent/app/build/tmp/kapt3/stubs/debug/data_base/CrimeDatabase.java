package data_base;

import java.lang.System;

@androidx.room.TypeConverters(value = {data_base.CrimeTypeConverters.class})
@androidx.room.Database(entities = {com.bignerdranch.android.criminalintent.Crime.class}, version = 2)
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&\u00a8\u0006\u0005"}, d2 = {"Ldata_base/CrimeDatabase;", "Landroidx/room/RoomDatabase;", "()V", "crimeDao", "Ldata_base/CrimeDao;", "app_debug"})
public abstract class CrimeDatabase extends androidx.room.RoomDatabase {
    
    public CrimeDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract data_base.CrimeDao crimeDao();
}