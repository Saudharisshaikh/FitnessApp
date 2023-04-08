package com.example.fitnessapp.ui.Fragments.viewmodels

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessapp.db.Run
import com.example.fitnessapp.others.SortType
import com.example.fitnesstrackingapp.Repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject
constructor(val mainRepository: MainRepository
): ViewModel(){

   private val runSortedByDate = mainRepository.getAllRunsSortedByDate()
  private  val runSortedByDistance = mainRepository.getAllRunsSortedByDistance()
  private  val runSortedByCaloriesBurned = mainRepository.getAllRunsSortedByCaloriesBurned()
  private  val runSortedByAvgSpeed = mainRepository.getAllRunsSortedByAvgSpeed()
  private  val runSortedByTimeInMillis = mainRepository.getAllRunsSortedByTimeInMills()

    val runs = MediatorLiveData<List<Run>> ()

    var sortType = SortType.DATE
    init {

        runs.addSource(runSortedByDate){result ->

            if(sortType == SortType.DATE){
                result?.let{ runs.value = it }
            }
        }
        runs.addSource(runSortedByAvgSpeed){result ->

            if(sortType == SortType.AVG_SPEED){
                result?.let{ runs.value = it }
            }
        }
        runs.addSource(runSortedByDistance){result ->

            if(sortType == SortType.DISTANCE){
                result?.let{ runs.value = it }
            }
        }
        runs.addSource(runSortedByTimeInMillis){result ->

            if(sortType == SortType.RUNNING_TIME){
                result?.let{ runs.value = it }
            }
        }
        runs.addSource(runSortedByCaloriesBurned){result ->

            if(sortType == SortType.CALORIES_BURNED){
                result?.let{ runs.value = it }
            }
        }


    }


    fun sortRuns(sortType: SortType) = when(sortType){

        SortType.DATE -> runSortedByDate.value?.let {
            Log.d("--type", "sortRuns: Date")
            runs.value = it

        }
        SortType.AVG_SPEED -> runSortedByAvgSpeed.value?.let {
            Log.d("--type", "sortRuns: Speed")
            runs.value = it }
        SortType.DISTANCE -> runSortedByDistance.value?.let {
            Log.d("--type", "sortRuns: Distance")
            runs.value = it }
        SortType.CALORIES_BURNED -> runSortedByCaloriesBurned.value?.let {
            Log.d("--type", "sortRuns: calories")
            runs.value = it }
        SortType.RUNNING_TIME -> runSortedByTimeInMillis.value?.let {
            Log.d("--type", "sortRuns: Time")
            runs.value = it }
    }.also {
        this.sortType = sortType
    }

    fun insertRun(run: Run) = viewModelScope.launch {

        mainRepository.insertRun(run)
    }




}