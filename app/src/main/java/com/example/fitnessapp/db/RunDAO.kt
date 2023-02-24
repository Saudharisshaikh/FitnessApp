package com.example.fitnessapp.db

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
 interface RunDAO {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertRun (run: Run);

  @Delete
  suspend fun deleteRun(run: Run)

  @Query("SELECT * FROM running_table ORDER BY timestamp DESC")
  fun getAllRunsSortedByTimeStamp():LiveData<List<Run>>

  @Query("SELECT * FROM running_table ORDER BY timeInMills DESC")
  fun getAllRunsSortedByTimeInMills():LiveData<List<Run>>

  @Query("SELECT * FROM running_table ORDER BY caleriesBurned DESC")
  fun getAllRunsSortedByCaloriesBurned (): LiveData<List<Run>>

  @Query("SELECT * FROM running_table ORDER BY avgSpeedInKmh DESC")
  fun getAllRunsSortedByavgSpeedInKmh (): LiveData<List<Run>>

  @Query("SELECT * FROM running_table ORDER BY distanceInMeters DESC")
  fun  getALLRunsSortedByDistanceInMeters (): LiveData<List<Run>>

  @Query("SELECT SUM(timeInMills) FROM running_table")
  fun getTotalTimeMills ():LiveData<Long>

  @Query("SELECT SUM(caleriesBurned) FROM running_table")
  fun getTotalBurnedCalories ():LiveData<Int>

  @Query("SELECT SUM(distanceInMeters) FROM running_table")
  fun getTotalDistance (): LiveData<Int>

  @Query("SELECT AVG(avgSpeedInKmh) FROM running_table")
  fun getAverageSpeedInKmh (): LiveData<Float>

}