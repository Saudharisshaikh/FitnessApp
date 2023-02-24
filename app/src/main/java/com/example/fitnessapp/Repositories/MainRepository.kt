package com.example.fitnesstrackingapp.Repositories
import com.example.fitnessapp.db.Run
import com.example.fitnessapp.db.RunDAO
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


class MainRepository @Inject constructor(val runDAO: RunDAO) {

    suspend fun insertRun(run: Run) = runDAO.insertRun(run)

    suspend fun deleteRun(run: Run) = runDAO.deleteRun(run)

    // means get all runs sorted by date
    fun getAllRunsSortedByDate () = runDAO.getAllRunsSortedByTimeStamp()

    fun getAllRunsSortedByDistance () = runDAO.getALLRunsSortedByDistanceInMeters()

    fun getAllRunsSortedByTimeInMills () = runDAO.getAllRunsSortedByTimeInMills()

    fun getAllRunsSortedByAvgSpeed () = runDAO.getAllRunsSortedByavgSpeedInKmh()

    fun getAllRunsSortedByCaloriesBurned () = runDAO.getAllRunsSortedByCaloriesBurned()

    fun getTotalAvgSpeed () = runDAO.getAverageSpeedInKmh()

    fun getTotalDistance () = runDAO.getTotalDistance()

    fun getTotalTimeInMills () = runDAO.getTotalTimeMills()

    fun getTotalCaloriesBurned () = runDAO.getTotalBurnedCalories()
}

