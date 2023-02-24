package com.example.fitnessapp.ui.Fragments.viewmodels

import androidx.lifecycle.ViewModel
import com.example.fitnesstrackingapp.Repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


class StatisticsViewModel @Inject
constructor(
    val repository:
    MainRepository
) :
    ViewModel() {
}