package data_base

import androidx.room.Dao
import androidx.room.Query
import com.bignerdranch.android.criminalintent.Crime
import java.util.UUID
import androidx.lifecycle.LiveData
import androidx.room.Update
import androidx.room.Insert

//@Dao - интерфейс доступа к базе данных
@Dao interface CrimeDao {

     //@Query - extracting information
     //LiveData<List<Crime>> provide an access to the data between 2 threads and starts the functions in the second thread
     @Query("SELECT * FROM Crime") fun getCrimes(): LiveData<List<Crime>>
     @Query("SELECT * FROM Crime WHERE id =(:id)") fun getCrime(id:UUID): LiveData<Crime?>

     //updating crime comparing ID
     @Update fun updateCrime(crime: Crime)

     //adding new Crime
     @Insert fun addCrime(crime: Crime)
}