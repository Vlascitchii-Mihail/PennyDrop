package com.bignerdranch.android.criminalintent;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\bJ\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0005J\u000e\u0010\u0015\u001a\u00020\u00132\u0006\u0010\u0011\u001a\u00020\bR\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\"\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\b0\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/bignerdranch/android/criminalintent/CrimeDetailViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "crimeIdLiveData", "Landroidx/lifecycle/MutableLiveData;", "Ljava/util/UUID;", "crimeLiveData", "Landroidx/lifecycle/LiveData;", "Lcom/bignerdranch/android/criminalintent/Crime;", "getCrimeLiveData", "()Landroidx/lifecycle/LiveData;", "setCrimeLiveData", "(Landroidx/lifecycle/LiveData;)V", "crimeRepository", "Lcom/bignerdranch/android/criminalintent/CrimeRepository;", "getPhotoFile", "Ljava/io/File;", "crime", "loadCrime", "", "crimeId", "saveCrime", "app_debug"})
public final class CrimeDetailViewModel extends androidx.lifecycle.ViewModel {
    private final com.bignerdranch.android.criminalintent.CrimeRepository crimeRepository = null;
    private final androidx.lifecycle.MutableLiveData<java.util.UUID> crimeIdLiveData = null;
    @org.jetbrains.annotations.NotNull()
    private androidx.lifecycle.LiveData<com.bignerdranch.android.criminalintent.Crime> crimeLiveData;
    
    public CrimeDetailViewModel() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.bignerdranch.android.criminalintent.Crime> getCrimeLiveData() {
        return null;
    }
    
    public final void setCrimeLiveData(@org.jetbrains.annotations.NotNull()
    androidx.lifecycle.LiveData<com.bignerdranch.android.criminalintent.Crime> p0) {
    }
    
    public final void loadCrime(@org.jetbrains.annotations.NotNull()
    java.util.UUID crimeId) {
    }
    
    public final void saveCrime(@org.jetbrains.annotations.NotNull()
    com.bignerdranch.android.criminalintent.Crime crime) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.io.File getPhotoFile(@org.jetbrains.annotations.NotNull()
    com.bignerdranch.android.criminalintent.Crime crime) {
        return null;
    }
}