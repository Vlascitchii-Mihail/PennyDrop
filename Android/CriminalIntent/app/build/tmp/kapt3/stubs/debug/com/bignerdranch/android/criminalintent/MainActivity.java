package com.bignerdranch.android.criminalintent;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0003\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H\u0002J\b\u0010\u0006\u001a\u00020\u0007H\u0002J\u0012\u0010\b\u001a\u00020\u00052\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0014J\u0010\u0010\u000b\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\rH\u0016J-\u0010\u000e\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u00102\u000e\u0010\u0011\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00130\u00122\u0006\u0010\u0014\u001a\u00020\u0015H\u0016\u00a2\u0006\u0002\u0010\u0016J\b\u0010\u0017\u001a\u00020\u0005H\u0002\u00a8\u0006\u0018"}, d2 = {"Lcom/bignerdranch/android/criminalintent/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Lcom/bignerdranch/android/criminalintent/CrimeListFragment$Callbacks;", "()V", "askUserForOpeningCallSettings", "", "hasReadContactsPermission", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCrimeSelected", "crimeId", "Ljava/util/UUID;", "onRequestPermissionsResult", "requestCode", "", "permissions", "", "", "grantResults", "", "(I[Ljava/lang/String;[I)V", "requestPermission", "app_debug"})
public final class MainActivity extends androidx.appcompat.app.AppCompatActivity implements com.bignerdranch.android.criminalintent.CrimeListFragment.Callbacks {
    
    public MainActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    public void onCrimeSelected(@org.jetbrains.annotations.NotNull()
    java.util.UUID crimeId) {
    }
    
    /**
     * @param this - ActivityContext or ApplicationContext
     * @param Manifest.permission.READ_CONTACTS - checked permission
     */
    private final boolean hasReadContactsPermission() {
        return false;
    }
    
    private final void requestPermission() {
    }
    
    /**
     * @param requestCode - the requestCode from ActivityCompat.requestPermissions()
     * @param permissions the array of permissions from ActivityCompat.requestPermissions()
     * @param grantResults : Int an array of results for each permission from the permission's array
     */
    @java.lang.Override()
    public void onRequestPermissionsResult(int requestCode, @org.jetbrains.annotations.NotNull()
    java.lang.String[] permissions, @org.jetbrains.annotations.NotNull()
    int[] grantResults) {
    }
    
    private final void askUserForOpeningCallSettings() {
    }
}