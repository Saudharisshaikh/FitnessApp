package com.example.fitnessapp.ui.Fragments.viewmodels

import androidx.lifecycle.ViewModel
import com.example.fitnesstrackingapp.Repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject
constructor(
    val repository:
    MainRepository
) :
    ViewModel() {

     val totalTimeRun = repository.getTotalTimeInMills()
     val totalDistance = repository.getTotalDistance()
     val totalCaloriesBurned = repository.getTotalCaloriesBurned()
     val avgSpeed = repository.getTotalAvgSpeed()

    val getRunSortedByDate = repository.getAllRunsSortedByDate()

}