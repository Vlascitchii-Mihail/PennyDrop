package com.bignerdranch.android.criminalintent;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0006R\u001d\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0086\u000e\u00a2\u0006\u0010\n\u0002\u0010\u0011\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010\u00a8\u0006\u0015"}, d2 = {"Lcom/bignerdranch/android/criminalintent/CrimeListViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "crimeListLiveData", "Landroidx/lifecycle/LiveData;", "", "Lcom/bignerdranch/android/criminalintent/Crime;", "getCrimeListLiveData", "()Landroidx/lifecycle/LiveData;", "crimeRepository", "Lcom/bignerdranch/android/criminalintent/CrimeRepository;", "dataListSize", "", "getDataListSize", "()Ljava/lang/Integer;", "setDataListSize", "(Ljava/lang/Integer;)V", "Ljava/lang/Integer;", "addCrime", "", "crime", "app_debug"})
public final class CrimeListViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.Nullable()
    private java.lang.Integer dataListSize = 0;
    private final com.bignerdranch.android.criminalintent.CrimeRepository crimeRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<com.bignerdranch.android.criminalintent.Crime>> crimeListLiveData = null;
    
    public CrimeListViewModel() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getDataListSize() {
        return null;
    }
    
    public final void setDataListSize(@org.jetbrains.annotations.Nullable()
    java.lang.Integer p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.bignerdranch.android.criminalintent.Crime>> getCrimeListLiveData() {
        return null;
    }
    
    public final void addCrime(@org.jetbrains.annotations.NotNull()
    com.bignerdranch.android.criminalintent.Crime crime) {
    }
}