package com.bignerdranch.android.criminalintent;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0004\u0018\u0000 \u001a2\u00020\u0001:\u0001\u001aB\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011J\u0016\u0010\u0012\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00110\u00132\u0006\u0010\u0014\u001a\u00020\u0015J\u0012\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\u00170\u0013J\u000e\u0010\u0018\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010\u0019\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n \u000b*\u0004\u0018\u00010\n0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\f\u001a\n \u000b*\u0004\u0018\u00010\r0\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2 = {"Lcom/bignerdranch/android/criminalintent/CrimeRepository;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "crimeDao", "Ldata_base/CrimeDao;", "database", "Ldata_base/CrimeDatabase;", "executor", "Ljava/util/concurrent/ExecutorService;", "kotlin.jvm.PlatformType", "filesDir", "Ljava/io/File;", "addCrime", "", "crime", "Lcom/bignerdranch/android/criminalintent/Crime;", "getCrime", "Landroidx/lifecycle/LiveData;", "id", "Ljava/util/UUID;", "getCrimes", "", "getPhotoFile", "updateCrime", "Companion", "app_debug"})
public final class CrimeRepository {
    
    /**
     * @param context.applicationContext - app's context - CriminalIntentApplication : Application
     * @param CrimeDatabase::class.java - database's class
     * @param DATABASE_NAME - database's name
     */
    private final data_base.CrimeDatabase database = null;
    private final java.util.concurrent.ExecutorService executor = null;
    private final java.io.File filesDir = null;
    private final data_base.CrimeDao crimeDao = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.bignerdranch.android.criminalintent.CrimeRepository.Companion Companion = null;
    private static com.bignerdranch.android.criminalintent.CrimeRepository INSTANCE;
    
    private CrimeRepository(android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.bignerdranch.android.criminalintent.Crime>> getCrimes() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.bignerdranch.android.criminalintent.Crime> getCrime(@org.jetbrains.annotations.NotNull()
    java.util.UUID id) {
        return null;
    }
    
    public final void updateCrime(@org.jetbrains.annotations.NotNull()
    com.bignerdranch.android.criminalintent.Crime crime) {
    }
    
    public final void addCrime(@org.jetbrains.annotations.NotNull()
    com.bignerdranch.android.criminalintent.Crime crime) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.io.File getPhotoFile(@org.jetbrains.annotations.NotNull()
    com.bignerdranch.android.criminalintent.Crime crime) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0005\u001a\u00020\u0004J\u000e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/bignerdranch/android/criminalintent/CrimeRepository$Companion;", "", "()V", "INSTANCE", "Lcom/bignerdranch/android/criminalintent/CrimeRepository;", "get", "initialize", "", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        public final void initialize(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.bignerdranch.android.criminalintent.CrimeRepository get() {
            return null;
        }
    }
}